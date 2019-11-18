package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.metamodel.IdentifiableTypeDescriptor;
import cn.sexycode.myjpa.metamodel.MappedSuperclassTypeDescriptor;
import cn.sexycode.myjpa.metamodel.internal.AbstractIdentifiableType;
import cn.sexycode.myjpa.session.SessionFactory;

import javax.persistence.MappedSuperclass;

/**
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 */
public class MappedSuperclassTypeImpl<X> extends AbstractIdentifiableType<X> implements
        MappedSuperclassTypeDescriptor<X> {
    public MappedSuperclassTypeImpl(
            Class<X> javaType,
            MappedSuperclass mappedSuperclass,
            IdentifiableTypeDescriptor<? super X> superType,
            SessionFactory sessionFactory) {
        super(
                javaType,
                javaType.getName(),
                superType,
                mappedSuperclass.getDeclaredIdentifierMapper() != null || ( superType != null && superType.hasIdClass() ),
                mappedSuperclass.hasIdentifierProperty(),
                mappedSuperclass.isVersioned(),
                sessionFactory
        );
    }

    @Override
    public PersistenceType getPersistenceType() {
        return PersistenceType.MAPPED_SUPERCLASS;
    }

    /*@Override
    public <S extends X> SubGraphImplementor<S> makeSubGraph(Class<S> subType) {
        throw new NotYetImplementedException(  );
    }*/
}
