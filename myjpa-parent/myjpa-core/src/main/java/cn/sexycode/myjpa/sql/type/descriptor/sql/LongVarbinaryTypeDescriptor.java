package cn.sexycode.myjpa.sql.type.descriptor.sql;

import cn.sexycode.myjpa.sql.type.descriptor.sql.VarbinaryTypeDescriptor;

import java.sql.Types;

/**
 * Descriptor for {@link Types#LONGVARBINARY LONGVARBINARY} handling.
 *
 * @author qzz
 */
public class LongVarbinaryTypeDescriptor extends VarbinaryTypeDescriptor {
    public static final LongVarbinaryTypeDescriptor INSTANCE = new LongVarbinaryTypeDescriptor();

    public LongVarbinaryTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.LONGVARBINARY;
    }
}
