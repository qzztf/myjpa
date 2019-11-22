package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.StringTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.LongVarcharTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#LONGVARCHAR LONGVARCHAR} and {@link String}
 *
 * @author qzz,
 */
public class TextType extends AbstractSingleColumnStandardBasicType<String> {
    public static final TextType INSTANCE = new TextType();

    public TextType() {
        super(LongVarcharTypeDescriptor.INSTANCE, StringTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "text";
    }

}
