package cn.sexycode.myjpa.sql.type.descriptor.java;

import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.util.core.object.ReflectHelper;

/**
 * Descriptor for {@link Class} handling.
 *
 * @author qzz
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
            throw new TypeException("Unable to locate named class " + string);
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
