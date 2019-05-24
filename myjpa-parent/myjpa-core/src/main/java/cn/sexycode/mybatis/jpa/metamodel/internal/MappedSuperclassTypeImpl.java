package cn.sexycode.mybatis.jpa.metamodel.internal;


import javax.persistence.MappedSuperclass;
import javax.persistence.metamodel.MappedSuperclassType;

/**
 *
 *
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
