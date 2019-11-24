package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.cfg.InheritanceState;
import cn.sexycode.myjpa.mapping.Join;
import cn.sexycode.myjpa.mapping.PersistentClass;
import cn.sexycode.myjpa.mapping.RootClass;
import cn.sexycode.util.core.cls.XClass;
import cn.sexycode.util.core.exception.AnnotationException;
import cn.sexycode.util.core.exception.AssertionFailure;
import cn.sexycode.util.core.str.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import java.util.HashMap;

/**
 * Stateful holder and processor for binding Entity information
 */
public class EntityBinder {
    private static final Logger LOG = LoggerFactory.getLogger(EntityBinder.class);

    private static final String NATURAL_ID_CACHE_SUFFIX = "##NaturalId";

    private MetadataBuildingContext context;

    private String name;

    private XClass annotatedClass;

    private PersistentClass persistentClass;

    private String discriminatorValue = "";

    private Boolean forceDiscriminator;

    private Boolean insertableDiscriminator;

    private boolean dynamicInsert;

    private boolean dynamicUpdate;

    private boolean explicitHibernateEntityAnnotation;

    //	private OptimisticLockType optimisticLockType;
    //	private PolymorphismType polymorphismType;
    private boolean selectBeforeUpdate;

    private int batchSize;

    private boolean lazy;

    private XClass proxyClass;

    private String where;

    // todo : we should defer to InFlightMetadataCollector.EntityTableXref for secondary table tracking;
    //		atm we use both from here; HBM binding solely uses InFlightMetadataCollector.EntityTableXref
    private java.util.Map<String, Join> secondaryTables = new HashMap<>();

    private java.util.Map<String, Object> secondaryTableJoins = new HashMap<>();

    //	private List<Filter> filters = new ArrayList<>();
    private InheritanceState inheritanceState;

    private boolean ignoreIdAnnotations;

    //	private AccessType propertyAccessType = AccessType.DEFAULT;
    private boolean wrapIdsInEmbeddedComponents;

    private String subselect;

    private boolean isCached;

    private String cacheConcurrentStrategy;

    private String cacheRegion;

    private boolean cacheLazyProperty;

    private String naturalIdCacheRegion;

    public boolean wrapIdsInEmbeddedComponents() {
        return wrapIdsInEmbeddedComponents;
    }

    /**
     * Use as a fake one for Collection of elements
     */
    public EntityBinder() {
    }

    public EntityBinder(Entity ejb3Ann, XClass annotatedClass, PersistentClass persistentClass,
            MetadataBuildingContext context) {
        this.context = context;
        this.persistentClass = persistentClass;
        this.annotatedClass = annotatedClass;
        bindEjb3Annotation(ejb3Ann);
        //		bindHibernateAnnotation( hibAnn );
    }

    public boolean isRootEntity() {
        // This is the best option I can think of here since PersistentClass is most likely not yet fully populated
        return persistentClass instanceof RootClass;
    }

    public void setDiscriminatorValue(String discriminatorValue) {
        this.discriminatorValue = discriminatorValue;
    }

    public void setForceDiscriminator(boolean forceDiscriminator) {
        this.forceDiscriminator = forceDiscriminator;
    }

    public void setInsertableDiscriminator(boolean insertableDiscriminator) {
        this.insertableDiscriminator = insertableDiscriminator;
    }

