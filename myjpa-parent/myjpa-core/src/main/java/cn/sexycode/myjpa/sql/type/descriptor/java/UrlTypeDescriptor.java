package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Descriptor for {@link URL} handling.
 *
 * @author qzz
 */
public class UrlTypeDescriptor extends AbstractTypeDescriptor<URL> {
    public static final UrlTypeDescriptor INSTANCE = new UrlTypeDescriptor();

    public UrlTypeDescriptor() {
        super(URL.class);
    }

    public String toString(URL value) {
        return value.toExternalForm();
    }

    public URL fromString(String string) {
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
            throw new TypeException("Unable to convert string [" + string + "] to URL : " + e);
        }
    }

    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(URL value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (String.class.isAssignableFrom(type)) {
            return (X) toString(value);
        }
        throw unknownUnwrap(type);
    }

    public <X> URL wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (String.class.isInstance(value)) {
            return fromString((String) value);
        }
        throw unknownWrap(value.getClass());
    }
}
