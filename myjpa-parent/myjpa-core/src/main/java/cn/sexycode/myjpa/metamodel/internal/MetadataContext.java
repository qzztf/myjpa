package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.mapping.PersistentClass;
import cn.sexycode.myjpa.session.SessionFactory;

import javax.persistence.MappedSuperclass;
import javax.persistence.metamodel.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Defines a context for storing information during the building of the {@link MetamodelImpl}.
 * <p/>
 * This contextual information includes data needing to be processed in a second pass as well as
 * cross-references into the built metamodel classes.
 * <p/>
 * At the end of the day, clients are interested in the {@link #getEntityTypeMap} and {@link #getEmbeddableTypeMap}
 * results, which represent all the registered {@linkplain #registerEntityType entities} and
 * {@linkplain #registerEmbeddedableType embeddables} respectively.
 *
 *
 *
 */
class MetadataContext {

    private final SessionFactory sessionFactory;
    private Set<MappedSuperclass> knownMappedSuperclasses;

    private Map<Class<?>, EntityTypeImpl<?>> entityTypes = new HashMap<>();
    private Map<String, EntityTypeImpl<?>> entityTypesByEntityName = new HashMap<>();
    private Map<PersistentClass, EntityTypeImpl<?>> entityTypesByPersistentClass = new HashMap<>();
    private Map<Class<?>, EmbeddableTypeImpl<?>> embeddables = new HashMap<>();
    //this list contains MappedSuperclass and EntityTypes ordered by superclass first
    private List<Object> orderedMappings = new ArrayList<>();
    /**
     * Stack of PersistentClass being process. Last in the list is the highest in the stack.
     */
    private List<PersistentClass> stackOfPersistentClassesBeingProcessed = new ArrayList<>();

    public MetadataContext(
            SessionFactory sessionFactory,
            Set<MappedSuperclass> mappedSuperclasses) {
        this.sessionFactory = sessionFactory;
        this.knownMappedSuperclasses = mappedSuperclasses;
    }

    /*package*/ SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    /**
     * Retrieves the {@linkplain Class java type} to {@link EntityTypeImpl} map.
     *
     * @return The {@linkplain Class java type} to {@link EntityTypeImpl} map.
     */
    public Map<Class<?>, EntityTypeImpl<?>> getEntityTypeMap() {
        return Collections.unmodifiableMap(entityTypes);
    }

    public Map<Class<?>, EmbeddableTypeImpl<?>> getEmbeddableTypeMap() {
        return Collections.unmodifiableMap(embeddables);
    }

    public Map<Class<?>, MappedSuperclassType<?>> getMappedSuperclassTypeMap() {
        // we need to actually build this map...
        final Map<Class<?>, MappedSuperclassType<?>> mappedSuperClassTypeMap = new HashMap<>();

		/*for ( MappedSuperclassTypeImpl mappedSuperclassType : mappedSuperclassByMappedSuperclassMapping.values() ) {
			mappedSuperClassTypeMap.put(
					mappedSuperclassType.getJavaType(),
					mappedSuperclassType
			);
		}*/

        return mappedSuperClassTypeMap;
    }

    /*package*/ void registerEntityType(PersistentClass persistentClass, EntityTypeImpl<?> entityType) {
        if (entityType.getBindableJavaType() != null) {
            entityTypes.put(entityType.getBindableJavaType(), entityType);
        }
        entityTypesByEntityName.put(persistentClass.getEntityName(), entityType);
        entityTypesByPersistentClass.put(persistentClass, entityType);
        orderedMappings.add(persistentClass);
    }

    /*package*/ void registerEmbeddedableType(EmbeddableTypeImpl<?> embeddableType) {
        embeddables.put(embeddableType.getJavaType(), embeddableType);
    }


    /**
     * Given a Hibernate {@link PersistentClass}, locate the corresponding JPA {@link org.hibernate.type.EntityType}
     * implementation.  May retur null if the given {@link PersistentClass} has not yet been processed.
     *
     * @param persistentClass The Hibernate (config time) metamodel instance representing an entity.
     * @return Tne corresponding JPA {@link org.hibernate.type.EntityType}, or null if not yet processed.
     */
    public EntityTypeImpl<?> locateEntityType(PersistentClass persistentClass) {
        return entityTypesByPersistentClass.get(persistentClass);
    }

    /**
     * Given a Java {@link Class}, locate the corresponding JPA {@link org.hibernate.type.EntityType}.  May
     * return null which could means that no such mapping exists at least at this time.
     *
     * @param javaType The java class.
     * @return The corresponding JPA {@link org.hibernate.type.EntityType}, or null.
     */
    public EntityTypeImpl<?> locateEntityType(Class<?> javaType) {
        return entityTypes.get(javaType);
    }

    /**
     * Given an entity-name, locate the corresponding JPA {@link org.hibernate.type.EntityType}.  May
     * return null which could means that no such mapping exists at least at this time.
     *
     * @param entityName The entity-name.
     * @return The corresponding JPA {@link org.hibernate.type.EntityType}, or null.
     */
    public EntityTypeImpl<?> locateEntityType(String entityName) {
        return entityTypesByEntityName.get(entityName);
    }

    public Map<String, EntityTypeImpl<?>> getEntityTypesByEntityName() {
        return Collections.unmodifiableMap(entityTypesByEntityName);
    }

    public void wrapUp() {
        /*if ( LOG.isTraceEnabled() ) {
            LOG.trace( "Wrapping up metadata context..." );
        }

        boolean staticMetamodelScanEnabled = JpaStaticMetaModelPopulationSetting
                .determineJpaMetaModelPopulationSetting( sessionFactory.getProperties() ) != JpaStaticMetaModelPopulationSetting.DISABLED;

        //we need to process types from superclasses to subclasses
        for ( Object mapping : orderedMappings ) {
            if ( PersistentClass.class.isAssignableFrom( mapping.getClass() ) ) {
                @SuppressWarnings("unchecked")
                final PersistentClass safeMapping = (PersistentClass) mapping;
                if ( LOG.isTraceEnabled() ) {
                    LOG.trace( "Starting entity [" + safeMapping.getEntityName() + ']' );
                }
                try {
                    final EntityTypeDescriptor<?> jpaMapping = entityTypesByPersistentClass.get( safeMapping );

                    applyIdMetadata( safeMapping, jpaMapping );
                    applyVersionAttribute( safeMapping, jpaMapping );

                    Iterator<Property> properties = safeMapping.getDeclaredPropertyIterator();
                    while ( properties.hasNext() ) {
                        final Property property = properties.next();
                        if ( property.getValue() == safeMapping.getIdentifierMapper() ) {
                            // property represents special handling for id-class mappings but we have already
                            // accounted for the embedded property mappings in #applyIdMetadata &&
                            // #buildIdClassAttributes
                            continue;
                        }
                        if ( safeMapping.isVersioned() && property == safeMapping.getVersion() ) {
                            // skip the version property, it was already handled previously.
                            continue;
                        }
                        final PersistentAttributeDescriptor attribute = attributeFactory.buildAttribute( jpaMapping, property );
                        if ( attribute != null ) {
                            jpaMapping.getInFlightAccess().addAttribute( attribute );
                        }
                    }

                    jpaMapping.getInFlightAccess().finishUp();

                    if ( staticMetamodelScanEnabled ) {
                        populateStaticMetamodel( jpaMapping );
                    }
                }
                finally {
                    if ( LOG.isTraceEnabled() ) {
                        LOG.trace( "Completed entity [" + safeMapping.getEntityName() + ']' );
                    }
                }
            }
            else if ( MappedSuperclass.class.isAssignableFrom( mapping.getClass() ) ) {
                @SuppressWarnings("unchecked")
                final MappedSuperclass safeMapping = (MappedSuperclass) mapping;
                if ( LOG.isTraceEnabled() ) {
                    LOG.trace( "Starting mapped superclass [" + safeMapping.getMappedClass().getName() + ']' );
                }
                try {
                    final MappedSuperclassTypeDescriptor<?> jpaType = mappedSuperclassByMappedSuperclassMapping.get( safeMapping );

                    applyIdMetadata( safeMapping, jpaType );
                    applyVersionAttribute( safeMapping, jpaType );

                    Iterator<Property> properties = safeMapping.getDeclaredPropertyIterator();
                    while ( properties.hasNext() ) {
                        final Property property = properties.next();
                        if ( safeMapping.isVersioned() && property == safeMapping.getVersion() ) {
                            // skip the version property, it was already handled previously.
                            continue;
                        }
                        final PersistentAttributeDescriptor attribute = attributeFactory.buildAttribute( jpaType, property );
                        if ( attribute != null ) {
                            jpaType.getInFlightAccess().addAttribute( attribute );
                        }
                    }
                    jpaType.getInFlightAccess().finishUp();

                    if ( staticMetamodelScanEnabled ) {
                        populateStaticMetamodel( jpaType );
                    }
                }
                finally {
                    if ( LOG.isTraceEnabled() ) {
                        LOG.trace( "Completed mapped superclass [" + safeMapping.getMappedClass().getName() + ']' );
                    }
                }
            }
            else {
                throw new AssertionFailure( "Unexpected mapping type: " + mapping.getClass() );
            }
        }

        if ( staticMetamodelScanEnabled ) {
            for ( EmbeddedTypeDescriptor embeddable : embeddables ) {
                populateStaticMetamodel( embeddable );
            }
        }*/
    }

    private <X> void applyIdMetadata(PersistentClass persistentClass, EntityTypeImpl<X> jpaEntityType) {
		/*if ( persistentClass.hasIdentifierProperty() ) {
			final Property declaredIdentifierProperty = persistentClass.getDeclaredIdentifierProperty();
			if ( declaredIdentifierProperty != null ) {
				jpaEntityType.getBuilder().applyIdAttribute(
						attributeFactory.buildIdAttribute( jpaEntityType, declaredIdentifierProperty )
				);
			}
		}
		else if ( persistentClass.hasIdentifierMapper() ) {
			@SuppressWarnings("unchecked")
			Iterator<Property> propertyIterator = persistentClass.getIdentifierMapper().getPropertyIterator();
			Set<SingularAttribute<? super X, ?>> attributes = buildIdClassAttributes( jpaEntityType, propertyIterator );
			jpaEntityType.getBuilder().applyIdClassAttributes( attributes );
		}
		else {
			final KeyValue value = persistentClass.getIdentifier();
			if ( value instanceof Component ) {
				final Component component = (Component) value;
				if ( component.getPropertySpan() > 1 ) {
					//FIXME we are an Hibernate embedded id (ie not type)
				}
				else {
					//FIXME take care of declared vs non declared property
					jpaEntityType.getBuilder().applyIdAttribute(
							attributeFactory.buildIdAttribute(
									jpaEntityType,
									(Property) component.getPropertyIterator().next()
							)
					);
				}
			}
		}*/
    }

    private <X> void applyIdMetadata(MappedSuperclass mappingType, MappedSuperclassTypeImpl<X> jpaMappingType) {
		/*if ( mappingType.hasIdentifierProperty() ) {
			final Property declaredIdentifierProperty = mappingType.getDeclaredIdentifierProperty();
			if ( declaredIdentifierProperty != null ) {
				jpaMappingType.getBuilder().applyIdAttribute(
						attributeFactory.buildIdAttribute( jpaMappingType, declaredIdentifierProperty )
				);
			}
		}
		//an MappedSuperclass can have no identifier if the id is set below in the hierarchy
		else if ( mappingType.getIdentifierMapper() != null ) {
			@SuppressWarnings("unchecked")
//			Iterator<Property> propertyIterator = mappingType.getIdentifierMapper().getPropertyIterator();
			Set<SingularAttribute<? super X, ?>> attributes = buildIdClassAttributes(
					jpaMappingType
			);
//			jpaMappingType.getBuilder().applyIdClassAttributes( attributes );
		}*/
    }

    private <X> void applyVersionAttribute(PersistentClass persistentClass, EntityTypeImpl<X> jpaEntityType) {
		/*final Property declaredVersion = persistentClass.getDeclaredVersion();
		if ( declaredVersion != null ) {
			jpaEntityType.getBuilder().applyVersionAttribute(
					attributeFactory.buildVersionAttribute( jpaEntityType, declaredVersion )
			);
		}*/
    }

    private <X> void applyVersionAttribute(MappedSuperclass mappingType) {
		/*final Property declaredVersion = mappingType.getDeclaredVersion();
		if ( declaredVersion != null ) {
			jpaMappingType.getBuilder().applyVersionAttribute(
					attributeFactory.buildVersionAttribute( jpaMappingType, declaredVersion )
			);
		}*/
    }

    private <X> Set<SingularAttribute<? super X, ?>> buildIdClassAttributes(
            AbstractIdentifiableType<X> ownerType) {
		/*if ( LOG.isTraceEnabled() ) {
			LOG.trace( "Building old-school composite identifier [" + ownerType.getJavaType().getName() + ']' );
		}*/
        Set<SingularAttribute<? super X, ?>> attributes = new HashSet<SingularAttribute<? super X, ?>>();
		/*while ( propertyIterator.hasNext() ) {
			attributes.add( attributeFactory.buildIdAttribute( ownerType, propertyIterator.next() ) );
		}*/
        return attributes;
    }

    private <X> void populateStaticMetamodel(AbstractManagedType<X> managedType) {
        final Class<X> managedTypeClass = managedType.getJavaType();
        if (managedTypeClass == null) {
            // should indicate MAP entity mode, skip...
            return;
        }
        final String metamodelClassName = managedTypeClass.getName() + '_';
        try {
            final Class metamodelClass = Class.forName(metamodelClassName, true, managedTypeClass.getClassLoader());
            // we found the class; so populate it...
            registerAttributes(metamodelClass, managedType);
        } catch (ClassNotFoundException ignore) {
            // nothing to do...
        }

        // todo : this does not account for @MappeSuperclass, mainly because this is not being tracked in our
        // internal metamodel as populated from the annotatios properly
        AbstractManagedType<? super X> superType = managedType.getSupertype();
        if (superType != null) {
            populateStaticMetamodel(superType);
        }
    }

    private final Set<Class> processedMetamodelClasses = new HashSet<Class>();

    private <X> void registerAttributes(Class metamodelClass, AbstractManagedType<X> managedType) {
        if (!processedMetamodelClasses.add(metamodelClass)) {
            return;
        }

        // push the attributes on to the metamodel class...
        for (Attribute<X, ?> attribute : managedType.getDeclaredAttributes()) {
            registerAttribute(metamodelClass, attribute);
        }

        if (managedType instanceof IdentifiableType) {
            final AbstractIdentifiableType<X> entityType = (AbstractIdentifiableType<X>) managedType;

            // handle version
			/*if ( entityType.hasDeclaredVersionAttribute() ) {
				registerAttribute( metamodelClass, entityType.getDeclaredVersion() );
			}*/

            // handle id-class mappings specially
            if (entityType.hasIdClass()) {
                final Set<SingularAttribute<? super X, ?>> attributes = entityType.getIdClassAttributesSafely();
                if (attributes != null) {
                    for (SingularAttribute<? super X, ?> attribute : attributes) {
                        registerAttribute(metamodelClass, attribute);
                    }
                }
            }
        }
    }

    private <X> void registerAttribute(Class metamodelClass, Attribute<X, ?> attribute) {
        final String name = attribute.getName();
        try {
            // there is a shortcoming in the existing Hibernate code in terms of the way MappedSuperclass
            // support was bolted on which comes to bear right here when the attribute is an embeddable type
            // defined on a MappedSuperclass.  We do not have the correct information to determine the
            // appropriate attribute declarer in such cases and so the incoming metamodelClass most likely
            // does not represent the declarer in such cases.
            //
            // As a result, in the case of embeddable classes we simply use getField rather than get
            // getDeclaredField
            final boolean allowNonDeclaredFieldReference =
                    attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.EMBEDDED
                            || attribute.getDeclaringType().getPersistenceType() == Type.PersistenceType.EMBEDDABLE;

            final Field field = allowNonDeclaredFieldReference
                    ? metamodelClass.getField(name)
                    : metamodelClass.getDeclaredField(name);
            try {
                // should be public anyway, but to be sure...
                field.setAccessible(true);
                field.set(null, attribute);
            } catch (IllegalAccessException e) {
                // todo : exception type?

            } catch (IllegalArgumentException e) {
                // most likely a mismatch in the type we are injecting and the defined field; this represents a
                // mismatch in how the annotation processor interpretted the attribute and how our metamodel
                // and/or annotation binder did.

//              This is particularly the case as arrays are nto handled propery by the StaticMetamodel generator

//				throw new AssertionFailure(
//						"Illegal argument on static metamodel field injection : " + metamodelClass.getName() + '#' + name
//								+ "; expected type :  " + attribute.getClass().getName()
//								+ "; encountered type : " + field.getType().getName()
//				);

            }
        } catch (NoSuchFieldException e) {
//			LOG.unableToLocateStaticMetamodelField( metamodelClass.getName(), name );
//			throw new AssertionFailure(
//					"Unable to locate static metamodel field : " + metamodelClass.getName() + '#' + name
//			);
        }
    }


    public void pushEntityWorkedOn(PersistentClass persistentClass) {
        stackOfPersistentClassesBeingProcessed.add(persistentClass);
    }


    private PersistentClass getEntityWorkedOn() {
        return stackOfPersistentClassesBeingProcessed.get(
                stackOfPersistentClassesBeingProcessed.size() - 1
        );
    }


    public Set<MappedSuperclass> getUnusedMappedSuperclasses() {
        return new HashSet<MappedSuperclass>(knownMappedSuperclasses);
    }
}
