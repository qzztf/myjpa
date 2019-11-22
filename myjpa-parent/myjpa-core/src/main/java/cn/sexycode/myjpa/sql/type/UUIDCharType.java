package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.StringType;
import cn.sexycode.myjpa.sql.type.descriptor.java.UUIDTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.UUID;

/**
 * A type mapping {@link java.sql.Types#CHAR} (or {@link java.sql.Types#VARCHAR}) and {@link UUID}
 *
 * @author qzz
 */
public class UUIDCharType extends AbstractSingleColumnStandardBasicType<UUID> implements LiteralType<UUID> {
    public static final UUIDCharType INSTANCE = new UUIDCharType();

    public UUIDCharType() {
        super(VarcharTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "uuid-char";
    }

    public String objectToSQLString(UUID value, Dialect dialect) throws Exception {
        return StringType.INSTANCE.objectToSQLString(value.toString(), dialect);
    }
}
