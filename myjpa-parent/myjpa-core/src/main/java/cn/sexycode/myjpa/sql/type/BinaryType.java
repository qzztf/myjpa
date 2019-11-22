package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.RowVersionType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.PrimitiveByteArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarbinaryTypeDescriptor;

import java.util.Comparator;

/**
 * A type that maps between a {@link java.sql.Types#VARBINARY VARBINARY} and {@code byte[]}
 * <p>
 * Implementation of the {@link VersionType} interface should be considered deprecated.
 * For binary entity versions/timestamps, {@link RowVersionType} should be used instead.
 *
 */
public class BinaryType
        extends AbstractSingleColumnStandardBasicType<byte[]>
        implements VersionType<byte[]> {

    public static final BinaryType INSTANCE = new BinaryType();

    @Override
    public String getName() {
        return "binary";
    }

    public BinaryType() {
        super(VarbinaryTypeDescriptor.INSTANCE, PrimitiveByteArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), "byte[]", byte[].class.getName()};
    }


    /**
     * Get a comparator for version values.
     *
     * @return The comparator to use to compare different version values.
     * @deprecated use {@link RowVersionType} for binary entity versions/timestamps
     */
    @Override
    @Deprecated
    public Comparator<byte[]> getComparator() {
        return PrimitiveByteArrayTypeDescriptor.INSTANCE.getComparator();
    }
}
