 package cn.sexycode.myjpa.metamodel;

import java.util.Map;
import javax.persistence.metamodel.MapAttribute;

/**
 * Hibernate extension to the JPA {@link MapAttribute} descriptor
 *
 * @author Steve Ebersole
 */
public interface MapPersistentAttribute<D,K,V> extends MapAttribute<D, K, V>, PluralPersistentAttribute<D,Map<K,V>,V> {
	@Override
	SimpleTypeDescriptor<K> getKeyType();
}
