package cn.sexycode.myjpa.sql.type.descriptor;

import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contract for extracting value via JDBC (from {@link ResultSet} or as output param from {@link CallableStatement}).
 *
 */
public interface ValueExtractor<X> {
    /**
     * Extract value from result set
     *
     * @param rs      The result set from which to extract the value
     * @param name    The name by which to extract the value from the result set
     * @param options The options
     * @return The extracted value
     * @throws SQLException Indicates a JDBC error occurred.
     */
    public X extract(ResultSet rs, String name, WrapperOptions options) throws SQLException;

    public X extract(CallableStatement statement, int index, WrapperOptions options) throws SQLException;

    public X extract(CallableStatement statement, String[] paramNames, WrapperOptions options) throws SQLException;
}