    private void bindEjb3Annotation(Entity ejb3Ann) {
        if (ejb3Ann == null) {
            throw new AssertionFailure("@Entity should always be not null");
        }
        if (BinderHelper.isEmptyAnnotationValue(ejb3Ann.name())) {
            name = StringUtils.unqualify(annotatedClass.getName());
        } else {
            name = ejb3Ann.name();
        }
    }
    public void bindEntity() {
        //		persistentClass.setAbstract( annotatedClass.isAbstract() );
        persistentClass.setClassName(annotatedClass.getName());
        persistentClass.setJpaEntityName(name);
        //persistentClass.setDynamic(false); //no longer needed with the Entity name refactoring?
        persistentClass.setEntityName(annotatedClass.getName());
        bindDiscriminatorValue();

        //		persistentClass.setLazy( lazy );
        if (proxyClass != null) {
            persistentClass.setProxyInterfaceName(proxyClass.getName());
        }
        persistentClass.setDynamicInsert(dynamicInsert);
        persistentClass.setDynamicUpdate(dynamicUpdate);

        if (persistentClass instanceof RootClass) {
            RootClass rootClass = (RootClass) persistentClass;
            boolean mutable = true;
            //priority on @Immutable, then @Entity.mutable()

                Entity entityAnn = annotatedClass.getAnnotation(Entity.class);
				/*if ( entityAnn != null ) {
					mutable = entityAnn.mutable();
				}*/

            //			rootClass.setMutable( mutable );
            //			rootClass.setExplicitPolymorphism( isExplicitPolymorphism( polymorphismType ) );

            if (StringUtils.isNotEmpty(where)) {
                //				rootClass.setWhere( where );
            }

			/*if ( cacheConcurrentStrategy != null ) {
				rootClass.setCacheConcurrencyStrategy( cacheConcurrentStrategy );
				rootClass.setCacheRegionName( cacheRegion );
				rootClass.setLazyPropertiesCacheable( cacheLazyProperty );
			}

			rootClass.setNaturalIdCacheRegionName( naturalIdCacheRegion );
*/
			/*boolean forceDiscriminatorInSelects = forceDiscriminator == null
					? context.getBuildingOptions().shouldImplicitlyForceDiscriminatorInSelect()
					: forceDiscriminator;

			rootClass.setForceDiscriminator( forceDiscriminatorInSelects );

			if ( insertableDiscriminator != null ) {
				rootClass.setDiscriminatorInsertable( insertableDiscriminator );
			}*/
        } else {
			/*if (explicitHibernateEntityAnnotation) {
				LOG.entityAnnotationOnNonRoot(annotatedClass.getName());
			}
			if (annotatedClass.isAnnotationPresent(Immutable.class)) {
				LOG.immutableAnnotationOnNonRoot(annotatedClass.getName());
			}*/
        }

        //		persistentClass.setCached( isCached );

        //		persistentClass.setOptimisticLockStyle( getVersioning( optimisticLockType ) );
        //		persistentClass.setSelectBeforeUpdate( selectBeforeUpdate );

        //set persister if needed
        //		Persister persisterAnn = annotatedClass.getAnnotation( Persister.class );
		/*Class persister = null;
		if ( persisterAnn != null ) {
			persister = persisterAnn.impl();
		}
		else {
			org.hibernate.annotations.Entity entityAnn = annotatedClass.getAnnotation( org.hibernate.annotations.Entity.class );
			if ( entityAnn != null && !BinderHelper.isEmptyAnnotationValue( entityAnn.persister() ) ) {
				try {
					persister = context.getBootstrapContext().getClassLoaderAccess().classForName( entityAnn.persister() );
				}
				catch (ClassLoadingException e) {
					throw new AnnotationException( "Could not find persister class: " + entityAnn.persister(), e );
				}
			}
		}
		if ( persister != null ) {
			persistentClass.setEntityPersisterClass( persister );
		}

		persistentClass.setBatchSize( batchSize );
*/
        //SQL overriding
		/*SQLInsert sqlInsert = annotatedClass.getAnnotation( SQLInsert.class );
		SQLUpdate sqlUpdate = annotatedClass.getAnnotation( SQLUpdate.class );
		SQLDelete sqlDelete = annotatedClass.getAnnotation( SQLDelete.class );
		SQLDeleteAll sqlDeleteAll = annotatedClass.getAnnotation( SQLDeleteAll.class );
		Loader loader = annotatedClass.getAnnotation( Loader.class );

		if ( sqlInsert != null ) {
			persistentClass.setCustomSQLInsert( sqlInsert.sql().trim(), sqlInsert.callable(),
					ExecuteUpdateResultCheckStyle.fromExternalName( sqlInsert.check().toString().toLowerCase(Locale.ROOT) )
			);

		}
		if ( sqlUpdate != null ) {
			persistentClass.setCustomSQLUpdate( sqlUpdate.sql(), sqlUpdate.callable(),
					ExecuteUpdateResultCheckStyle.fromExternalName( sqlUpdate.check().toString().toLowerCase(Locale.ROOT) )
			);
		}
		if ( sqlDelete != null ) {
			persistentClass.setCustomSQLDelete( sqlDelete.sql(), sqlDelete.callable(),
					ExecuteUpdateResultCheckStyle.fromExternalName( sqlDelete.check().toString().toLowerCase(Locale.ROOT) )
			);
		}
		if ( sqlDeleteAll != null ) {
			persistentClass.setCustomSQLDelete( sqlDeleteAll.sql(), sqlDeleteAll.callable(),
					ExecuteUpdateResultCheckStyle.fromExternalName( sqlDeleteAll.check().toString().toLowerCase(Locale.ROOT) )
			);
		}
		if ( loader != null ) {
			persistentClass.setLoaderName( loader.namedQuery() );
		}*/

		/*final JdbcEnvironment jdbcEnvironment = context.getMetadataCollector().getDatabase().getJdbcEnvironment();
		if ( annotatedClass.isAnnotationPresent( Synchronize.class )) {
			Synchronize synchronizedWith = annotatedClass.getAnnotation(Synchronize.class);

			String [] tables = synchronizedWith.value();
			for (String table : tables) {
				persistentClass.addSynchronizedTable(
						context.getBuildingOptions().getPhysicalNamingStrategy().toPhysicalTableName(
								jdbcEnvironment.getIdentifierHelper().toIdentifier( table ),
								jdbcEnvironment
						).render( jdbcEnvironment.getDialect() )
				);
			}
		}

		if ( annotatedClass.isAnnotationPresent(Subselect.class )) {
			Subselect subselect = annotatedClass.getAnnotation(Subselect.class);
			this.subselect = subselect.value();
		}

		//tuplizers
		if ( annotatedClass.isAnnotationPresent( Tuplizers.class ) ) {
			for (Tuplizer tuplizer : annotatedClass.getAnnotation( Tuplizers.class ).value()) {
				EntityMode mode = EntityMode.parse( tuplizer.entityMode() );
				//todo tuplizer.entityModeType
				persistentClass.addTuplizer( mode, tuplizer.impl().getName() );
			}
		}
		if ( annotatedClass.isAnnotationPresent( Tuplizer.class ) ) {
			Tuplizer tuplizer = annotatedClass.getAnnotation( Tuplizer.class );
			EntityMode mode = EntityMode.parse( tuplizer.entityMode() );
			//todo tuplizer.entityModeType
			persistentClass.addTuplizer( mode, tuplizer.impl().getName() );
		}

		for ( Filter filter : filters ) {
			String filterName = filter.name();
			String cond = filter.condition();
			if ( BinderHelper.isEmptyAnnotationValue( cond ) ) {
				FilterDefinition definition = context.getMetadataCollector().getFilterDefinition( filterName );
				cond = definition == null ? null : definition.getDefaultFilterCondition();
				if ( StringUtils.isEmpty( cond ) ) {
					throw new AnnotationException(
							"no filter condition found for filter " + filterName + " in " + this.name
					);
				}
			}
			persistentClass.addFilter(filterName, cond, filter.deduceAliasInjectionPoints(), 
					toAliasTableMap(filter.aliases()), toAliasEntityMap(filter.aliases()));
		}*/
        LOG.debug("Import with entity name {}", name);
        try {
            context.getMetadataCollector().addImport(name, persistentClass.getEntityName());
            String entityName = persistentClass.getEntityName();
            if (!entityName.equals(name)) {
                context.getMetadataCollector().addImport(entityName, entityName);
            }
        } catch (MappingException me) {
            throw new AnnotationException("Use of the same entity name twice: " + name, me);
        }

        processNamedEntityGraphs();
    }

