/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type.descriptor.java;

import cn.sexycode.mybatis.jpa.binding.MyJpaException;
import cn.sexycode.sql.type.descriptor.WrapperOptions;
import cn.sexycode.sql.util.ReflectHelper;

/**
 * Descriptor for {@link Class} handling.
 *
 * @author Steve Ebersole
 */
public class ClassTypeDescriptor extends AbstractTypeDescriptor<Class> {
    public static final ClassTypeDescriptor INSTANCE = new ClassTypeDescriptor();

    public ClassTypeDescriptor() {
        super(Class.class);
    }

    @Override
    public String toString(Class value) {
        return value.getName();
    }

    @Override
    public Class fromString(String string) {
        if (string == null) {
            return null;
        }

        try {
            return ReflectHelper.classForName(string);
        } catch (ClassNotFoundException e) {
            throw new MyJpaException("Unable to locate named class " + string);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(Class value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Class.class.isAssignableFrom(type)) {
            return (X) value;
        }
        if (String.class.isAssignableFrom(type)) {
            return (X) toString(value);
        }
        throw unknownUnwrap(type);
    }

    @Override
    public <X> Class wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Class.class.isInstance(value)) {
            return (Class) value;
        }
        if (String.class.isInstance(value)) {
            return fromString((String) value);
        }
        throw unknownWrap(value.getClass());
    }
}
