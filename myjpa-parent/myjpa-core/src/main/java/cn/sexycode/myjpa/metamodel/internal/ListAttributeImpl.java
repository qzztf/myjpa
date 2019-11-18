package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.metamodel.ListPersistentAttribute;

import java.util.List;


/**
 * @author Steve Ebersole
 */
class ListAttributeImpl<X, E> extends AbstractPluralAttribute<X, List<E>, E> implements ListPersistentAttribute<X, E> {
	ListAttributeImpl(PluralAttributeBuilder<X, List<E>, E, ?> xceBuilder) {
		super( xceBuilder );
	}

	@Override
	public CollectionType getCollectionType() {
		return CollectionType.LIST;
	}
}
