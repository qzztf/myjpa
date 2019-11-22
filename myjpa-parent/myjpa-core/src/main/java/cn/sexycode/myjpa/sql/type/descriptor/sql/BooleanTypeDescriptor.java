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
 * Descriptor for {@link Types#BOOLEAN BOOLEAN} handling.
 *
 * @author qzz
 */
public class BooleanTypeDescriptor implements SqlTypeDescriptor {
    public static final BooleanTypeDescriptor INSTANCE = new BooleanTypeDescriptor();

    public BooleanTypeDescriptor() {
    }

    public int getSqlType() {
        return Types.BOOLEAN;
    }

    @Override
    public boolean canBeRemapped() {
        return true;
    }

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
