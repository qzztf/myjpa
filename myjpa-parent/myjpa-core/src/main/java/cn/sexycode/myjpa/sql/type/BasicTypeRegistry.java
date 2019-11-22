package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.*;
import cn.sexycode.myjpa.sql.type.BasicType;
import cn.sexycode.myjpa.sql.type.BigDecimalType;
import cn.sexycode.myjpa.sql.type.BigIntegerType;
import cn.sexycode.myjpa.sql.type.BinaryType;
import cn.sexycode.myjpa.sql.type.BlobType;
import cn.sexycode.myjpa.sql.type.BooleanType;
import cn.sexycode.myjpa.sql.type.ByteType;
import cn.sexycode.myjpa.sql.type.CalendarDateType;
import cn.sexycode.myjpa.sql.type.CalendarType;
import cn.sexycode.myjpa.sql.type.CharArrayType;
import cn.sexycode.myjpa.sql.type.CharacterType;
import cn.sexycode.myjpa.sql.type.ClassType;
import cn.sexycode.myjpa.sql.type.ClobType;
import cn.sexycode.myjpa.sql.type.CurrencyType;
import cn.sexycode.myjpa.sql.type.CustomType;
import cn.sexycode.myjpa.sql.type.DateType;
import cn.sexycode.myjpa.sql.type.DbTimestampType;
import cn.sexycode.myjpa.sql.type.DoubleType;
import cn.sexycode.myjpa.sql.type.DurationType;
import cn.sexycode.myjpa.sql.type.FloatType;
import cn.sexycode.myjpa.sql.type.ImageType;
import cn.sexycode.myjpa.sql.type.InstantType;
import cn.sexycode.myjpa.sql.type.IntegerType;
import cn.sexycode.myjpa.sql.type.LocalDateType;
import cn.sexycode.myjpa.sql.type.LocalTimeType;
import cn.sexycode.myjpa.sql.type.LocaleType;
import cn.sexycode.myjpa.sql.type.LongType;
import cn.sexycode.myjpa.sql.type.NClobType;
import cn.sexycode.myjpa.sql.type.NTextType;
import cn.sexycode.myjpa.sql.type.ObjectType;
import cn.sexycode.myjpa.sql.type.OffsetTimeType;
import cn.sexycode.myjpa.sql.type.RowVersionType;
import cn.sexycode.myjpa.sql.type.SerializableType;
import cn.sexycode.myjpa.sql.type.ShortType;
import cn.sexycode.myjpa.sql.type.StringType;
import cn.sexycode.myjpa.sql.type.TextType;
import cn.sexycode.myjpa.sql.type.TimeType;
import cn.sexycode.myjpa.sql.type.TimeZoneType;
import cn.sexycode.myjpa.sql.type.TimestampType;
import cn.sexycode.myjpa.sql.type.TrueFalseType;
import cn.sexycode.myjpa.sql.type.Type;
import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.UUIDBinaryType;
import cn.sexycode.myjpa.sql.type.UUIDCharType;
import cn.sexycode.myjpa.sql.type.UrlType;
import cn.sexycode.myjpa.sql.type.UserType;
import cn.sexycode.myjpa.sql.type.YesNoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A registry of {@link cn.sexycode.myjpa.sql.type.BasicType} instances
 *
 */
