package cn.sexycode.myjpa.sql.type.descriptor.sql;

import cn.sexycode.myjpa.sql.type.descriptor.sql.VarcharTypeDescriptor;

import java.sql.Types;

/**
 * Descriptor for {@link Types#CHAR CHAR} handling.
 *
 * @author qzz
 */
public class CharTypeDescriptor extends VarcharTypeDescriptor {
    public static final CharTypeDescriptor INSTANCE = new CharTypeDescriptor();

    public CharTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.CHAR;
    }
}
