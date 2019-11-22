package cn.sexycode.myjpa.sql.type.descriptor.sql;


import cn.sexycode.myjpa.sql.type.descriptor.ValueBinder;
import cn.sexycode.myjpa.sql.type.descriptor.ValueExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.*;

/**
 * Descriptor for {@link Types#DOUBLE DOUBLE} handling.
 *
 * @author qzz
 */
public class DoubleTypeDescriptor implements SqlTypeDescriptor {
    public static final DoubleTypeDescriptor INSTANCE = new DoubleTypeDescriptor();

    public DoubleTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.DOUBLE;
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
                st.setDouble(index, javaTypeDescriptor.unwrap(value, Double.class, options));
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                    throws SQLException {
                st.setDouble(name, javaTypeDescriptor.unwrap(value, Double.class, options));
            }
        };
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>(javaTypeDescriptor, this) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(rs.getDouble(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getDouble(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getDouble(name), options);
            }
        };
    }
}
