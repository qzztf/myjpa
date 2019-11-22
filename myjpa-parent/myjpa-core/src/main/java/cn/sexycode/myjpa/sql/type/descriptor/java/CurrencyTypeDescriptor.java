package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;

import java.util.Currency;

/**
 * Descriptor for {@link Currency} handling.
 *
 * @author qzz
 */
public class CurrencyTypeDescriptor extends AbstractTypeDescriptor<Currency> {
    public static final CurrencyTypeDescriptor INSTANCE = new CurrencyTypeDescriptor();

    public CurrencyTypeDescriptor() {
        super(Currency.class);
    }

    @Override
    public String toString(Currency value) {
        return value.getCurrencyCode();
    }

    @Override
    public Currency fromString(String string) {
        return Currency.getInstance(string);
    }

    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(Currency value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (String.class.isAssignableFrom(type)) {
            return (X) value.getCurrencyCode();
        }
        throw unknownUnwrap(type);
    }

    @Override
    public <X> Currency wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (String.class.isInstance(value)) {
            return Currency.getInstance((String) value);
        }
        throw unknownWrap(value.getClass());
    }
}
