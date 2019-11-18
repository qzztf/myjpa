 package cn.sexycode.myjpa.metamodel;

import java.util.Set;
import javax.persistence.metamodel.SetAttribute;

/**
 * Hibernate extension to the JPA {@link SetAttribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface SetPersistentAttribute<D,E> extends SetAttribute<D,E>, PluralPersistentAttribute<D,Set<E>,E> {
}
