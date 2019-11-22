package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.RowVersionTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarbinaryTypeDescriptor;

import java.util.Comparator;

/**
 * A type that maps between a {@link java.sql.Types#VARBINARY VARBINARY} and {@code byte[]}
 * specifically for entity versions/timestamps.
 *
 */
public class RowVersionType
        extends AbstractSingleColumnStandardBasicType<byte[]>
        implements VersionType<byte[]> {

    public static final RowVersionType INSTANCE = new RowVersionType();

    @Override
    public String getName() {
        return "row_version";
    }

    public RowVersionType() {
        super(VarbinaryTypeDescriptor.INSTANCE, RowVersionTypeDescriptor.INSTANCE);
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName()};
    }

    @Override
    public Comparator<byte[]> getComparator() {
        return getJavaTypeDescriptor().getComparator();
    }
}
