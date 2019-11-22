package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.StringTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NClobTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#CLOB CLOB} and {@link String}
 *
 */
public class MaterializedNClobType extends AbstractSingleColumnStandardBasicType<String> {
    public static final MaterializedNClobType INSTANCE = new MaterializedNClobType();

    public MaterializedNClobType() {
        super(NClobTypeDescriptor.DEFAULT, StringTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "materialized_nclob";
    }
}
