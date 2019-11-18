package cn.sexycode.myjpa.metamodel;

import javax.persistence.metamodel.Attribute;


/**
 * Hibernate extension to the JPA {@link Attribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface PersistentAttributeDescriptor<D, J> extends PersistentAttribute<D, J> {
	@Override
	ManagedTypeDescriptor<D> getDeclaringType();

	SimpleTypeDescriptor<?> getValueGraphType();
	SimpleTypeDescriptor<?> getKeyGraphType();
}
