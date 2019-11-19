package cn.sexycode.myjpa.metamodel.model.domain.spi;

import cn.sexycode.myjpa.metamodel.model.domain.PersistentAttribute;

import javax.persistence.metamodel.Attribute;

/**
 * Hibernate extension to the JPA {@link Attribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface PersistentAttributeDescriptor<D, J> extends PersistentAttribute<D, J> {
    @Override
    ManagedTypeDescriptor<D> getDeclaringType();

    @Override
    SimpleTypeDescriptor<?> getValueGraphType();

    @Override
    SimpleTypeDescriptor<?> getKeyGraphType();
}
