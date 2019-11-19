package cn.sexycode.myjpa.metamodel.model.domain.spi;

import javax.persistence.metamodel.ListAttribute;
import java.util.List;

/**
 * Hibernate extension to the JPA {@link ListAttribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface ListPersistentAttribute<D, E> extends ListAttribute<D, E>, PluralPersistentAttribute<D, List<E>, E> {
}
