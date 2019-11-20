/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package cn.sexycode.myjpa.metamodel.model.domain.internal;

import cn.sexycode.myjpa.metamodel.model.domain.spi.EmbeddedTypeDescriptor;
import cn.sexycode.myjpa.metamodel.model.domain.spi.ManagedTypeDescriptor;
import cn.sexycode.myjpa.session.SessionFactory;

import java.io.Serializable;

/**
 * Standard Hibernate implementation of JPA's {@link javax.persistence.metamodel.EmbeddableType}
 * contract
 *
 * @author Emmanuel Bernard
 * @author Steve Ebersole`
 */
public class EmbeddableTypeImpl<J> extends AbstractManagedType<J> implements EmbeddedTypeDescriptor<J>, Serializable {

    private final ManagedTypeDescriptor<?> parent;

    //    private final ComponentType hibernateType;

    public EmbeddableTypeImpl(Class<J> javaType, ManagedTypeDescriptor<?> parent/*, ComponentType hibernateType*/,
            SessionFactory sessionFactory) {
        super(javaType, null, null, sessionFactory);
        this.parent = parent;
        //        this.hibernateType = hibernateType;
    }

    @Override
    public PersistenceType getPersistenceType() {
        return PersistenceType.EMBEDDABLE;
    }

    public ManagedTypeDescriptor<?> getParent() {
        return parent;
    }

    /*public ComponentType getHibernateType() {
        return hibernateType;
    }*/

    /*@Override
    @SuppressWarnings("unchecked")
    public <S extends J> SubGraphImplementor<S> makeSubGraph(Class<S> subType) {
        return new SubGraphImpl(this, true, sessionFactory());
    }*/
}
