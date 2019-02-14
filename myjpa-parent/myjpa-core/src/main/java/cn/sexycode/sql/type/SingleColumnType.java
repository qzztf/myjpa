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
 * Provide convenient methods for binding and extracting values for use with {@link BasicType}.
 *
 * @author Steve Ebersole
 */
public interface SingleColumnType<T> extends Type {

    int sqlType();

    String toString(T value) throws TypeException;

    T fromStringValue(String xml) throws TypeException;

    /**
     * Get a column value from a result set by name.
     *
     * @param rs      The result set from which to extract the value.
     * @param name    The name of the value to extract.
     * @param session The session from which the request originates
     * @return The extracted value.
     * @throws org.hibernate.TypeException Generally some form of mismatch error.
     * @throws SQLException                Indicates problem making the JDBC call(s).
     */
    T nullSafeGet(ResultSet rs, String name) throws SQLException;

    /**
     * Get a column value from a result set, without worrying about the possibility of null values.
     *
     * @param rs      The result set from which to extract the value.
     * @param name    The name of the value to extract.
     * @param session The session from which the request originates
     * @return The extracted value.
     * @throws org.hibernate.TypeException Generally some form of mismatch error.
     * @throws SQLException                Indicates problem making the JDBC call(s).
     */
    Object get(ResultSet rs, String name) throws TypeException, SQLException;

    /**
     * Set a parameter value without worrying about the possibility of null
     * values.  Called from {@link #nullSafeSet} afterQuery nullness checks have
     * been performed.
     *
     * @param st      The statement into which to bind the parameter value.
     * @param value   The parameter value to bind.
     * @param index   The position or index at which to bind the param value.
     * @param session The session from which the request originates
     * @throws org.hibernate.TypeException Generally some form of mismatch error.
     * @throws SQLException                Indicates problem making the JDBC call(s).
     */
    void set(PreparedStatement st, T value, int index) throws TypeException, SQLException;
}
