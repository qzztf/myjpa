package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.OffsetDateTimeJavaDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TimestampTypeDescriptor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

/**
 */
public class OffsetDateTimeType
        extends AbstractSingleColumnStandardBasicType<OffsetDateTime>
        implements VersionType<OffsetDateTime>, LiteralType<OffsetDateTime> {

    /**
     * Singleton access
     */
    public static final OffsetDateTimeType INSTANCE = new OffsetDateTimeType();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S xxxxx", Locale.ENGLISH);

    public OffsetDateTimeType() {
        super(TimestampTypeDescriptor.INSTANCE, OffsetDateTimeJavaDescriptor.INSTANCE);
    }

    @Override
    public String objectToSQLString(OffsetDateTime value, Dialect dialect) throws Exception {
        return "{ts '" + FORMATTER.format(value) + "'}";
    }


    @Override
    @SuppressWarnings("unchecked")
    public Comparator<OffsetDateTime> getComparator() {
        return OffsetDateTime.timeLineOrder();
    }

    @Override
    public String getName() {
        return OffsetDateTime.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
