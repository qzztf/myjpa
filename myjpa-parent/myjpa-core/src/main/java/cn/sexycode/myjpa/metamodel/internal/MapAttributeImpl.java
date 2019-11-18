package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.metamodel.MapPersistentAttribute;
import cn.sexycode.myjpa.metamodel.SimpleTypeDescriptor;

import java.util.Map;


/**
 * @author Steve Ebersole
 */
class MapAttributeImpl<X, K, V> extends AbstractPluralAttribute<X, Map<K, V>, V>
		implements MapPersistentAttribute<X, K, V> {
	private final SimpleTypeDescriptor<K> keyType;

	MapAttributeImpl(PluralAttributeBuilder<X, Map<K, V>, V, K> xceBuilder) {
		super( xceBuilder );
		this.keyType = xceBuilder.getKeyType();
	}

	@Override
	public CollectionType getCollectionType() {
		return CollectionType.MAP;
	}

	@Override
	public Class<K> getKeyJavaType() {
		return keyType.getJavaType();
	}

	@Override
	public SimpleTypeDescriptor<K> getKeyType() {
		return keyType;
	}

	@Override
	public SimpleTypeDescriptor<K> getKeyGraphType() {
		return getKeyType();
	}
}
