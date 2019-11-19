/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package cn.sexycode.myjpa.metamodel.model.domain.internal;

import org.hibernate.metamodel.model.domain.spi.SetPersistentAttribute;

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
