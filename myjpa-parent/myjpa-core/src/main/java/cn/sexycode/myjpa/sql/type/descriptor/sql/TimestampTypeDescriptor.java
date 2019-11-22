package cn.sexycode.myjpa.sql.type.descriptor.sql;


import cn.sexycode.myjpa.sql.type.descriptor.ValueBinder;
import cn.sexycode.myjpa.sql.type.descriptor.ValueExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.*;
import java.util.Calendar;

/**
 * Descriptor for {@link Types#TIMESTAMP TIMESTAMP} handling.
 *
 * @author qzz
 */
public class TimestampTypeDescriptor implements SqlTypeDescriptor {
    public static final TimestampTypeDescriptor INSTANCE = new TimestampTypeDescriptor();

    public TimestampTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.TIMESTAMP;
    }

    @Override
    public boolean canBeRemapped() {
        return true;
    }

    @Override
    public <X> ValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicBinder<X>(javaTypeDescriptor, this) {
            @Override
            protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
                final Timestamp timestamp = javaTypeDescriptor.unwrap(value, Timestamp.class, options);
                if (value instanceof Calendar) {
                    st.setTimestamp(index, timestamp, (Calendar) value);
                } else if (options.getJdbcTimeZone() != null) {
                    st.setTimestamp(index, timestamp, Calendar.getInstance(options.getJdbcTimeZone()));
                } else {
                    st.setTimestamp(index, timestamp);
                }
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                    throws SQLException {
                final Timestamp timestamp = javaTypeDescriptor.unwrap(value, Timestamp.class, options);
                if (value instanceof Calendar) {
                    st.setTimestamp(name, timestamp, (Calendar) value);
                } else if (options.getJdbcTimeZone() != null) {
                    st.setTimestamp(name, timestamp, Calendar.getInstance(options.getJdbcTimeZone()));
                } else {
                    st.setTimestamp(name, timestamp);
                }
            }
        };
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>(javaTypeDescriptor, this) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return options.getJdbcTimeZone() != null ?
                        javaTypeDescriptor.wrap(rs.getTimestamp(name, Calendar.getInstance(options.getJdbcTimeZone())), options) :
                        javaTypeDescriptor.wrap(rs.getTimestamp(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return options.getJdbcTimeZone() != null ?
                        javaTypeDescriptor.wrap(statement.getTimestamp(index, Calendar.getInstance(options.getJdbcTimeZone())), options) :
                        javaTypeDescriptor.wrap(statement.getTimestamp(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return options.getJdbcTimeZone() != null ?
                        javaTypeDescriptor.wrap(statement.getTimestamp(name, Calendar.getInstance(options.getJdbcTimeZone())), options) :
                        javaTypeDescriptor.wrap(statement.getTimestamp(name), options);
            }
        };
    }
}
