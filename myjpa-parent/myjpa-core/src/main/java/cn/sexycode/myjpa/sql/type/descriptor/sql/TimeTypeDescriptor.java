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
 * Descriptor for {@link Types#TIME TIME} handling.
 *
 * @author qzz
 */
public class TimeTypeDescriptor implements SqlTypeDescriptor {
    public static final TimeTypeDescriptor INSTANCE = new TimeTypeDescriptor();

    public TimeTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.TIME;
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
                final Time time = javaTypeDescriptor.unwrap(value, Time.class, options);
                if (value instanceof Calendar) {
                    st.setTime(index, time, (Calendar) value);
                } else if (options.getJdbcTimeZone() != null) {
                    st.setTime(index, time, Calendar.getInstance(options.getJdbcTimeZone()));
                } else {
                    st.setTime(index, time);
                }
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                    throws SQLException {
                final Time time = javaTypeDescriptor.unwrap(value, Time.class, options);
                if (value instanceof Calendar) {
                    st.setTime(name, time, (Calendar) value);
                } else if (options.getJdbcTimeZone() != null) {
                    st.setTime(name, time, Calendar.getInstance(options.getJdbcTimeZone()));
                } else {
                    st.setTime(name, time);
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
                        javaTypeDescriptor.wrap(rs.getTime(name, Calendar.getInstance(options.getJdbcTimeZone())), options) :
                        javaTypeDescriptor.wrap(rs.getTime(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return options.getJdbcTimeZone() != null ?
                        javaTypeDescriptor.wrap(statement.getTime(index, Calendar.getInstance(options.getJdbcTimeZone())), options) :
                        javaTypeDescriptor.wrap(statement.getTime(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return options.getJdbcTimeZone() != null ?
                        javaTypeDescriptor.wrap(statement.getTime(name, Calendar.getInstance(options.getJdbcTimeZone())), options) :
                        javaTypeDescriptor.wrap(statement.getTime(name), options);
            }
        };
    }
}
