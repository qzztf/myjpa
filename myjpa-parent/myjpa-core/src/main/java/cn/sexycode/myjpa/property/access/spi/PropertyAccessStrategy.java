package cn.sexycode.myjpa.property.access.spi;

/**
 * Describes a strategy for persistent property access (field, JavaBean-style property, etc).
 * Acts as a factory for {@link PropertyAccess} instances.
 *
 * @author Steve Ebersole
 */
public interface PropertyAccessStrategy {
	/**
	 * Build a PropertyAccess for the indicated property
	 *
	 * @param containerJavaType The Java type that contains the property; may be {@code null} for non-pojo cases.
	 * @param propertyName The property name
	 *
	 * @return The appropriate PropertyAccess
	 */
	PropertyAccess buildPropertyAccess(Class containerJavaType, String propertyName);
}
