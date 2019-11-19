package cn.sexycode.myjpa.metamodel.model.domain.internal;

import cn.sexycode.myjpa.metamodel.model.domain.spi.BagPersistentAttribute;

import java.util.Collection;

/**
 * @author Steve Ebersole
 */
class BagAttributeImpl<X, E> extends AbstractPluralAttribute<X, Collection<E>, E>
        implements BagPersistentAttribute<X, E> {
    BagAttributeImpl(PluralAttributeBuilder<X, Collection<E>, E, ?> xceBuilder) {
        super(xceBuilder);
    }

    @Override
    public CollectionType getCollectionType() {
        return CollectionType.COLLECTION;
    }
}
