package cn.sexycode.myjpa.sql.type.descriptor.java;

import java.util.Comparator;

/**
 * Comparator for things that cannot be compared (in a way we know about).
 *
 * @author qzz
 */
public class IncomparableComparator implements Comparator {
    public static final IncomparableComparator INSTANCE = new IncomparableComparator();

    @Override
    @SuppressWarnings("ComparatorMethodParameterNotUsed")
    public int compare(Object o1, Object o2) {
        return 0;
    }
}
