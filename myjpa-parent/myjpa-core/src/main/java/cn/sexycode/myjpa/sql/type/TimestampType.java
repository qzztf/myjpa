package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.*;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.StringType;
import cn.sexycode.myjpa.sql.type.VersionType;
import cn.sexycode.myjpa.sql.type.descriptor.java.JdbcTimestampTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TimestampTypeDescriptor;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * A type that maps between {@link java.sql.Types#TIMESTAMP TIMESTAMP} and {@link Timestamp}
 *
 */
public class TimestampType
        extends AbstractSingleColumnStandardBasicType<Date>
        implements VersionType<Date>, LiteralType<Date> {

    public static final TimestampType INSTANCE = new TimestampType();

    public TimestampType() {
        super(TimestampTypeDescriptor.INSTANCE, JdbcTimestampTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "timestamp";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), Timestamp.class.getName(), Date.class.getName()};
    }


    @Override
    public Comparator<Date> getComparator() {
        return getJavaTypeDescriptor().getComparator();
    }

    @Override
    public String objectToSQLString(Date value, Dialect dialect) throws Exception {
        final Timestamp ts = Timestamp.class.isInstance(value)
                ? (Timestamp) value
                : new Timestamp(value.getTime());
        // TODO : use JDBC date literal escape syntax? -> {d 'date-string'} in yyyy-mm-dd hh:mm:ss[.f...] format
        return StringType.INSTANCE.objectToSQLString(ts.toString(), dialect);
    }

    @Override
    public Date fromStringValue(String xml) throws TypeException {
        return fromString(xml);
    }
}
