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
 * Descriptor for {@link Types#DATE DATE} handling.
 *
 * @author qzz
 */
public class DateTypeDescriptor implements SqlTypeDescriptor {
    public static final DateTypeDescriptor INSTANCE = new DateTypeDescriptor();

    public DateTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.DATE;
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
                final Date date = javaTypeDescriptor.unwrap(value, Date.class, options);
                if (value instanceof Calendar) {
                    st.setDate(index, date, (Calendar) value);
                } else {
                    st.setDate(index, date);
                }
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                    throws SQLException {
                final Date date = javaTypeDescriptor.unwrap(value, Date.class, options);
                if (value instanceof Calendar) {
                    st.setDate(name, date, (Calendar) value);
                } else {
                    st.setDate(name, date);
                }
            }
        };
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>(javaTypeDescriptor, this) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(rs.getDate(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getDate(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getDate(name), options);
            }
        };
    }
}
