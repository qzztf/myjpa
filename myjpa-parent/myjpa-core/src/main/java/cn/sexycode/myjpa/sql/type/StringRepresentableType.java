package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.TypeException;

/**
 * Additional, optional contract for types capable of rendering and consuming their values to/from strings.
 *
 */
public interface StringRepresentableType<T> {
    /**
     * Render the value to the string representation.
     *
     * @param value The value to render to string.
     * @return The string representation
     * @throws cn.sexycode.myjpa.sql.type.TypeException Problem rendering
     */
    String toString(T value) throws cn.sexycode.myjpa.sql.type.TypeException;

    /**
     * Consume the given string representation back into this types java form.
     *
     * @param string The string representation to be consumed.
     * @return The java type representation
     * @throws cn.sexycode.myjpa.sql.type.TypeException Problem consuming
     */
    T fromStringValue(String string) throws TypeException;
}
