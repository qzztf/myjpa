
package cn.sexycode.myjpa.util;

import cn.sexycode.myjpa.metamodel.model.domain.spi.*;

/**
 * Helper containing utilities useful for graph handling
 *
 * @author Steve Ebersole
 */
public class GraphHelper {
	@SuppressWarnings("unchecked")
	public static <J> SimpleTypeDescriptor<J> resolveKeyTypeDescriptor(SingularPersistentAttribute attribute) {
		// only valid for entity-valued attributes where the entity has a
		// composite id
		final SimpleTypeDescriptor attributeType = attribute.getType();
		if ( attributeType instanceof IdentifiableTypeDescriptor) {
			return ( (IdentifiableTypeDescriptor) attributeType ).getIdType();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <J> SimpleTypeDescriptor<J> resolveKeyTypeDescriptor(PluralPersistentAttribute attribute) {
		if ( attribute instanceof SingularPersistentAttribute ) {
			// only valid for entity-valued attributes where the entity has a
			// composite id
			final SimpleTypeDescriptor attributeType = ( (SingularPersistentAttribute) attribute ).getType();
			if ( attributeType instanceof IdentifiableTypeDescriptor ) {
				return ( (IdentifiableTypeDescriptor) attributeType ).getIdType();
			}

			return null;
		}
		else if ( attribute instanceof PluralPersistentAttribute ) {
			if ( attribute instanceof MapPersistentAttribute) {
				return ( (MapPersistentAttribute) attribute ).getKeyType();
			}

			return null;
		}

		throw new IllegalArgumentException(
				"Unexpected Attribute Class [" + attribute.getClass().getName()
						+ "] - expecting SingularAttributeImplementor or PluralAttributeImplementor"
		);
	}
}
