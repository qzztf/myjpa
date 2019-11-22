package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.InstantJavaDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TimestampTypeDescriptor;
import cn.sexycode.myjpa.sql.util.ComparableComparator;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

/**
 * A type that maps between {@link java.sql.Types#TIMESTAMP TIMESTAMP} and {@link java.time.LocalDateTime}.
 *
 */
public class InstantType
        extends AbstractSingleColumnStandardBasicType<Instant>
        implements VersionType<Instant>, LiteralType<Instant> {
    /**
     * Singleton access
     */
    public static final InstantType INSTANCE = new InstantType();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S 'Z'", Locale.ENGLISH);

    public InstantType() {
        super(TimestampTypeDescriptor.INSTANCE, InstantJavaDescriptor.INSTANCE);
    }

    @Override
    public String objectToSQLString(Instant value, Dialect dialect) throws Exception {
        return "{ts '" + FORMATTER.format(ZonedDateTime.ofInstant(value, ZoneId.of("UTC"))) + "'}";
    }


    @Override
    @SuppressWarnings("unchecked")
    public Comparator<Instant> getComparator() {
        return ComparableComparator.INSTANCE;
    }

    @Override
    public String getName() {
        return Instant.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
