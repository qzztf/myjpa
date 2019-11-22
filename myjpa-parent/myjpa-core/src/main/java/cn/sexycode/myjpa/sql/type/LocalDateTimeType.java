package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.LocalDateTimeJavaDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TimestampTypeDescriptor;
import cn.sexycode.myjpa.sql.util.ComparableComparator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

/**
 * A type that maps between {@link java.sql.Types#TIMESTAMP TIMESTAMP} and {@link LocalDateTime}.
 *
 */
public class LocalDateTimeType
        extends AbstractSingleColumnStandardBasicType<LocalDateTime>
        implements VersionType<LocalDateTime>, LiteralType<LocalDateTime> {
    /**
     * Singleton access
     */
    public static final LocalDateTimeType INSTANCE = new LocalDateTimeType();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH);

    public LocalDateTimeType() {
        super(TimestampTypeDescriptor.INSTANCE, LocalDateTimeJavaDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return LocalDateTime.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String objectToSQLString(LocalDateTime value, Dialect dialect) throws Exception {
        return "{ts '" + FORMATTER.format(value) + "'}";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<LocalDateTime> getComparator() {
        return ComparableComparator.INSTANCE;
    }
}
