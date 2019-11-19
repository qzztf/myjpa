package cn.sexycode.myjpa.metamodel.model.domain.internal;

import cn.sexycode.myjpa.metamodel.model.domain.spi.SetPersistentAttribute;

import java.util.Set;

/**
 * @author Steve Ebersole
 */
public class SetAttributeImpl<X, E> extends AbstractPluralAttribute<X, Set<E>, E>
        implements SetPersistentAttribute<X, E> {
    public SetAttributeImpl(PluralAttributeBuilder<X, Set<E>, E, ?> xceBuilder) {
        super(xceBuilder);
    }

    @Override
    public CollectionType getCollectionType() {
        return CollectionType.SET;
    }
}
