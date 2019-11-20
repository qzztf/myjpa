/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package cn.sexycode.myjpa.metamodel.model.domain.internal;

import cn.sexycode.myjpa.mapping.PersistentClass;
import cn.sexycode.myjpa.metamodel.model.domain.spi.EntityTypeDescriptor;
import cn.sexycode.myjpa.metamodel.model.domain.spi.IdentifiableTypeDescriptor;
import cn.sexycode.myjpa.session.SessionFactory;

import javax.persistence.metamodel.EntityType;
import java.io.Serializable;

/**
 * Defines the Hibernate implementation of the JPA {@link EntityType} contract.
 *
 * @author Steve Ebersole
 * @author Emmanuel Bernard
 */
public class EntityTypeImpl<J> extends AbstractIdentifiableType<J> implements EntityTypeDescriptor<J>, Serializable {
    private final String jpaEntityName;

    @SuppressWarnings("unchecked")
    public EntityTypeImpl(Class javaType, IdentifiableTypeDescriptor<? super J> superType,
            PersistentClass persistentClass, SessionFactory sessionFactory) {
        super(javaType, persistentClass.getEntityName(), superType,
                /*persistentClass.getDeclaredIdentifierMapper() != null ||*/ (superType != null && superType.hasIdClass()),
                persistentClass.hasIdentifierProperty(), /*persistentClass.isVersioned()*/ false, sessionFactory);
        this.jpaEntityName = persistentClass.getJpaEntityName();
    }

    @Override
    public String getName() {
        return jpaEntityName;
    }

    @Override
    public BindableType getBindableType() {
        return BindableType.ENTITY_TYPE;
    }

    @Override
    public Class<J> getBindableJavaType() {
        return getJavaType();
    }

    @Override
    public PersistenceType getPersistenceType() {
        return PersistenceType.ENTITY;
    }

    @Override
    public IdentifiableTypeDescriptor<? super J> getSuperType() {
        return super.getSuperType();
    }

 /*   @Override
    @SuppressWarnings("unchecked")
    public <S extends J> SubGraphImplementor<S> makeSubGraph(Class<S> subType) {
        if (!getBindableJavaType().isAssignableFrom(subType)) {
            throw new IllegalArgumentException(
                    String.format("Entity type [%s] cannot be treated as requested sub-type [%s]", getName(),
                            subType.getName()));
        }

        return new SubGraphImpl(this, true, sessionFactory());
    }

    @Override
    public SubGraphImplementor<J> makeSubGraph() {
        return makeSubGraph(getBindableJavaType());
    }
*/
    @Override
    public String toString() {
        return getName();
    }
}
