package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.DiscriminatorType;
import cn.sexycode.myjpa.sql.type.PrimitiveType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.LongTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BigIntTypeDescriptor;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A type that maps between {@link java.sql.Types#BIGINT BIGINT} and {@link Long}
 *
 * @author qzz
 * @author qzz
 */
public class LongType
        extends AbstractSingleColumnStandardBasicType<Long>
        implements PrimitiveType<Long>, DiscriminatorType<Long>, VersionType<Long> {

    public static final LongType INSTANCE = new LongType();

    private static final Long ZERO = (long) 0;

    public LongType() {
        super(BigIntTypeDescriptor.INSTANCE, LongTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "long";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), long.class.getName(), Long.class.getName()};
    }

    @Override
    public Serializable getDefaultValue() {
        return ZERO;
    }

    @Override
    public Class getPrimitiveClass() {
        return long.class;
    }

    @Override
    public Long stringToObject(String xml) throws Exception {
        return Long.valueOf(xml);
    }


    @Override
    public Comparator<Long> getComparator() {
        return getJavaTypeDescriptor().getComparator();
    }

    @Override
    public String objectToSQLString(Long value, Dialect dialect) throws Exception {
        return value.toString();
    }
}
