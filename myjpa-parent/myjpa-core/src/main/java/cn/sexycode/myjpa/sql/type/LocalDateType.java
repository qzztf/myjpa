package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.descriptor.java.LocalDateJavaDescriptor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 */
public class LocalDateType
        extends AbstractSingleColumnStandardBasicType<LocalDate>
        implements LiteralType<LocalDate> {

    /**
     * Singleton access
     */
    public static final LocalDateType INSTANCE = new LocalDateType();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public LocalDateType() {
        super(cn.sexycode.myjpa.sql.type.descriptor.sql.DateTypeDescriptor.INSTANCE, LocalDateJavaDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return LocalDate.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String objectToSQLString(LocalDate value, Dialect dialect) throws Exception {
        return "{d '" + FORMATTER.format(value) + "'}";
    }
}
