package cn.sexycode.myjpa.sql.type.descriptor.sql;


import cn.sexycode.myjpa.sql.type.descriptor.JdbcTypeNameMapper;
import cn.sexycode.myjpa.sql.type.descriptor.ValueBinder;
import cn.sexycode.myjpa.sql.type.descriptor.ValueExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.*;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicBinder;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BasicExtractor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BigIntTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BinaryTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BitTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BlobTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BooleanTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.CharTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.ClobTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.DateTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.DecimalTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.DoubleTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.FloatTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.IntegerTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.JdbcTypeFamilyInformation;
import cn.sexycode.myjpa.sql.type.descriptor.sql.LongVarcharTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NCharTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NClobTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NVarcharTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NumericTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.RealTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SmallIntTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TimeTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TimestampTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TinyIntTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarbinaryTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarcharTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basically a map from JDBC type code (int) -> {@link cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor}
 *
 * @author qzz
 */
public class SqlTypeDescriptorRegistry {
    public static final SqlTypeDescriptorRegistry INSTANCE = new SqlTypeDescriptorRegistry();

    private static final Logger log = LoggerFactory.getLogger(SqlTypeDescriptorRegistry.class);

    private ConcurrentHashMap<Integer, cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor> descriptorMap = new ConcurrentHashMap<Integer, cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor>();

    private SqlTypeDescriptorRegistry() {
        addDescriptor(BooleanTypeDescriptor.INSTANCE);

        addDescriptor(BitTypeDescriptor.INSTANCE);
        addDescriptor(BigIntTypeDescriptor.INSTANCE);
        addDescriptor(DecimalTypeDescriptor.INSTANCE);
        addDescriptor(DoubleTypeDescriptor.INSTANCE);
        addDescriptor(FloatTypeDescriptor.INSTANCE);
        addDescriptor(IntegerTypeDescriptor.INSTANCE);
        addDescriptor(NumericTypeDescriptor.INSTANCE);
        addDescriptor(RealTypeDescriptor.INSTANCE);
        addDescriptor(SmallIntTypeDescriptor.INSTANCE);
        addDescriptor(TinyIntTypeDescriptor.INSTANCE);

        addDescriptor(DateTypeDescriptor.INSTANCE);
        addDescriptor(TimestampTypeDescriptor.INSTANCE);
        addDescriptor(TimeTypeDescriptor.INSTANCE);

        addDescriptor(BinaryTypeDescriptor.INSTANCE);
        addDescriptor(cn.sexycode.myjpa.sql.type.descriptor.sql.VarbinaryTypeDescriptor.INSTANCE);
        addDescriptor(LongVarbinaryTypeDescriptor.INSTANCE);
        addDescriptor(BlobTypeDescriptor.DEFAULT);

        addDescriptor(CharTypeDescriptor.INSTANCE);
        addDescriptor(VarcharTypeDescriptor.INSTANCE);
        addDescriptor(LongVarcharTypeDescriptor.INSTANCE);
        addDescriptor(ClobTypeDescriptor.DEFAULT);

        addDescriptor(NCharTypeDescriptor.INSTANCE);
        addDescriptor(NVarcharTypeDescriptor.INSTANCE);
        addDescriptor(LongNVarcharTypeDescriptor.INSTANCE);
        addDescriptor(NClobTypeDescriptor.DEFAULT);
    }

    public void addDescriptor(cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor sqlTypeDescriptor) {
        descriptorMap.put(sqlTypeDescriptor.getSqlType(), sqlTypeDescriptor);
    }

    public cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor getDescriptor(int jdbcTypeCode) {
        cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor descriptor = descriptorMap.get(Integer.valueOf(jdbcTypeCode));
        if (descriptor != null) {
            return descriptor;
        }

        if (JdbcTypeNameMapper.isStandardTypeCode(jdbcTypeCode)) {
            log.debug(
                    "A standard JDBC type code [%s] was not defined in SqlTypeDescriptorRegistry",
                    jdbcTypeCode
            );
        }

        // see if the typecode is part of a known type family...
        cn.sexycode.myjpa.sql.type.descriptor.sql.JdbcTypeFamilyInformation.Family family = JdbcTypeFamilyInformation.INSTANCE.locateJdbcTypeFamilyByTypeCode(jdbcTypeCode);
        if (family != null) {
            for (int potentialAlternateTypeCode : family.getTypeCodes()) {
                if (potentialAlternateTypeCode != jdbcTypeCode) {
                    final cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor potentialAlternateDescriptor = descriptorMap.get(Integer.valueOf(potentialAlternateTypeCode));
                    if (potentialAlternateDescriptor != null) {
                        // todo : add a SqlTypeDescriptor.canBeAssignedFrom method...
                        return potentialAlternateDescriptor;
                    }

                    if (JdbcTypeNameMapper.isStandardTypeCode(potentialAlternateTypeCode)) {
                        log.debug(
                                "A standard JDBC type code [%s] was not defined in SqlTypeDescriptorRegistry",
                                potentialAlternateTypeCode
                        );
                    }
                }
            }
        }

        // finally, create a new descriptor mapping to getObject/setObject for this type code...
        final ObjectSqlTypeDescriptor fallBackDescriptor = new ObjectSqlTypeDescriptor(jdbcTypeCode);
        addDescriptor(fallBackDescriptor);
        return fallBackDescriptor;
    }

    public static class ObjectSqlTypeDescriptor implements SqlTypeDescriptor {
        private final int jdbcTypeCode;

        public ObjectSqlTypeDescriptor(int jdbcTypeCode) {
            this.jdbcTypeCode = jdbcTypeCode;
        }

        @Override
        public int getSqlType() {
            return jdbcTypeCode;
        }

        @Override
        public boolean canBeRemapped() {
            return true;
        }

        @Override
        public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {
            if (Serializable.class.isAssignableFrom(javaTypeDescriptor.getJavaTypeClass())) {
                return cn.sexycode.myjpa.sql.type.descriptor.sql.VarbinaryTypeDescriptor.INSTANCE.getBinder(javaTypeDescriptor);
            }

            return new BasicBinder<X>(javaTypeDescriptor, this) {
                @Override
                protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                        throws SQLException {
                    st.setObject(index, value, jdbcTypeCode);
                }

                @Override
                protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                        throws SQLException {
                    st.setObject(name, value, jdbcTypeCode);
                }
            };
        }

        @Override
        @SuppressWarnings("unchecked")
        public ValueExtractor getExtractor(JavaTypeDescriptor javaTypeDescriptor) {
            if (Serializable.class.isAssignableFrom(javaTypeDescriptor.getJavaTypeClass())) {
                return VarbinaryTypeDescriptor.INSTANCE.getExtractor(javaTypeDescriptor);
            }

            return new BasicExtractor(javaTypeDescriptor, this) {
                @Override
                protected Object doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                    return rs.getObject(name);
                }

                @Override
                protected Object doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                    return statement.getObject(index);
                }

                @Override
                protected Object doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                    return statement.getObject(name);
                }
            };
        }
    }
}
