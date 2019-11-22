package cn.sexycode.myjpa.sql.type.descriptor.sql;

import cn.sexycode.myjpa.sql.type.descriptor.sql.RealTypeDescriptor;

import java.sql.Types;

/**
 * Descriptor for {@link Types#FLOAT FLOAT} handling.
 *
 * @author qzz
 */
public class FloatTypeDescriptor extends RealTypeDescriptor {
    public static final FloatTypeDescriptor INSTANCE = new FloatTypeDescriptor();

    public FloatTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.FLOAT;
    }
}
