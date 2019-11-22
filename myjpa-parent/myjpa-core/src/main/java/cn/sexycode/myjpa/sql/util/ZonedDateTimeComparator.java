package cn.sexycode.myjpa.sql.util;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Comparator;

/**
 */
public class ZonedDateTimeComparator implements Comparator<ZonedDateTime>, Serializable {
    public static final Comparator INSTANCE = new ZonedDateTimeComparator();

    public int compare(ZonedDateTime one, ZonedDateTime another) {
        return one.toInstant().compareTo(another.toInstant());
    }
}
