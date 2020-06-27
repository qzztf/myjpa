package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.binding.Metadata;
import cn.sexycode.myjpa.mapping.PersistentClass;
import cn.sexycode.myjpa.session.SessionFactory;

import javax.persistence.EntityGraph;
import javax.persistence.metamodel.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * implementation of the JPA {@link javax.persistence.metamodel.Metamodel} contract.
 *
 */
public class MetamodelImpl implements Metamodel, Serializable {
    private final Map<String, String> imports = new ConcurrentHashMap<>();
    private final Map<Class, String> entityProxyInterfaceMap = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> collectionRolesByEntityParticipant = new ConcurrentHashMap<>();


    private final Map<Class<?>, EntityTypeDescriptor<?>> jpaEntityTypeMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, EmbeddedTypeDescriptor<?>> jpaEmbeddableTypeMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, MappedSuperclassType<?>> jpaMappedSuperclassTypeMap = new ConcurrentHashMap<>();
    private final Map<String, EntityTypeDescriptor<?>> jpaEntityTypesByEntityName = new ConcurrentHashMap<>();
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // NOTE : Relational/mapping information is not part of the JPA metamodel
    // (type system).  However, this relational/mapping info *is* part of the
    // Hibernate metamodel.  This is a mismatch.  Normally this is not a
    // problem - ignoring Hibernate's representation mode (entity mode),
    // an entity (or mapped superclass) *Class* always refers to the same
    // EntityType (JPA) and EntityPersister (Hibernate)..  The problem is
    // in regards to embeddables.  For an embeddable, as with the rest of its
    // metamodel, Hibernate combines the embeddable's relational/mapping
    // while JPA does not.  This is consistent with each's model paradigm.
    // However, it causes a mismatch in that while JPA expects a single
    // "type descriptor" for a given embeddable class, Hibernate incorporates
    // the relational/mapping info so we have a "type descriptor" for each
    // usage of that embeddable.  Think embeddable versus embedded.
    //
    // To account for this, we track both paradigms here...

    /**
     * There can be multiple instances of an Embeddable type, each one being relative to its parent entity.
     */
    private final Set<EmbeddedTypeDescriptor<?>> jpaEmbeddableTypes = new CopyOnWriteArraySet<>();

    private final transient Map<String, EntityGraph> entityGraphMap = new ConcurrentHashMap<>();
    private final SessionFactory sessionFactory;

    public MetamodelImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <X> EntityType<X> entity(Class<X> cls) {
        final EntityType<?> entityType = jpaEntityTypeMap.get(cls);
        if (entityType == null) {
            throw new IllegalArgumentException("Not an entity: " + cls);
        }
        return (EntityType<X>) entityType;
    }

    @Override
    public <X> ManagedType<X> managedType(Class<X> cls) {
        ManagedType<?> type = jpaEntityTypeMap.get(cls);
        if (type == null) {
            type = jpaMappedSuperclassTypeMap.get(cls);
        }
        if (type == null) {
            type = jpaEmbeddableTypeMap.get(cls);
        }
        /*if (type == null) {
            EntityTypeImpl entityType = new EntityTypeImpl(
                    cls,
                    null
            );
            jpaEntityTypeMap.put(cls, entityType);
            type = entityType;
//            throw new IllegalArgumentException("Not a managed type: " + cls);
        }*/
        return (ManagedType<X>) type;
    }

    @Override
    public <X> EmbeddableType<X> embeddable(Class<X> cls) {
        EmbeddableType<?> embeddableType = jpaEmbeddableTypeMap.get(cls);
        if (embeddableType == null) {
            EmbeddableTypeImpl type = new EmbeddableTypeImpl(
                    cls,
                    null, sessionFactory
            );
            jpaEmbeddableTypeMap.put(cls, type);
            embeddableType = type;
//            throw new IllegalArgumentException("Not an embeddable: " + cls);
        }
        return (EmbeddableType<X>) embeddableType;
    }

    @Override
    public Set<ManagedType<?>> getManagedTypes() {
        final Set<ManagedType<?>> managedTypes = new HashSet<ManagedType<?>>();
        managedTypes.addAll(jpaEntityTypeMap.values());
        managedTypes.addAll(jpaMappedSuperclassTypeMap.values());
        managedTypes.addAll(jpaEmbeddableTypeMap.values());
        return managedTypes;
    }

    @Override
    public Set<EntityType<?>> getEntities() {
        return new HashSet<>(jpaEntityTypesByEntityName.values());
    }

    @Override
    public Set<EmbeddableType<?>> getEmbeddables() {
        return new HashSet<>(jpaEmbeddableTypeMap.values());
    }

    /**
     * Prepare the metamodel using the information from the collection of Hibernate
     * {@link PersistentClass} models
     *
     * @param mappingMetadata The mapping information
     */
    public void initialize(Metadata mappingMetadata) {

        MetadataContext context = new MetadataContext(sessionFactory);

        for (PersistentClass entityBinding : mappingMetadata.getEntityBindings()) {
            locateOrBuildEntityType(entityBinding, context);
        }
        context.wrapUp();
        this.jpaEntityTypeMap.putAll(context.getEntityTypeMap());
        for ( EmbeddedTypeDescriptor<?> embeddable: jpaEmbeddableTypes ) {
            this.jpaEmbeddableTypeMap.put( embeddable.getJavaType(), embeddable );
        }
        this.jpaMappedSuperclassTypeMap.putAll(context.getMappedSuperclassTypeMap());
        this.jpaEntityTypesByEntityName.putAll(context.getEntityTypesByEntityName());

    }

    private static EntityTypeDescriptor<?> locateOrBuildEntityType(PersistentClass persistentClass, MetadataContext context) {
        EntityTypeDescriptor<?> entityType = context.locateEntityType(persistentClass);
        if (entityType == null) {
            entityType = buildEntityType(persistentClass, context);
        }
        return entityType;
    }

    //TODO remove / reduce @SW scope
    @SuppressWarnings("unchecked")
    private static EntityTypeImpl<?> buildEntityType(PersistentClass persistentClass, MetadataContext context) {
        final Class javaType = persistentClass.getMappedClass();
        context.pushEntityWorkedOn(persistentClass);

        EntityTypeImpl entityType = new EntityTypeImpl(javaType, null, persistentClass, context.getSessionFactory());

        context.registerEntityType(persistentClass, entityType);
        return entityType;
    }

}
