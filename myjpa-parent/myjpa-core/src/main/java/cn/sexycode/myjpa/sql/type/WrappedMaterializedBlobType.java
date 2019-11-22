package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.ByteArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.BlobTypeDescriptor;

/**
 * A type that maps JDBC {@link java.sql.Types#BLOB BLOB} and {@code Byte[]}.
 * A type that maps an SQL BLOB to Java Byte[].
 *
 */
public class WrappedMaterializedBlobType extends AbstractSingleColumnStandardBasicType<Byte[]> {
    public static final WrappedMaterializedBlobType INSTANCE = new WrappedMaterializedBlobType();

    public WrappedMaterializedBlobType() {
        super(BlobTypeDescriptor.DEFAULT, ByteArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        // todo name these annotation types for addition to the registry
        return null;
    }
}
