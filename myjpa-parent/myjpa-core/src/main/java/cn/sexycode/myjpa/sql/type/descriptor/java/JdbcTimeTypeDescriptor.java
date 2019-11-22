package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.MutableMutabilityPlan;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Descriptor for {@link Time} handling.
 *
 * @author qzz
 */
public class JdbcTimeTypeDescriptor extends AbstractTypeDescriptor<Date> {
    public static final JdbcTimeTypeDescriptor INSTANCE = new JdbcTimeTypeDescriptor();
    public static final String TIME_FORMAT = "HH:mm:ss.SSS";

    public static class TimeMutabilityPlan extends MutableMutabilityPlan<Date> {
        public static final TimeMutabilityPlan INSTANCE = new TimeMutabilityPlan();

        @Override
        public Date deepCopyNotNull(Date value) {
            return Time.class.isInstance(value)
                    ? new Time(value.getTime())
                    : new Date(value.getTime());
        }
    }

    public JdbcTimeTypeDescriptor() {
        super(Date.class, TimeMutabilityPlan.INSTANCE);
    }

    @Override
    public String toString(Date value) {
        return new SimpleDateFormat(TIME_FORMAT).format(value);
    }

    @Override
    public Date fromString(String string) {
        try {
            return new Time(new SimpleDateFormat(TIME_FORMAT).parse(string).getTime());
        } catch (ParseException pe) {
            throw new TypeException("could not parse time string" + string, pe);
        }
    }

    @Override
    public int extractHashCode(Date value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(value);
        int hashCode = 1;
        hashCode = 31 * hashCode + calendar.get(Calendar.HOUR_OF_DAY);
        hashCode = 31 * hashCode + calendar.get(Calendar.MINUTE);
        hashCode = 31 * hashCode + calendar.get(Calendar.SECOND);
        hashCode = 31 * hashCode + calendar.get(Calendar.MILLISECOND);
        return hashCode;
    }

    @Override
    public boolean areEqual(Date one, Date another) {
        if (one == another) {
            return true;
        }
        if (one == null || another == null) {
            return false;
        }

        if (one.getTime() == another.getTime()) {
            return true;
        }

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(one);
        calendar2.setTime(another);

        return calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY)
                && calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE)
                && calendar1.get(Calendar.SECOND) == calendar2.get(Calendar.SECOND)
                && calendar1.get(Calendar.MILLISECOND) == calendar2.get(Calendar.MILLISECOND);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <X> X unwrap(Date value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Time.class.isAssignableFrom(type)) {
            final Time rtn = Time.class.isInstance(value)
                    ? (Time) value
                    : new Time(value.getTime());
            return (X) rtn;
        }
        if (java.sql.Date.class.isAssignableFrom(type)) {
            final java.sql.Date rtn = java.sql.Date.class.isInstance(value)
                    ? (java.sql.Date) value
                    : new java.sql.Date(value.getTime());
            return (X) rtn;
        }
        if (java.sql.Timestamp.class.isAssignableFrom(type)) {
            final java.sql.Timestamp rtn = java.sql.Timestamp.class.isInstance(value)
                    ? (java.sql.Timestamp) value
                    : new java.sql.Timestamp(value.getTime());
            return (X) rtn;
        }
        if (Date.class.isAssignableFrom(type)) {
            return (X) value;
        }
        if (Calendar.class.isAssignableFrom(type)) {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(value.getTime());
            return (X) cal;
        }
        if (Long.class.isAssignableFrom(type)) {
            return (X) Long.valueOf(value.getTime());
        }
        throw unknownUnwrap(type);
    }

    @Override
    public <X> Date wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Time.class.isInstance(value)) {
            return (Time) value;
        }

        if (Long.class.isInstance(value)) {
            return new Time((Long) value);
        }

        if (Calendar.class.isInstance(value)) {
            return new Time(((Calendar) value).getTimeInMillis());
        }

        if (Date.class.isInstance(value)) {
            return (Date) value;
        }

        throw unknownWrap(value.getClass());
    }
}
