package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.session.SessionFactory;


/**
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 */
public class MappedSuperclassTypeImpl<X> extends AbstractIdentifiableType<X> implements MappedSuperclassTypeDescriptor<X> {
    public MappedSuperclassTypeImpl(
            Class<X> javaType,
            MappedSuperclass mappedSuperclass,
            IdentifiableTypeDescriptor<? super X> superType,
            SessionFactory sessionFactory) {
        super(
                javaType,
                javaType.getName(),
                superType,
                false,
                mappedSuperclass.hasIdentifierProperty(),
                mappedSuperclass.isVersioned(),
                sessionFactory
        );
    }

    @Override
    public PersistenceType getPersistenceType() {
        return PersistenceType.MAPPED_SUPERCLASS;
    }

}