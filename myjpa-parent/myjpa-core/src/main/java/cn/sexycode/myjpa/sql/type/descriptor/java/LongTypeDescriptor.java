package cn.sexycode.myjpa.sql.type.descriptor.java;

import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Descriptor for {@link Long} handling.
 *
 * @author qzz
 */
public class LongTypeDescriptor extends AbstractTypeDescriptor<Long> {
    public static final LongTypeDescriptor INSTANCE = new LongTypeDescriptor();

    public LongTypeDescriptor() {
        super(Long.class);
    }

    @Override
    public String toString(Long value) {
        return value == null ? null : value.toString();
    }

    @Override
    public Long fromString(String string) {
        return Long.valueOf(string);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <X> X unwrap(Long value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Long.class.isAssignableFrom(type)) {
            return (X) value;
        }
        if (Byte.class.isAssignableFrom(type)) {
            return (X) Byte.valueOf(value.byteValue());
        }
        if (Short.class.isAssignableFrom(type)) {
            return (X) Short.valueOf(value.shortValue());
        }
        if (Integer.class.isAssignableFrom(type)) {
            return (X) Integer.valueOf(value.intValue());
        }
        if (Double.class.isAssignableFrom(type)) {
            return (X) Double.valueOf(value.doubleValue());
        }
        if (Float.class.isAssignableFrom(type)) {
            return (X) Float.valueOf(value.floatValue());
        }
        if (BigInteger.class.isAssignableFrom(type)) {
            return (X) BigInteger.valueOf(value);
        }
        if (BigDecimal.class.isAssignableFrom(type)) {
            return (X) BigDecimal.valueOf(value);
        }
        if (String.class.isAssignableFrom(type)) {
            return (X) value.toString();
        }
        throw unknownUnwrap(type);
    }

    @Override
    public <X> Long wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Long.class.isInstance(value)) {
            return (Long) value;
        }
        if (Number.class.isInstance(value)) {
            return ((Number) value).longValue();
        } else if (String.class.isInstance(value)) {
            return Long.valueOf(((String) value));
        }
        throw unknownWrap(value.getClass());
    }
}
