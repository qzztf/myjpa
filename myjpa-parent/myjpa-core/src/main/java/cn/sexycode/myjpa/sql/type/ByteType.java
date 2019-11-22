package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.DiscriminatorType;
import cn.sexycode.myjpa.sql.type.PrimitiveType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.ByteTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TinyIntTypeDescriptor;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A type that maps between {@link java.sql.Types#TINYINT TINYINT} and {@link Byte}
 *
 * @author qzz
 * @author qzz
 */
public class ByteType
        extends AbstractSingleColumnStandardBasicType<Byte>
        implements PrimitiveType<Byte>, DiscriminatorType<Byte>, VersionType<Byte> {

    public static final ByteType INSTANCE = new ByteType();

    private static final Byte ZERO = (byte) 0;

    public ByteType() {
        super(TinyIntTypeDescriptor.INSTANCE, ByteTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "byte";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), byte.class.getName(), Byte.class.getName()};
    }

    @Override
    public Serializable getDefaultValue() {
        return ZERO;
    }

    @Override
    public Class getPrimitiveClass() {
        return byte.class;
    }

    @Override
    public String objectToSQLString(Byte value, Dialect dialect) {
        return toString(value);
    }

    @Override
    public Byte stringToObject(String xml) {
        return fromString(xml);
    }

    @Override
    public Byte fromStringValue(String xml) {
        return fromString(xml);
    }

    @Override
    public Comparator<Byte> getComparator() {
        return getJavaTypeDescriptor().getComparator();
    }
}
