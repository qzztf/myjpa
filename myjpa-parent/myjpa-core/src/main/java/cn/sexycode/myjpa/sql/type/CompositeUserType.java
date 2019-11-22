package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.Type;
import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.UserType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A <tt>UserType</tt> that may be dereferenced in a query.
 * This interface allows a custom type to define "properties".
 * These need not necessarily correspond to physical JavaBeans
 * style properties.<br>
 * <br>
 * A <tt>CompositeUserType</tt> may be used in almost every way
 * that a component may be used. It may even contain many-to-one
 * associations.<br>
 * <br>
 * Implementors must be immutable and must declare a public
 * default constructor.<br>
 * <br>
 * Unlike <tt>UserType</tt>, cacheability does not depend upon
 * serializability. Instead, <tt>assemble()</tt> and
 * <tt>disassemble</tt> provide conversion to/from a cacheable
 * representation.
 *
 * @see UserType for more simple cases
 * @see org.hibernate.type.Type
 */
public interface CompositeUserType {

    /**
     * Get the "property names" that may be used in a
     * query.
     *
     * @return an array of "property names"
     */
    String[] getPropertyNames();

    /**
     * Get the corresponding "property types".
     *
     * @return an array of Hibernate types
     */
    Type[] getPropertyTypes();

    /**
     * Get the value of a property.
     *
     * @param component an instance of class mapped by this "type"
     * @param property
     * @return the property value
     * @throws cn.sexycode.myjpa.sql.type.TypeException
     */
    Object getPropertyValue(Object component, int property) throws cn.sexycode.myjpa.sql.type.TypeException;

    /**
     * Set the value of a property.
     *
     * @param component an instance of class mapped by this "type"
     * @param property
     * @param value     the value to set
     * @throws cn.sexycode.myjpa.sql.type.TypeException
     */
    void setPropertyValue(Object component, int property, Object value) throws cn.sexycode.myjpa.sql.type.TypeException;

    /**
     * The class returned by <tt>nullSafeGet()</tt>.
     *
     * @return Class
     */
    Class returnedClass();

    /**
     * Compare two instances of the class mapped by this type for persistence "equality".
     * Equality of the persistent state.
     *
     * @throws cn.sexycode.myjpa.sql.type.TypeException
     */
    boolean equals(Object x, Object y) throws cn.sexycode.myjpa.sql.type.TypeException;

    /**
     * Get a hashcode for the instance, consistent with persistence "equality"
     */
    int hashCode(Object x) throws cn.sexycode.myjpa.sql.type.TypeException;

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset. Implementors
     * should handle possibility of null values.
     *
     * @param rs      a JDBC result set
     * @param names   the column names
     * @param session
     * @param owner   the containing entity
     * @return Object
     * @throws cn.sexycode.myjpa.sql.type.TypeException
     * @throws SQLException
     */
    Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws cn.sexycode.myjpa.sql.type.TypeException, SQLException;

    /**
     * Write an instance of the mapped class to a prepared statement. Implementors
     * should handle possibility of null values. A multi-column type should be written
     * to parameters starting from <tt>index</tt>.
     *
     * @param st      a JDBC prepared statement
     * @param value   the object to write
     * @param index   statement parameter index
     * @param session
     * @throws cn.sexycode.myjpa.sql.type.TypeException
     * @throws SQLException
     */
    void nullSafeSet(PreparedStatement st, Object value, int index) throws cn.sexycode.myjpa.sql.type.TypeException, SQLException;

    /**
     * Return a deep copy of the persistent state, stopping at entities and at collections.
     *
     * @param value generally a collection element or entity field
     * @return Object a copy
     * @throws cn.sexycode.myjpa.sql.type.TypeException
     */
    Object deepCopy(Object value) throws TypeException;

    /**
     * Check if objects of this type mutable.
     *
     * @return boolean
     */
    boolean isMutable();

}
