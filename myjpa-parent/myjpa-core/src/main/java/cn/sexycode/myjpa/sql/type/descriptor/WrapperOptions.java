package cn.sexycode.myjpa.sql.type.descriptor;


import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Gives binding (nullSafeSet) and extracting (nullSafeGet) code access to options.
 *
 * @todo Definitely could use a better name
 */
public interface WrapperOptions {
    /**
     * Should streams be used for binding LOB values.
     *
     * @return {@code true}/{@code false}
     */
    public boolean useStreamForLobBinding();


    /**
     * Allow remapping of descriptors for dealing with sql type.
     *
     * @param sqlTypeDescriptor The known descriptor
     * @return The remapped descriptor.  May be the same as the known descriptor indicating no remapping.
     */
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor);

    /**
     * The JDBC {@link TimeZone} used when persisting Timestamp and DateTime properties into the database.
     * This setting is used when storing timestamps using the {@link java.sql.PreparedStatement#setTimestamp(int, Timestamp, Calendar)} method.
     * <p>
     * This way, the storage {@link TimeZone} can differ from the default JVM TimeZone given by {@link TimeZone#getDefault()}.
     *
     * @return JDBC {@link TimeZone}
     */
    public TimeZone getJdbcTimeZone();
}
