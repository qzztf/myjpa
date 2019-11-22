package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.ByteArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarbinaryTypeDescriptor;

/**
 * A type mapping {@link java.sql.Types#VARBINARY VARBINARY} and {@link Byte Byte[]}
 *
 */
public class WrapperBinaryType extends AbstractSingleColumnStandardBasicType<Byte[]> {
    public static final WrapperBinaryType INSTANCE = new WrapperBinaryType();

    public WrapperBinaryType() {
        super(VarbinaryTypeDescriptor.INSTANCE, ByteArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), "Byte[]", Byte[].class.getName()};
    }

    public String getName() {
        //TODO find a decent name beforeQuery documenting
        return "wrapper-binary";
    }
}
