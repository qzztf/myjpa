package cn.sexycode.myjpa.metamodel.internal;

public interface PersistentAttributeDescriptor<D, J> extends PersistentAttribute<D, J> {
	@Override
	ManagedTypeDescriptor<D> getDeclaringType();

	SimpleTypeDescriptor<?> getValueGraphType();
	SimpleTypeDescriptor<?> getKeyGraphType();
}