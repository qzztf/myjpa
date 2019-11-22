package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.DiscriminatorType;
import cn.sexycode.myjpa.sql.type.PrimitiveType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.ShortTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SmallIntTypeDescriptor;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A type that maps between {@link java.sql.Types#SMALLINT SMALLINT} and {@link Short}
 *
 */
public class ShortType
        extends AbstractSingleColumnStandardBasicType<Short>
        implements PrimitiveType<Short>, DiscriminatorType<Short>, VersionType<Short> {

    public static final ShortType INSTANCE = new ShortType();

    private static final Short ZERO = (short) 0;

    public ShortType() {
        super(SmallIntTypeDescriptor.INSTANCE, ShortTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "short";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), short.class.getName(), Short.class.getName()};
    }

    @Override
    public Serializable getDefaultValue() {
        return ZERO;
    }

    @Override
    public Class getPrimitiveClass() {
        return short.class;
    }

    @Override
    public String objectToSQLString(Short value, Dialect dialect) throws Exception {
        return value.toString();
    }

    @Override
    public Short stringToObject(String xml) throws Exception {
        return Short.valueOf(xml);
    }

    @Override
    public Comparator<Short> getComparator() {
        return getJavaTypeDescriptor().getComparator();
    }

}
