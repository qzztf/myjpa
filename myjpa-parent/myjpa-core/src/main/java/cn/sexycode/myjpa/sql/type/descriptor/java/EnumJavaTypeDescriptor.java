package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ImmutableMutabilityPlan;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptorRegistry;

/**
 * Describes a Java Enum type.
 *
 * @author qzz
 */
public class EnumJavaTypeDescriptor<T extends Enum> extends AbstractTypeDescriptor<T> {
    @SuppressWarnings("unchecked")
    protected EnumJavaTypeDescriptor(Class<T> type) {
        super(type, ImmutableMutabilityPlan.INSTANCE);

        JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(this);
    }

    @Override
    public String toString(T value) {
        return value == null ? "<null>" : value.name();
    }

    @Override
    public T fromString(String string) {
        return string == null ? null : (T) Enum.valueOf(getJavaTypeClass(), string);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
        return (X) value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> T wrap(X value, WrapperOptions options) {
        return (T) value;
    }
}
