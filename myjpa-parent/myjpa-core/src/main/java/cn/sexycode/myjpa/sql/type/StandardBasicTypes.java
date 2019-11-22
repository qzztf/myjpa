package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.*;
import cn.sexycode.myjpa.sql.type.BasicTypeRegistry;
import cn.sexycode.myjpa.sql.type.BigDecimalType;
import cn.sexycode.myjpa.sql.type.BigIntegerType;
import cn.sexycode.myjpa.sql.type.BinaryType;
import cn.sexycode.myjpa.sql.type.BlobType;
import cn.sexycode.myjpa.sql.type.BooleanType;
import cn.sexycode.myjpa.sql.type.ByteType;
import cn.sexycode.myjpa.sql.type.CalendarDateType;
import cn.sexycode.myjpa.sql.type.CalendarType;
import cn.sexycode.myjpa.sql.type.CharArrayType;
import cn.sexycode.myjpa.sql.type.CharacterArrayType;
import cn.sexycode.myjpa.sql.type.CharacterType;
import cn.sexycode.myjpa.sql.type.ClassType;
import cn.sexycode.myjpa.sql.type.ClobType;
import cn.sexycode.myjpa.sql.type.CurrencyType;
import cn.sexycode.myjpa.sql.type.DateType;
import cn.sexycode.myjpa.sql.type.DoubleType;
import cn.sexycode.myjpa.sql.type.FloatType;
import cn.sexycode.myjpa.sql.type.ImageType;
import cn.sexycode.myjpa.sql.type.IntegerType;
import cn.sexycode.myjpa.sql.type.LocaleType;
import cn.sexycode.myjpa.sql.type.LongType;
import cn.sexycode.myjpa.sql.type.NClobType;
import cn.sexycode.myjpa.sql.type.NTextType;
import cn.sexycode.myjpa.sql.type.NumericBooleanType;
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
import cn.sexycode.myjpa.sql.type.UUIDBinaryType;
import cn.sexycode.myjpa.sql.type.UUIDCharType;
import cn.sexycode.myjpa.sql.type.UrlType;
import cn.sexycode.myjpa.sql.type.WrapperBinaryType;
import cn.sexycode.myjpa.sql.type.YesNoType;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;

import java.util.HashSet;
import java.util.Set;

