package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.*;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.BigDecimalTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.BigIntegerTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.BlobTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.BooleanTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ByteArrayTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ByteTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.CalendarTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.CharacterTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ClassTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ClobTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.CurrencyTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.DateTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.DoubleTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.DurationJavaDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.EnumJavaTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.FloatTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ImmutableMutabilityPlan;
import cn.sexycode.myjpa.sql.type.descriptor.java.InstantJavaDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.IntegerTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.JdbcDateTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.JdbcTimeTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.LocalDateJavaDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.LocaleTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.LongTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.MutabilityPlan;
import cn.sexycode.myjpa.sql.type.descriptor.java.MutableMutabilityPlan;
import cn.sexycode.myjpa.sql.type.descriptor.java.NClobTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.OffsetTimeJavaDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ShortTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.StringTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.TimeZoneTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.UUIDTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.UrlTypeDescriptor;
import cn.sexycode.util.core.object.ReflectHelper;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basically a map from {@link Class} -> {@link cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor}
 *
 * @author qzz
 */
public class JavaTypeDescriptorRegistry {
    private static final Logger log = LoggerFactory.getLogger(JavaTypeDescriptorRegistry.class);

    public static final JavaTypeDescriptorRegistry INSTANCE = new JavaTypeDescriptorRegistry();

    private ConcurrentHashMap<Class, cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor> descriptorsByClass = new ConcurrentHashMap<>();

    public JavaTypeDescriptorRegistry() {
        addDescriptorInternal(ByteTypeDescriptor.INSTANCE);
        addDescriptorInternal(BooleanTypeDescriptor.INSTANCE);
        addDescriptorInternal(CharacterTypeDescriptor.INSTANCE);
        addDescriptorInternal(ShortTypeDescriptor.INSTANCE);
        addDescriptorInternal(IntegerTypeDescriptor.INSTANCE);
        addDescriptorInternal(LongTypeDescriptor.INSTANCE);
        addDescriptorInternal(FloatTypeDescriptor.INSTANCE);
        addDescriptorInternal(DoubleTypeDescriptor.INSTANCE);
        addDescriptorInternal(BigDecimalTypeDescriptor.INSTANCE);
        addDescriptorInternal(BigIntegerTypeDescriptor.INSTANCE);

        addDescriptorInternal(StringTypeDescriptor.INSTANCE);

        addDescriptorInternal(BlobTypeDescriptor.INSTANCE);
        addDescriptorInternal(ClobTypeDescriptor.INSTANCE);
        addDescriptorInternal(NClobTypeDescriptor.INSTANCE);

        addDescriptorInternal(ByteArrayTypeDescriptor.INSTANCE);
        addDescriptorInternal(CharacterArrayTypeDescriptor.INSTANCE);
        addDescriptorInternal(PrimitiveByteArrayTypeDescriptor.INSTANCE);
        addDescriptorInternal(PrimitiveCharacterArrayTypeDescriptor.INSTANCE);

        addDescriptorInternal(DurationJavaDescriptor.INSTANCE);
        addDescriptorInternal(InstantJavaDescriptor.INSTANCE);
        addDescriptorInternal(LocalDateJavaDescriptor.INSTANCE);
        addDescriptorInternal(LocalDateTimeJavaDescriptor.INSTANCE);
        addDescriptorInternal(OffsetDateTimeJavaDescriptor.INSTANCE);
        addDescriptorInternal(OffsetTimeJavaDescriptor.INSTANCE);
        addDescriptorInternal(ZonedDateTimeJavaDescriptor.INSTANCE);

        addDescriptorInternal(CalendarTypeDescriptor.INSTANCE);
        addDescriptorInternal(DateTypeDescriptor.INSTANCE);
        descriptorsByClass.put(java.sql.Date.class, JdbcDateTypeDescriptor.INSTANCE);
        descriptorsByClass.put(java.sql.Time.class, JdbcTimeTypeDescriptor.INSTANCE);
        descriptorsByClass.put(java.sql.Timestamp.class, JdbcTimestampTypeDescriptor.INSTANCE);
        addDescriptorInternal(TimeZoneTypeDescriptor.INSTANCE);

        addDescriptorInternal(ClassTypeDescriptor.INSTANCE);

        addDescriptorInternal(CurrencyTypeDescriptor.INSTANCE);
        addDescriptorInternal(LocaleTypeDescriptor.INSTANCE);
        addDescriptorInternal(UrlTypeDescriptor.INSTANCE);
        addDescriptorInternal(UUIDTypeDescriptor.INSTANCE);
    }

