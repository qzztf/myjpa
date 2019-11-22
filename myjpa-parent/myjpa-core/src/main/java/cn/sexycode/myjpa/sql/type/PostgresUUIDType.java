package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.ValueBinder;
import cn.sexycode.myjpa.sql.type.descriptor.ValueExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.UUIDTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.*;
import java.util.UUID;

/**
 * Specialized type mapping for {@link UUID} and the Postgres UUID data type (which is mapped as OTHER in its
 * JDBC driver).
 *
 */
public class PostgresUUIDType extends AbstractSingleColumnStandardBasicType<UUID> {
    public static final PostgresUUIDType INSTANCE = new PostgresUUIDType();

    public PostgresUUIDType() {
        super(PostgresUUIDSqlTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "pg-uuid";
    }

    @Override
    protected boolean registerUnderJavaType() {
        // register this type under UUID when it is added to the basic type registry
        return true;
    }

    public static class PostgresUUIDSqlTypeDescriptor implements SqlTypeDescriptor {
        public static final PostgresUUIDSqlTypeDescriptor INSTANCE = new PostgresUUIDSqlTypeDescriptor();

        @Override
        public int getSqlType() {
            // ugh
            return Types.OTHER;
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
                    st.setObject(index, javaTypeDescriptor.unwrap(value, UUID.class, options), getSqlType());
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    st.setObject(name, javaTypeDescriptor.unwrap(value, UUID.class, options), getSqlType());
                }
            };
        }

        @Override
        public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new BasicExtractor<X>(javaTypeDescriptor, this) {
                @Override
                protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(rs.getObject(name), options);
                }

                @Override
                protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(statement.getObject(index), options);
                }

                @Override
                protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(statement.getObject(name), options);
                }
            };
        }
    }
}
