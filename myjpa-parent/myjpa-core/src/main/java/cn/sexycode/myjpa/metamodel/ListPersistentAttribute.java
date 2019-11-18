 package cn.sexycode.myjpa.metamodel;

import java.util.List;
import javax.persistence.metamodel.ListAttribute;

/**
 * Hibernate extension to the JPA {@link ListAttribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface ListPersistentAttribute<D,E> extends ListAttribute<D,E>, PluralPersistentAttribute<D,List<E>,E> {
}