    private cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor addDescriptorInternal(
            cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor descriptor) {
        return descriptorsByClass.put(descriptor.getJavaTypeClass(), descriptor);
    }

    /**
     * Adds the given descriptor to this registry
     *
     * @param descriptor The descriptor to add.
     */
    public void addDescriptor(cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor descriptor) {
        cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor old = addDescriptorInternal(descriptor);
        if (old != null) {
            log.debug(
                    "JavaTypeDescriptorRegistry entry replaced : %s -> %s (was %s)",
                    descriptor.getJavaTypeClass(),
                    descriptor,
                    old
            );
        }
    }

    @SuppressWarnings("unchecked")
    public <T> cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor<T> getDescriptor(Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class passed to locate Java type descriptor cannot be null");
        }

        cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor<T> descriptor = descriptorsByClass.get(cls);
        if (descriptor != null) {
            return descriptor;
        }

        if (cls.isEnum()) {
            descriptor = new EnumJavaTypeDescriptor(cls);
            descriptorsByClass.put(cls, descriptor);
            return descriptor;
        }

        // find the first "assignable" match
        for (Map.Entry<Class, JavaTypeDescriptor> entry : descriptorsByClass.entrySet()) {
            if (entry.getKey().isAssignableFrom(cls)) {
                log.debug("Using  cached JavaTypeDescriptor instance for Java class [%s]", cls.getName());
                return entry.getValue();
            }
        }

        if (Serializable.class.isAssignableFrom(cls)) {
            return new SerializableTypeDescriptor(cls);
        }

        log.debug(
                "Could not find matching JavaTypeDescriptor for requested Java class [%s]; using fallback.  " +
                        "This means Hibernate does not know how to perform certain basic operations in relation to this Java type." +
                        "",
                cls.getName()
        );
        checkEqualsAndHashCode(cls);

        return new FallbackJavaTypeDescriptor<T>(cls);
    }

    @SuppressWarnings("unchecked")
    private void checkEqualsAndHashCode(Class javaType) {
        if (!ReflectHelper.overridesEquals(javaType) || !ReflectHelper.overridesHashCode(javaType)) {
//            log.unknownJavaTypeNoEqualsHashCode(javaType);
        }
    }


    public static class FallbackJavaTypeDescriptor<T> extends AbstractTypeDescriptor<T> {
        protected FallbackJavaTypeDescriptor(final Class<T> type) {
            super(type, createMutabilityPlan(type));
        }

        @SuppressWarnings("unchecked")
        private static <T> MutabilityPlan<T> createMutabilityPlan(final Class<T> type) {
            if (type.isAnnotationPresent(Immutable.class)) {
                return ImmutableMutabilityPlan.INSTANCE;
            }
            // MutableMutabilityPlan is the "safest" option, but we do not necessarily know how to deepCopy etc...
            return new MutableMutabilityPlan<T>() {
                @Override
                protected T deepCopyNotNull(T value) {
                    throw new TypeException(
                            "Not known how to deep copy value of type: [" + type
                                    .getName() + "]"
                    );
                }
            };
        }

        @Override
        public String toString(T value) {
            return value == null ? "<null>" : value.toString();
        }

        @Override
        public T fromString(String string) {
            throw new TypeException(
                    "Not known how to convert String to given type [" + getJavaTypeClass().getName() + "]"
            );
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

}
