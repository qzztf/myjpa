/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


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
 * @author Gavin King
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
     * @throws TypeException
     */
    Object getPropertyValue(Object component, int property) throws TypeException;

    /**
     * Set the value of a property.
     *
     * @param component an instance of class mapped by this "type"
     * @param property
     * @param value     the value to set
     * @throws TypeException
     */
    void setPropertyValue(Object component, int property, Object value) throws TypeException;

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
     * @throws TypeException
     */
    boolean equals(Object x, Object y) throws TypeException;

    /**
     * Get a hashcode for the instance, consistent with persistence "equality"
     */
    int hashCode(Object x) throws TypeException;

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset. Implementors
     * should handle possibility of null values.
     *
     * @param rs      a JDBC result set
     * @param names   the column names
     * @param session
     * @param owner   the containing entity
     * @return Object
     * @throws TypeException
     * @throws SQLException
     */
    Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws TypeException, SQLException;

    /**
     * Write an instance of the mapped class to a prepared statement. Implementors
     * should handle possibility of null values. A multi-column type should be written
     * to parameters starting from <tt>index</tt>.
     *
     * @param st      a JDBC prepared statement
     * @param value   the object to write
     * @param index   statement parameter index
     * @param session
     * @throws TypeException
     * @throws SQLException
     */
    void nullSafeSet(PreparedStatement st, Object value, int index) throws TypeException, SQLException;

    /**
     * Return a deep copy of the persistent state, stopping at entities and at collections.
     *
     * @param value generally a collection element or entity field
     * @return Object a copy
     * @throws TypeException
     */
    Object deepCopy(Object value) throws TypeException;

    /**
     * Check if objects of this type mutable.
     *
     * @return boolean
     */
    boolean isMutable();

}