public class BasicTypeRegistry implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(BasicTypeRegistry.class);

    // TODO : analyze these sizing params; unfortunately this seems to be the only way to give a "concurrencyLevel"
    private Map<String, cn.sexycode.myjpa.sql.type.BasicType> registry = new ConcurrentHashMap<String, cn.sexycode.myjpa.sql.type.BasicType>(100, .75f, 1);
    private boolean locked;

    public BasicTypeRegistry() {
        register(BooleanType.INSTANCE);
        register(NumericBooleanType.INSTANCE);
        register(TrueFalseType.INSTANCE);
        register(YesNoType.INSTANCE);

        register(ByteType.INSTANCE);
        register(CharacterType.INSTANCE);
        register(ShortType.INSTANCE);
        register(IntegerType.INSTANCE);
        register(LongType.INSTANCE);
        register(FloatType.INSTANCE);
        register(DoubleType.INSTANCE);
        register(BigDecimalType.INSTANCE);
        register(BigIntegerType.INSTANCE);

        register(StringType.INSTANCE);
        register(StringNVarcharType.INSTANCE);
        register(CharacterNCharType.INSTANCE);
        register(UrlType.INSTANCE);

        register(DurationType.INSTANCE);
        register(InstantType.INSTANCE);
        register(LocalDateTimeType.INSTANCE);
        register(LocalDateType.INSTANCE);
        register(LocalTimeType.INSTANCE);
        register(OffsetDateTimeType.INSTANCE);
        register(OffsetTimeType.INSTANCE);
        register(ZonedDateTimeType.INSTANCE);

        register(cn.sexycode.myjpa.sql.type.DateType.INSTANCE);
        register(cn.sexycode.myjpa.sql.type.TimeType.INSTANCE);
        register(cn.sexycode.myjpa.sql.type.TimestampType.INSTANCE);
        register(cn.sexycode.myjpa.sql.type.DbTimestampType.INSTANCE);
        register(cn.sexycode.myjpa.sql.type.CalendarType.INSTANCE);
        register(cn.sexycode.myjpa.sql.type.CalendarDateType.INSTANCE);

        register(LocaleType.INSTANCE);
        register(CurrencyType.INSTANCE);
        register(TimeZoneType.INSTANCE);
        register(ClassType.INSTANCE);
        register(UUIDBinaryType.INSTANCE);
        register(UUIDCharType.INSTANCE);

        register(cn.sexycode.myjpa.sql.type.BinaryType.INSTANCE);
        register(WrapperBinaryType.INSTANCE);
        register(RowVersionType.INSTANCE);
        register(ImageType.INSTANCE);
        register(CharArrayType.INSTANCE);
        register(CharacterArrayType.INSTANCE);
        register(TextType.INSTANCE);
        register(NTextType.INSTANCE);
        register(BlobType.INSTANCE);
        register(MaterializedBlobType.INSTANCE);
        register(ClobType.INSTANCE);
        register(NClobType.INSTANCE);
        register(MaterializedClobType.INSTANCE);
        register(MaterializedNClobType.INSTANCE);
        register(cn.sexycode.myjpa.sql.type.SerializableType.INSTANCE);

        register(ObjectType.INSTANCE);

        //noinspection unchecked
        register(new AdaptedImmutableType(DateType.INSTANCE));
        //noinspection unchecked
        register(new AdaptedImmutableType(TimeType.INSTANCE));
        //noinspection unchecked
        register(new AdaptedImmutableType(TimestampType.INSTANCE));
        //noinspection unchecked
        register(new AdaptedImmutableType(DbTimestampType.INSTANCE));
        //noinspection unchecked
        register(new AdaptedImmutableType(CalendarType.INSTANCE));
        //noinspection unchecked
        register(new AdaptedImmutableType(CalendarDateType.INSTANCE));
        //noinspection unchecked
        register(new AdaptedImmutableType(BinaryType.INSTANCE));
        //noinspection unchecked
        register(new AdaptedImmutableType(SerializableType.INSTANCE));
    }

    /**
     * Constructor version used during shallow copy
     *
     * @param registeredTypes The type map to copy over
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private BasicTypeRegistry(Map<String, cn.sexycode.myjpa.sql.type.BasicType> registeredTypes) {
        registry.putAll(registeredTypes);
        locked = true;
    }

    public void register(cn.sexycode.myjpa.sql.type.BasicType type) {
        register(type, type.getRegistrationKeys());
    }

    public void register(cn.sexycode.myjpa.sql.type.BasicType type, String[] keys) {
        if (locked) {
            throw new cn.sexycode.myjpa.sql.type.TypeException("Can not alter TypeRegistry at this time");
        }

        if (type == null) {
            throw new TypeException("Type to register cannot be null");
        }

        if (keys == null || keys.length == 0) {
            return;
        }

        for (String key : keys) {
            // be safe...
            if (key == null) {
                continue;
            }
            LOG.debug("Adding type registration [{}] -> [{}]", key, type);
            final Type old = registry.put(key, type);
            if (old != null && old != type) {
            }
        }
    }

    public void register(UserType type, String[] keys) {
        register(new CustomType(type, keys));
    }

    public void register(CompositeUserType type, String[] keys) {
        register(new CompositeCustomType(type, keys));
    }

    public BasicType getRegisteredType(String key) {
        return registry.get(key);
    }

    public BasicTypeRegistry shallowCopy() {
        return new BasicTypeRegistry(this.registry);
    }
}
