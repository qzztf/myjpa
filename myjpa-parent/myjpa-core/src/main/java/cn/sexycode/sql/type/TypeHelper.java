/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

/**
 * Collection of convenience methods relating to operations across arrays of types...
 *
 * @author Steve Ebersole
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
