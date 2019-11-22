package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.LocalDateType;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ImmutableMutabilityPlan;

import java.sql.Timestamp;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Java type descriptor for the LocalDateTime type.
 *
 * @author qzz
 */
public class LocalDateJavaDescriptor extends AbstractTypeDescriptor<LocalDate> {
    /**
     * Singleton access
     */
    public static final LocalDateJavaDescriptor INSTANCE = new LocalDateJavaDescriptor();

    @SuppressWarnings("unchecked")
    public LocalDateJavaDescriptor() {
        super(LocalDate.class, ImmutableMutabilityPlan.INSTANCE);
    }

    @Override
    public String toString(LocalDate value) {
        return LocalDateType.FORMATTER.format(value);
    }

    @Override
    public LocalDate fromString(String string) {
        return LocalDate.from(LocalDateType.FORMATTER.parse(string));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X unwrap(LocalDate value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (LocalDate.class.isAssignableFrom(type)) {
            return (X) value;
        }

        if (java.sql.Date.class.isAssignableFrom(type)) {
            return (X) java.sql.Date.valueOf(value);
        }

        final LocalDateTime localDateTime = value.atStartOfDay();

        if (Timestamp.class.isAssignableFrom(type)) {
            return (X) Timestamp.valueOf(localDateTime);
        }

        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        if (Calendar.class.isAssignableFrom(type)) {
            return (X) GregorianCalendar.from(zonedDateTime);
        }

        final Instant instant = zonedDateTime.toInstant();

        if (Date.class.equals(type)) {
            return (X) Date.from(instant);
        }

        if (Long.class.isAssignableFrom(type)) {
            return (X) Long.valueOf(instant.toEpochMilli());
        }

        throw unknownUnwrap(type);
    }

    @Override
    public <X> LocalDate wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (LocalDate.class.isInstance(value)) {
            return (LocalDate) value;
        }

        if (Timestamp.class.isInstance(value)) {
            final Timestamp ts = (Timestamp) value;
            return LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault()).toLocalDate();
        }

        if (Long.class.isInstance(value)) {
            final Instant instant = Instant.ofEpochMilli((Long) value);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        }

        if (Calendar.class.isInstance(value)) {
            final Calendar calendar = (Calendar) value;
            return LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
        }

        if (Date.class.isInstance(value)) {
            if (java.sql.Date.class.isInstance(value)) {
                return ((java.sql.Date) value).toLocalDate();
            } else {
                return Instant.ofEpochMilli(((Date) value).getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }

        throw unknownWrap(value.getClass());
    }

}
