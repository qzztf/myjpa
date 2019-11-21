package cn.sexycode.myjpa.property.access.spi;

import cn.sexycode.myjpa.EntityMode;
import cn.sexycode.util.core.service.Service;

/**
 * Contract for resolving the PropertyAccessStrategy to use.
 * <p/>
 * todo : moving forward I'd prefer this not be a service, but instead a strategy on the MetadataBuildingContext or MetadataBuildingOptions
 *
 * @author Steve Ebersole
 */
public interface PropertyAccessStrategyResolver extends Service {
	/**
	 * Resolve the PropertyAccessStrategy to use
	 *
	 * @param containerClass The java class of the entity
	 * @param explicitAccessStrategyName The access strategy name explicitly specified, if any.
	 * @param entityMode The entity mode in effect for the property, used to interpret different default strategies.
	 *
	 * @return The resolved PropertyAccessStrategy
	 */
	PropertyAccessStrategy resolvePropertyAccessStrategy(Class containerClass, String explicitAccessStrategyName,
            EntityMode entityMode);
}
