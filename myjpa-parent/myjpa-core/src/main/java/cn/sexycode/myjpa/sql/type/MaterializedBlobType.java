package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.PrimitiveByteArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BlobTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#BLOB BLOB} and {@code byte[]}
 *
 */
public class MaterializedBlobType extends AbstractSingleColumnStandardBasicType<byte[]> {
    public static final MaterializedBlobType INSTANCE = new MaterializedBlobType();

    public MaterializedBlobType() {
        super(BlobTypeDescriptor.DEFAULT, PrimitiveByteArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "materialized_blob";
    }
}
