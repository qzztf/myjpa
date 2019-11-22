package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ArrayMutabilityPlan;
import cn.sexycode.myjpa.sql.type.descriptor.java.DataHelper;
import cn.sexycode.myjpa.sql.type.descriptor.java.IncomparableComparator;
import cn.sexycode.myjpa.sql.util.CharacterStream;
import cn.sexycode.myjpa.sql.util.CharacterStreamImpl;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Descriptor for {@code char[]} handling.
 *
 * @author qzz
 */
public class PrimitiveCharacterArrayTypeDescriptor extends AbstractTypeDescriptor<char[]> {
    public static final PrimitiveCharacterArrayTypeDescriptor INSTANCE = new PrimitiveCharacterArrayTypeDescriptor();

    @SuppressWarnings({"unchecked"})
    protected PrimitiveCharacterArrayTypeDescriptor() {
        super(char[].class, ArrayMutabilityPlan.INSTANCE);
    }

    public String toString(char[] value) {
        return new String(value);
    }

    public char[] fromString(String string) {
        return string.toCharArray();
    }

    @Override
    public boolean areEqual(char[] one, char[] another) {
        return one == another
                || (one != null && another != null && Arrays.equals(one, another));
    }

    @Override
    public int extractHashCode(char[] chars) {
        int hashCode = 1;
        for (char aChar : chars) {
            hashCode = 31 * hashCode + aChar;
        }
        return hashCode;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Comparator<char[]> getComparator() {
        return IncomparableComparator.INSTANCE;
    }

    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(char[] value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (char[].class.isAssignableFrom(type)) {
            return (X) value;
        }
        if (String.class.isAssignableFrom(type)) {
            return (X) new String(value);
        }

        if (Reader.class.isAssignableFrom(type)) {
            return (X) new StringReader(new String(value));
        }
        if (CharacterStream.class.isAssignableFrom(type)) {
            return (X) new CharacterStreamImpl(new String(value));
        }
        throw unknownUnwrap(type);
    }

    public <X> char[] wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (char[].class.isInstance(value)) {
            return (char[]) value;
        }
        if (String.class.isInstance(value)) {
            return ((String) value).toCharArray();
        }
        if (Clob.class.isInstance(value)) {
            return cn.sexycode.myjpa.sql.type.descriptor.java.DataHelper.extractString(((Clob) value)).toCharArray();
        }
        if (Reader.class.isInstance(value)) {
            return DataHelper.extractString(((Reader) value)).toCharArray();
        }
        throw unknownWrap(value.getClass());
    }
}
