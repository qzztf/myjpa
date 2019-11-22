package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.UUIDTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BinaryTypeDescriptor;

import java.util.UUID;

/**
 * A type mapping {@link java.sql.Types#BINARY} and {@link UUID}
 *
 */
public class UUIDBinaryType extends AbstractSingleColumnStandardBasicType<UUID> {
    public static final UUIDBinaryType INSTANCE = new UUIDBinaryType();

    public UUIDBinaryType() {
        super(BinaryTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "uuid-binary";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
