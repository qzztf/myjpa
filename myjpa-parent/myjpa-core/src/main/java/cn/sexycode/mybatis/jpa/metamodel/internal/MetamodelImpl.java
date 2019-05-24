package cn.sexycode.mybatis.jpa.metamodel.internal;

import cn.sexycode.mybatis.jpa.binding.Metadata;
import cn.sexycode.mybatis.jpa.mapping.PersistentClass;
import cn.sexycode.mybatis.jpa.session.SessionFactory;

import javax.persistence.EntityGraph;
import javax.persistence.metamodel.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * implementation of the JPA {@link javax.persistence.metamodel.Metamodel} contract.
 *
 */
public class MetamodelImpl implements Metamodel, Serializable {
    private final Map<String, String> imports = new ConcurrentHashMap<>();
    private final Map<Class, String> entityProxyInterfaceMap = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> collectionRolesByEntityParticipant = new ConcurrentHashMap<>();


    private final Map<Class<?>, EntityTypeImpl<?>> jpaEntityTypeMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, EmbeddableTypeImpl<?>> jpaEmbeddableTypeMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, MappedSuperclassType<?>> jpaMappedSuperclassTypeMap = new ConcurrentHashMap<>();
    private final Map<String, EntityTypeImpl<?>> jpaEntityTypesByEntityName = new ConcurrentHashMap<>();

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
                    null
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

        MetadataContext context = new MetadataContext(sessionFactory, null);

        for (PersistentClass entityBinding : mappingMetadata.getEntityBindings()) {
            locateOrBuildEntityType(entityBinding, context);
        }

        this.jpaEntityTypeMap.putAll(context.getEntityTypeMap());
        this.jpaEmbeddableTypeMap.putAll(context.getEmbeddableTypeMap());
        this.jpaMappedSuperclassTypeMap.putAll(context.getMappedSuperclassTypeMap());
        this.jpaEntityTypesByEntityName.putAll(context.getEntityTypesByEntityName());

    }

    private static EntityTypeImpl<?> locateOrBuildEntityType(PersistentClass persistentClass, MetadataContext context) {
        EntityTypeImpl<?> entityType = context.locateEntityType(persistentClass);
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

        EntityTypeImpl entityType = new EntityTypeImpl(javaType, null, persistentClass);

        context.registerEntityType(persistentClass, entityType);
        return entityType;
    }

}
