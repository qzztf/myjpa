package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.StringTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.ClobTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#CLOB CLOB} and {@link String}
 *
 */
public class MaterializedClobType extends AbstractSingleColumnStandardBasicType<String> {
    public static final MaterializedClobType INSTANCE = new MaterializedClobType();

    public MaterializedClobType() {
        super(ClobTypeDescriptor.DEFAULT, StringTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "materialized_clob";
    }
}
