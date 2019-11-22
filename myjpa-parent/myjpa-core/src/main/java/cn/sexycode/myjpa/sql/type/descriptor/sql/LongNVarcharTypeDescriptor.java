package cn.sexycode.myjpa.sql.type.descriptor.sql;

import cn.sexycode.myjpa.sql.type.descriptor.sql.NVarcharTypeDescriptor;

import java.sql.Types;

/**
 * Descriptor for {@link Types#LONGNVARCHAR LONGNVARCHAR} handling.
 *
 * @author qzz
 */
public class LongNVarcharTypeDescriptor extends NVarcharTypeDescriptor {
    public static final LongNVarcharTypeDescriptor INSTANCE = new LongNVarcharTypeDescriptor();

    public LongNVarcharTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.LONGNVARCHAR;
    }
}
