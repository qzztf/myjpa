package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.descriptor.java.LocalTimeJavaDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TimeTypeDescriptor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A type that maps between {@link java.sql.Types#TIMESTAMP TIMESTAMP} and {@link java.time.LocalDateTime}.
 *
 */
public class LocalTimeType
        extends AbstractSingleColumnStandardBasicType<LocalTime>
        implements LiteralType<LocalTime> {
    /**
     * Singleton access
     */
    public static final LocalTimeType INSTANCE = new LocalTimeType();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);

    public LocalTimeType() {
        super(TimeTypeDescriptor.INSTANCE, LocalTimeJavaDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return LocalTime.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String objectToSQLString(LocalTime value, Dialect dialect) throws Exception {
        return "{t '" + FORMATTER.format(value) + "'}";
    }
}
