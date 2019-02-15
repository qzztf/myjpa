/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type.descriptor.java;


import cn.sexycode.sql.CharacterStream;
import cn.sexycode.sql.CharacterStreamImpl;
import cn.sexycode.sql.type.descriptor.WrapperOptions;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;

/**
 * Descriptor for {@link String} handling.
 *
 * @author Steve Ebersole
 */
public class StringTypeDescriptor extends AbstractTypeDescriptor<String> {
    public static final StringTypeDescriptor INSTANCE = new StringTypeDescriptor();

    public StringTypeDescriptor() {
        super(String.class);
    }

    public String toString(String value) {
        return value;
    }

    public String fromString(String string) {
        return string;
    }

    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(String value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (String.class.isAssignableFrom(type)) {
            return (X) value;
        }
        if (Reader.class.isAssignableFrom(type)) {
            return (X) new StringReader(value);
        }
        if (CharacterStream.class.isAssignableFrom(type)) {
            return (X) new CharacterStreamImpl(value);
        }


        throw unknownUnwrap(type);
    }

    public <X> String wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (String.class.isInstance(value)) {
            return (String) value;
        }
        if (Reader.class.isInstance(value)) {
            return DataHelper.extractString((Reader) value);
        }
        if (Clob.class.isInstance(value)) {
            return DataHelper.extractString((Clob) value);
        }

        throw unknownWrap(value.getClass());
    }
}
