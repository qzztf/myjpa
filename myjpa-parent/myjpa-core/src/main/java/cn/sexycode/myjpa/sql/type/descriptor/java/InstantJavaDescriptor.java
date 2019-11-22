package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.ImmutableMutabilityPlan;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Java type descriptor for the LocalDateTime type.
 *
 * @author qzz
 */
public class InstantJavaDescriptor extends AbstractTypeDescriptor<Instant> {
    /**
     * Singleton access
     */
    public static final InstantJavaDescriptor INSTANCE = new InstantJavaDescriptor();

    @SuppressWarnings("unchecked")
    public InstantJavaDescriptor() {
        super(Instant.class, ImmutableMutabilityPlan.INSTANCE);
    }

    @Override
    public String toString(Instant value) {
        return DateTimeFormatter.ISO_INSTANT.format(value);
    }

    @Override
    public Instant fromString(String string) {
        return Instant.from(DateTimeFormatter.ISO_INSTANT.parse(string));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X unwrap(Instant instant, Class<X> type, WrapperOptions options) {
        if (instant == null) {
            return null;
        }

        if (Instant.class.isAssignableFrom(type)) {
            return (X) instant;
        }

        if (Calendar.class.isAssignableFrom(type)) {
            final ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.UTC);
            return (X) GregorianCalendar.from(instant.atZone(zoneId));
        }

        if (Timestamp.class.isAssignableFrom(type)) {
            return (X) Timestamp.from(instant);
        }

        if (java.sql.Date.class.isAssignableFrom(type)) {
            return (X) java.sql.Date.from(instant);
        }

        if (java.sql.Time.class.isAssignableFrom(type)) {
            return (X) java.sql.Time.from(instant);
        }

        if (Date.class.isAssignableFrom(type)) {
            return (X) Date.from(instant);
        }

        if (Long.class.isAssignableFrom(type)) {
            return (X) Long.valueOf(instant.toEpochMilli());
        }

        throw unknownUnwrap(type);
    }

    @Override
    public <X> Instant wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (Instant.class.isInstance(value)) {
            return (Instant) value;
        }

        if (Timestamp.class.isInstance(value)) {
            final Timestamp ts = (Timestamp) value;
            return ts.toInstant();
        }

        if (Long.class.isInstance(value)) {
            return Instant.ofEpochMilli((Long) value);
        }

        if (Calendar.class.isInstance(value)) {
            final Calendar calendar = (Calendar) value;
            return ZonedDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toInstant();
        }

        if (Date.class.isInstance(value)) {
            return ((Date) value).toInstant();
        }

        throw unknownWrap(value.getClass());
    }
}
