package cn.sexycode.myjpa.metamodel.model.domain.spi;

import javax.persistence.metamodel.SetAttribute;
import java.util.Set;

/**
 * Hibernate extension to the JPA {@link SetAttribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface SetPersistentAttribute<D, E> extends SetAttribute<D, E>, PluralPersistentAttribute<D, Set<E>, E> {
}