/**
 * Centralizes access to the standard set of basic {@link Type types}.
 * <p/>
 * Type mappings can be adjusted per {@link org.hibernate.SessionFactory}.  These adjusted mappings can be accessed
 * from the {@link org.hibernate.TypeHelper} instance obtained via {@link org.hibernate.SessionFactory#getTypeHelper()}
 *
 * @see BasicTypeRegistry
 * @see org.hibernate.TypeHelper
 * @see org.hibernate.SessionFactory#getTypeHelper()
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class StandardBasicTypes {
    private StandardBasicTypes() {
    }

    private static final Set<SqlTypeDescriptor> SQL_TYPE_DESCRIPTORS = new HashSet<SqlTypeDescriptor>();

    /**
     * The standard Hibernate type for mapping {@link Boolean} to JDBC {@link java.sql.Types#BIT BIT}.
     *
     * @see cn.sexycode.myjpa.sql.type.BooleanType
     */
    public static final cn.sexycode.myjpa.sql.type.BooleanType BOOLEAN = BooleanType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Boolean} to JDBC {@link java.sql.Types#INTEGER INTEGER}.
     *
     * @see cn.sexycode.myjpa.sql.type.NumericBooleanType
     */
    public static final cn.sexycode.myjpa.sql.type.NumericBooleanType NUMERIC_BOOLEAN = NumericBooleanType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Boolean} to JDBC {@link java.sql.Types#CHAR CHAR(1)} (using 'T'/'F').
     *
     * @see cn.sexycode.myjpa.sql.type.TrueFalseType
     */
    public static final cn.sexycode.myjpa.sql.type.TrueFalseType TRUE_FALSE = TrueFalseType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Boolean} to JDBC {@link java.sql.Types#CHAR CHAR(1)} (using 'Y'/'N').
     *
     * @see cn.sexycode.myjpa.sql.type.YesNoType
     */
    public static final cn.sexycode.myjpa.sql.type.YesNoType YES_NO = YesNoType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Byte} to JDBC {@link java.sql.Types#TINYINT TINYINT}.
     */
    public static final cn.sexycode.myjpa.sql.type.ByteType BYTE = ByteType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Short} to JDBC {@link java.sql.Types#SMALLINT SMALLINT}.
     *
     * @see cn.sexycode.myjpa.sql.type.ShortType
     */
    public static final cn.sexycode.myjpa.sql.type.ShortType SHORT = ShortType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Integer} to JDBC {@link java.sql.Types#INTEGER INTEGER}.
     *
     * @see cn.sexycode.myjpa.sql.type.IntegerType
     */
    public static final cn.sexycode.myjpa.sql.type.IntegerType INTEGER = IntegerType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Long} to JDBC {@link java.sql.Types#BIGINT BIGINT}.
     *
     * @see cn.sexycode.myjpa.sql.type.LongType
     */
    public static final cn.sexycode.myjpa.sql.type.LongType LONG = LongType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Float} to JDBC {@link java.sql.Types#FLOAT FLOAT}.
     *
     * @see cn.sexycode.myjpa.sql.type.FloatType
     */
    public static final cn.sexycode.myjpa.sql.type.FloatType FLOAT = FloatType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Double} to JDBC {@link java.sql.Types#DOUBLE DOUBLE}.
     *
     * @see cn.sexycode.myjpa.sql.type.DoubleType
     */
    public static final cn.sexycode.myjpa.sql.type.DoubleType DOUBLE = DoubleType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.math.BigInteger} to JDBC {@link java.sql.Types#NUMERIC NUMERIC}.
     *
     * @see cn.sexycode.myjpa.sql.type.BigIntegerType
     */
    public static final cn.sexycode.myjpa.sql.type.BigIntegerType BIG_INTEGER = BigIntegerType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.math.BigDecimal} to JDBC {@link java.sql.Types#NUMERIC NUMERIC}.
     *
     * @see cn.sexycode.myjpa.sql.type.BigDecimalType
     */
    public static final cn.sexycode.myjpa.sql.type.BigDecimalType BIG_DECIMAL = BigDecimalType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Character} to JDBC {@link java.sql.Types#CHAR CHAR(1)}.
     *
     * @see cn.sexycode.myjpa.sql.type.CharacterType
     */
    public static final cn.sexycode.myjpa.sql.type.CharacterType CHARACTER = CharacterType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link String} to JDBC {@link java.sql.Types#VARCHAR VARCHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.StringType
     */
    public static final cn.sexycode.myjpa.sql.type.StringType STRING = StringType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link String} to JDBC {@link java.sql.Types#NVARCHAR NVARCHAR}
     */
    public static final StringNVarcharType NSTRING = StringNVarcharType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.net.URL} to JDBC {@link java.sql.Types#VARCHAR VARCHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.UrlType
     */
    public static final cn.sexycode.myjpa.sql.type.UrlType URL = UrlType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.Date} ({@link java.sql.Time}) to JDBC
     * {@link java.sql.Types#TIME TIME}.
     *
     * @see cn.sexycode.myjpa.sql.type.TimeType
     */
    public static final cn.sexycode.myjpa.sql.type.TimeType TIME = cn.sexycode.myjpa.sql.type.TimeType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.Date} ({@link java.sql.Date}) to JDBC
     * {@link java.sql.Types#DATE DATE}.
     *
     * @see cn.sexycode.myjpa.sql.type.TimeType
     */
    public static final cn.sexycode.myjpa.sql.type.DateType DATE = DateType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.Date} ({@link java.sql.Timestamp}) to JDBC
     * {@link java.sql.Types#TIMESTAMP TIMESTAMP}.
     *
     * @see TimeType
     */
    public static final cn.sexycode.myjpa.sql.type.TimestampType TIMESTAMP = TimestampType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.Calendar} to JDBC
     * {@link java.sql.Types#TIMESTAMP TIMESTAMP}.
     *
     * @see cn.sexycode.myjpa.sql.type.CalendarType
     */
    public static final cn.sexycode.myjpa.sql.type.CalendarType CALENDAR = CalendarType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.Calendar} to JDBC
     * {@link java.sql.Types#DATE DATE}.
     *
     * @see cn.sexycode.myjpa.sql.type.CalendarDateType
     */
    public static final cn.sexycode.myjpa.sql.type.CalendarDateType CALENDAR_DATE = CalendarDateType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Class} to JDBC {@link java.sql.Types#VARCHAR VARCHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.ClassType
     */
    public static final cn.sexycode.myjpa.sql.type.ClassType CLASS = ClassType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.Locale} to JDBC {@link java.sql.Types#VARCHAR VARCHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.LocaleType
     */
    public static final cn.sexycode.myjpa.sql.type.LocaleType LOCALE = LocaleType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.Currency} to JDBC {@link java.sql.Types#VARCHAR VARCHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.CurrencyType
     */
    public static final cn.sexycode.myjpa.sql.type.CurrencyType CURRENCY = CurrencyType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.TimeZone} to JDBC {@link java.sql.Types#VARCHAR VARCHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.TimeZoneType
     */
    public static final cn.sexycode.myjpa.sql.type.TimeZoneType TIMEZONE = TimeZoneType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.UUID} to JDBC {@link java.sql.Types#BINARY BINARY}.
     *
     * @see cn.sexycode.myjpa.sql.type.UUIDBinaryType
     */
    public static final cn.sexycode.myjpa.sql.type.UUIDBinaryType UUID_BINARY = UUIDBinaryType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.util.UUID} to JDBC {@link java.sql.Types#CHAR CHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.UUIDCharType
     */
    public static final cn.sexycode.myjpa.sql.type.UUIDCharType UUID_CHAR = UUIDCharType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@code byte[]} to JDBC {@link java.sql.Types#VARBINARY VARBINARY}.
     *
     * @see cn.sexycode.myjpa.sql.type.BinaryType
     */
    public static final cn.sexycode.myjpa.sql.type.BinaryType BINARY = BinaryType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Byte Byte[]} to JDBC {@link java.sql.Types#VARBINARY VARBINARY}.
     *
     * @see cn.sexycode.myjpa.sql.type.WrapperBinaryType
     */
    public static final cn.sexycode.myjpa.sql.type.WrapperBinaryType WRAPPER_BINARY = WrapperBinaryType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@code byte[]} to JDBC {@link java.sql.Types#VARBINARY VARBINARY},
     * specifically for entity versions/timestamps.
     *
     * @see cn.sexycode.myjpa.sql.type.RowVersionType
     */
    public static final cn.sexycode.myjpa.sql.type.RowVersionType ROW_VERSION = RowVersionType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@code byte[]} to JDBC {@link java.sql.Types#LONGVARBINARY LONGVARBINARY}.
     *
     * @see cn.sexycode.myjpa.sql.type.ImageType
     * @see #MATERIALIZED_BLOB
     */
    public static final cn.sexycode.myjpa.sql.type.ImageType IMAGE = ImageType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.sql.Blob} to JDBC {@link java.sql.Types#BLOB BLOB}.
     *
     * @see cn.sexycode.myjpa.sql.type.BlobType
     * @see #MATERIALIZED_BLOB
     */
    public static final cn.sexycode.myjpa.sql.type.BlobType BLOB = BlobType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@code byte[]} to JDBC {@link java.sql.Types#BLOB BLOB}.
     *
     * @see MaterializedBlobType
     * @see #MATERIALIZED_BLOB
     * @see #IMAGE
     */
    public static final MaterializedBlobType MATERIALIZED_BLOB = MaterializedBlobType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@code char[]} to JDBC {@link java.sql.Types#VARCHAR VARCHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.CharArrayType
     */
    public static final cn.sexycode.myjpa.sql.type.CharArrayType CHAR_ARRAY = CharArrayType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link Character Character[]} to JDBC
     * {@link java.sql.Types#VARCHAR VARCHAR}.
     *
     * @see cn.sexycode.myjpa.sql.type.CharacterArrayType
     */
    public static final cn.sexycode.myjpa.sql.type.CharacterArrayType CHARACTER_ARRAY = CharacterArrayType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link String} to JDBC {@link java.sql.Types#LONGVARCHAR LONGVARCHAR}.
     * <p/>
     * Similar to a {@link #MATERIALIZED_CLOB}
     *
     * @see cn.sexycode.myjpa.sql.type.TextType
     */
    public static final cn.sexycode.myjpa.sql.type.TextType TEXT = TextType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link String} to JDBC {@link java.sql.Types#LONGNVARCHAR LONGNVARCHAR}.
     * <p/>
     * Similar to a {@link #MATERIALIZED_NCLOB}
     *
     * @see cn.sexycode.myjpa.sql.type.NTextType
     */
    public static final cn.sexycode.myjpa.sql.type.NTextType NTEXT = NTextType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.sql.Clob} to JDBC {@link java.sql.Types#CLOB CLOB}.
     *
     * @see cn.sexycode.myjpa.sql.type.ClobType
     * @see #MATERIALIZED_CLOB
     */
    public static final cn.sexycode.myjpa.sql.type.ClobType CLOB = ClobType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.sql.NClob} to JDBC {@link java.sql.Types#NCLOB NCLOB}.
     *
     * @see cn.sexycode.myjpa.sql.type.NClobType
     * @see #MATERIALIZED_NCLOB
     */
    public static final cn.sexycode.myjpa.sql.type.NClobType NCLOB = NClobType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link String} to JDBC {@link java.sql.Types#CLOB CLOB}.
     *
     * @see MaterializedClobType
     * @see #MATERIALIZED_CLOB
     * @see #TEXT
     */
    public static final MaterializedClobType MATERIALIZED_CLOB = MaterializedClobType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link String} to JDBC {@link java.sql.Types#NCLOB NCLOB}.
     *
     * @see MaterializedNClobType
     * @see #MATERIALIZED_CLOB
     * @see #NTEXT
     */
    public static final MaterializedNClobType MATERIALIZED_NCLOB = MaterializedNClobType.INSTANCE;

    /**
     * The standard Hibernate type for mapping {@link java.io.Serializable} to JDBC {@link java.sql.Types#VARBINARY VARBINARY}.
     * <p/>
     * See especially the discussion wrt {@link ClassLoader} determination on {@link cn.sexycode.myjpa.sql.type.SerializableType}
     *
     * @see cn.sexycode.myjpa.sql.type.SerializableType
     */
    public static final cn.sexycode.myjpa.sql.type.SerializableType SERIALIZABLE = SerializableType.INSTANCE;
}