    private void processNamedEntityGraphs() {
        processNamedEntityGraph(annotatedClass.getAnnotation(NamedEntityGraph.class));
        final NamedEntityGraphs graphs = annotatedClass.getAnnotation(NamedEntityGraphs.class);
        if (graphs != null) {
            for (NamedEntityGraph graph : graphs.value()) {
                processNamedEntityGraph(graph);
            }
        }
    }

    private void processNamedEntityGraph(NamedEntityGraph annotation) {
        if (annotation == null) {
            return;
        }
		/*context.getMetadataCollector().addNamedEntityGraph(
				new NamedEntityGraphDefinition( annotation, name, persistentClass.getEntityName() )
		);*/
    }

    public void bindDiscriminatorValue() {
		/*if ( StringUtils.isEmpty( discriminatorValue ) ) {
			Value discriminator = persistentClass.getDiscriminator();
			if ( discriminator == null ) {
				persistentClass.setDiscriminatorValue( name );
			}
			else if ( "character".equals( discriminator.getType().getName() ) ) {
				throw new AnnotationException(
						"Using default @DiscriminatorValue for a discriminator of type CHAR is not safe"
				);
			}
			else if ( "integer".equals( discriminator.getType().getName() ) ) {
				persistentClass.setDiscriminatorValue( String.valueOf( name.hashCode() ) );
			}
			else {
				persistentClass.setDiscriminatorValue( name ); //Spec compliant
			}
		}
		else {
			//persistentClass.getDiscriminator()
			persistentClass.setDiscriminatorValue( discriminatorValue );
		}*/
    }

    public void setWrapIdsInEmbeddedComponents(boolean wrapIdsInEmbeddedComponents) {
        this.wrapIdsInEmbeddedComponents = wrapIdsInEmbeddedComponents;
    }

    public java.util.Map<String, Join> getSecondaryTables() {
        return secondaryTables;
    }

    public void setInheritanceState(InheritanceState inheritanceState) {
        this.inheritanceState = inheritanceState;
    }

}
