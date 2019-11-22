package cn.sexycode.myjpa.sql.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Delegates to Comparable
 *
 */
public class ComparableComparator<T extends Comparable> implements Comparator<T>, Serializable {
    public static final Comparator INSTANCE = new ComparableComparator();

    @Override
    public int compare(Comparable one, Comparable another) {
        return one.compareTo(another);
    }
}
