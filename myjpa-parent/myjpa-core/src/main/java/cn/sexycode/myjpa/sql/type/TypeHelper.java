package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.Type;

/**
 * Collection of convenience methods relating to operations across arrays of types...
 *
 */
public class TypeHelper {
    /**
     * Disallow instantiation
     */
    private TypeHelper() {
    }


    public static String toLoggableString(
            Object[] state,
            Type[] types) {
        final StringBuilder buff = new StringBuilder();
        for (int i = 0; i < state.length; i++) {
            if (i > 0) {
                buff.append(", ");
            }
        }
        return buff.toString();
    }
}
