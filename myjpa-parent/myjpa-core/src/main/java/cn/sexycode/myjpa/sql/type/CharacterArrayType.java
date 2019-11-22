package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.CharacterArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarcharTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#VARCHAR VARCHAR} and {@link Character Character[]}
 *
 */
public class CharacterArrayType extends AbstractSingleColumnStandardBasicType<Character[]> {
    public static final CharacterArrayType INSTANCE = new CharacterArrayType();

    public CharacterArrayType() {
        super(VarcharTypeDescriptor.INSTANCE, CharacterArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "wrapper-characters";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), Character[].class.getName(), "Character[]"};
    }
}
