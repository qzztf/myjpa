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
 * Descriptor for {@link Types#BIT BIT} handling.
 * <p/>
 * Note that JDBC is very specific about its use of the type BIT to mean a single binary digit, whereas
 * SQL defines BIT having a parameterized length.
 *
 * @author qzz
 */
public class BitTypeDescriptor implements SqlTypeDescriptor {
    public static final BitTypeDescriptor INSTANCE = new BitTypeDescriptor();

    public BitTypeDescriptor() {
    }

    public int getSqlType() {
        return Types.BIT;
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
                st.setBoolean(index, javaTypeDescriptor.unwrap(value, Boolean.class, options));
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                    throws SQLException {
                st.setBoolean(name, javaTypeDescriptor.unwrap(value, Boolean.class, options));
            }
        };
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>(javaTypeDescriptor, this) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(rs.getBoolean(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getBoolean(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getBoolean(name), options);
            }
        };
    }
}
