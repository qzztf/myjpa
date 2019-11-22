package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.StringType;
import cn.sexycode.myjpa.sql.type.descriptor.java.JdbcTimeTypeDescriptor;

import java.sql.Time;
import java.util.Date;

/**
 * A type that maps between {@link java.sql.Types#TIME TIME} and {@link Time}
 *
 * @author qzz
 * @author qzz
 */
public class TimeType
        extends AbstractSingleColumnStandardBasicType<Date>
        implements LiteralType<Date> {

    public static final TimeType INSTANCE = new TimeType();

    public TimeType() {
        super(cn.sexycode.myjpa.sql.type.descriptor.sql.TimeTypeDescriptor.INSTANCE, JdbcTimeTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "time";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{
                getName(),
                Time.class.getName()
        };
    }

    //	@Override
//	protected boolean registerUnderJavaType() {
//		return true;
//	}

    public String objectToSQLString(Date value, Dialect dialect) throws Exception {
        Time jdbcTime = Time.class.isInstance(value)
                ? (Time) value
                : new Time(value.getTime());
        // TODO : use JDBC time literal escape syntax? -> {t 'time-string'} in hh:mm:ss format
        return StringType.INSTANCE.objectToSQLString(jdbcTime.toString(), dialect);
    }
}
