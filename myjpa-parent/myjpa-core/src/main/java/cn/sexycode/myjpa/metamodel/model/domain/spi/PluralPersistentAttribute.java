package cn.sexycode.myjpa.metamodel.model.domain.spi;

import javax.persistence.metamodel.PluralAttribute;

/**
 * Hibernate extension to the JPA {@link PluralAttribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface PluralPersistentAttribute<D, C, E>
        extends PluralAttribute<D, C, E>, PersistentAttributeDescriptor<D, C> {
    @Override
    ManagedTypeDescriptor<D> getDeclaringType();

    @Override
    SimpleTypeDescriptor<E> getElementType();

    @Override
    SimpleTypeDescriptor<E> getValueGraphType();
}
