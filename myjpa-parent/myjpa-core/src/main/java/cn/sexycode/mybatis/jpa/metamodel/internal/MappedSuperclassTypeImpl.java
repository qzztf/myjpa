/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.metamodel.internal;


import javax.persistence.MappedSuperclass;
import javax.persistence.metamodel.MappedSuperclassType;

/**
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 */
public class MappedSuperclassTypeImpl<X> extends AbstractIdentifiableType<X> implements MappedSuperclassType<X> {
    public MappedSuperclassTypeImpl(
            Class<X> javaType,
            MappedSuperclass mappedSuperclass,
            AbstractIdentifiableType<? super X> superType) {
        super(
                javaType,
                javaType.getName(),
                superType,
                (superType != null && superType.hasIdClass()),
                true,
                true
        );
    }

    @Override
    public PersistenceType getPersistenceType() {
        return PersistenceType.MAPPED_SUPERCLASS;
    }
}
