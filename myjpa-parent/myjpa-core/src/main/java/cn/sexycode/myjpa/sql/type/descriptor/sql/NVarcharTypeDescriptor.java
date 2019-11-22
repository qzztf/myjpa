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
 * Descriptor for {@link Types#NVARCHAR NVARCHAR} handling.
 *
 * @author qzz
 */
public class NVarcharTypeDescriptor implements SqlTypeDescriptor {
    public static final NVarcharTypeDescriptor INSTANCE = new NVarcharTypeDescriptor();

    public NVarcharTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.NVARCHAR;
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
                st.setNString(index, javaTypeDescriptor.unwrap(value, String.class, options));
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                    throws SQLException {
                st.setNString(name, javaTypeDescriptor.unwrap(value, String.class, options));
            }
        };
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>(javaTypeDescriptor, this) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(rs.getNString(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getNString(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getNString(name), options);
            }
        };
    }
}
