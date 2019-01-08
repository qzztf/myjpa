/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.metamodel.internal;

import cn.sexycode.mybatis.jpa.session.SessionFactory;

import javax.persistence.EntityGraph;
import javax.persistence.metamodel.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hibernate implementation of the JPA {@link javax.persistence.metamodel.Metamodel} contract.
 *
 * @author Steve Ebersole
 * @author Emmanuel Bernard
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
        if (type == null) {
            throw new IllegalArgumentException("Not a managed type: " + cls);
        }
        return (ManagedType<X>) type;
    }

    @Override
    public <X> EmbeddableType<X> embeddable(Class<X> cls) {
        final EmbeddableType<?> embeddableType = jpaEmbeddableTypeMap.get(cls);
        if (embeddableType == null) {
            throw new IllegalArgumentException("Not an embeddable: " + cls);
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
}
