package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.Attribute;

public interface PersistentAttribute<D,J> extends Attribute<D,J> {
	@Override
	ManagedDomainType<D> getDeclaringType();

	SimpleDomainType<?> getValueGraphType();
	SimpleDomainType<?> getKeyGraphType();
}