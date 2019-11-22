package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.MaterializedNClobType;
import cn.sexycode.myjpa.sql.type.descriptor.java.CharacterArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NClobTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#NCLOB NCLOB} and {@link Character Character[]}
 * <p/>
 * Essentially a {@link MaterializedNClobType} but represented as a Character[] in Java rather than String.
 *
 */
public class CharacterArrayNClobType extends AbstractSingleColumnStandardBasicType<Character[]> {
    public static final CharacterArrayNClobType INSTANCE = new CharacterArrayNClobType();

    public CharacterArrayNClobType() {
        super(NClobTypeDescriptor.DEFAULT, CharacterArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        // todo name these annotation types for addition to the registry
        return null;
    }

}
