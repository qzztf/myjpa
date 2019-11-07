package cn.sexycode.myjpa.query;

import javax.persistence.TemporalType;

import cn.sexycode.sql.type.Type;

/**
 * The value/type binding information for a particular query parameter.
 *
 * @author Steve Ebersole
 */
public interface QueryParameterBinding<T> {
	boolean isBound();

	/**
	 * Sets the parameter binding value.  The inherent parameter type (if known) is assumed
	 *
	 * @param value The bind value
	 */
	void setBindValue(T value);

	/**
	 * Sets the parameter binding value using the explicit Type.
	 *
	 * @param value The bind value
	 * @param clarifiedType The explicit Type to use
	 */
	void setBindValue(T value, Type clarifiedType);

	/**
	 * Sets the parameter binding value using the explicit TemporalType.
	 *
	 * @param value The bind value
	 * @param clarifiedTemporalType The temporal type to use
	 */
	void setBindValue(T value, TemporalType clarifiedTemporalType);

	/**
	 * Get the value current bound.
	 *
	 * @return The currently bound value
	 */
	T getBindValue();

	/**
	 * Get the Type currently associated with this binding.
	 *
	 * @return The currently associated Type
	 */
	Type getBindType();
}
