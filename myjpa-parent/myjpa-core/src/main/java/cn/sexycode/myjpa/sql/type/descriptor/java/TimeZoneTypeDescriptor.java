package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;

import java.util.Comparator;
import java.util.TimeZone;

/**
 * Descriptor for {@link TimeZone} handling.
 *
 * @author qzz
 */
public class TimeZoneTypeDescriptor extends AbstractTypeDescriptor<TimeZone> {
    public static final TimeZoneTypeDescriptor INSTANCE = new TimeZoneTypeDescriptor();

    public static class TimeZoneComparator implements Comparator<TimeZone> {
        public static final TimeZoneComparator INSTANCE = new TimeZoneComparator();

        public int compare(TimeZone o1, TimeZone o2) {
            return o1.getID().compareTo(o2.getID());
        }
    }

    public TimeZoneTypeDescriptor() {
        super(TimeZone.class);
    }

    public String toString(TimeZone value) {
        return value.getID();
    }

    public TimeZone fromString(String string) {
        return TimeZone.getTimeZone(string);
    }

    @Override
    public Comparator<TimeZone> getComparator() {
        return TimeZoneComparator.INSTANCE;
    }

    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(TimeZone value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (String.class.isAssignableFrom(type)) {
            return (X) toString(value);
        }
        throw unknownUnwrap(type);
    }

    public <X> TimeZone wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (String.class.isInstance(value)) {
            return fromString((String) value);
        }
        throw unknownWrap(value.getClass());
    }
}
