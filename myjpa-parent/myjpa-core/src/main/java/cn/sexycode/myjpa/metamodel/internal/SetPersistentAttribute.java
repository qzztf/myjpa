package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.SetAttribute;
import java.util.Set;

public interface SetPersistentAttribute<D,E> extends SetAttribute<D,E>, PluralPersistentAttribute<D, Set<E>,E> {
}