package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.MaterializedClobType;
import cn.sexycode.myjpa.sql.type.descriptor.java.CharacterArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.ClobTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#CLOB CLOB} and {@link Character Character[]}
 * <p/>
 * Essentially a {@link MaterializedClobType} but represented as a Character[] in Java rather than String.
 *
 */
public class CharacterArrayClobType extends AbstractSingleColumnStandardBasicType<Character[]> {
    public static final CharacterArrayClobType INSTANCE = new CharacterArrayClobType();

    public CharacterArrayClobType() {
        super(ClobTypeDescriptor.DEFAULT, CharacterArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        // todo name these annotation types for addition to the registry
        return null;
    }

}
