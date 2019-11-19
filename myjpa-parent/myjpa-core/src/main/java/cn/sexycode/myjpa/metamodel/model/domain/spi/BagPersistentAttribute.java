package cn.sexycode.myjpa.metamodel.model.domain.spi;

import javax.persistence.metamodel.CollectionAttribute;
import java.util.Collection;

/**
 * Hibernate extension to the JPA {@link CollectionAttribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface BagPersistentAttribute<D, E>
        extends CollectionAttribute<D, E>, PluralPersistentAttribute<D, Collection<E>, E> {
    @Override
    SimpleTypeDescriptor<E> getValueGraphType();

    @Override
    SimpleTypeDescriptor<E> getElementType();

    @Override
    ManagedTypeDescriptor<D> getDeclaringType();
}
