package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.CharacterArrayClobType;
import cn.sexycode.myjpa.sql.type.descriptor.java.PrimitiveCharacterArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NClobTypeDescriptor;

/**
 * Map a char[] to a NClob
 *
 */
public class PrimitiveCharacterArrayNClobType extends AbstractSingleColumnStandardBasicType<char[]> {
    public static final cn.sexycode.myjpa.sql.type.CharacterArrayClobType INSTANCE = new CharacterArrayClobType();

    public PrimitiveCharacterArrayNClobType() {
        super(NClobTypeDescriptor.DEFAULT, PrimitiveCharacterArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        // todo name these annotation types for addition to the registry
        return null;
    }
}
