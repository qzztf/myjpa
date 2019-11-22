package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.StringTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.LongNVarcharTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#LONGNVARCHAR LONGNVARCHAR} and {@link String}
 *
 */
public class NTextType extends AbstractSingleColumnStandardBasicType<String> {
    public static final NTextType INSTANCE = new NTextType();

    public NTextType() {
        super(LongNVarcharTypeDescriptor.INSTANCE, StringTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "ntext";
    }

}
