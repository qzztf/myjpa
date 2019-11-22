package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.Type;

import java.io.Serializable;

/**
 * Additional contract for primitive / primitive wrapper types.
 *
 */
public interface PrimitiveType<T> extends LiteralType<T> {
    /**
     * Retrieve the primitive counterpart to the wrapper type identified by
     * {@link Type#getReturnedClass()}.
     *
     * @return The primitive Java type.
     */
    Class getPrimitiveClass();

    /**
     * Retrieve the string representation of the given value.
     *
     * @param value The value to be stringified.
     * @return The string representation
     */
    String toString(T value);

    /**
     * Get this type's default value.
     *
     * @return The default value.
     */
    Serializable getDefaultValue();
}
