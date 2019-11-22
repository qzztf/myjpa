package cn.sexycode.myjpa.sql.util;

import cn.sexycode.myjpa.sql.util.ClobImplementer;

/**
 * Marker interface for non-contextually created java.sql.NClob instances..
 * <p/>
 * java.sql.NClob is a new type introduced in JDK 1.6 (JDBC 4)
 *
 */
public interface NClobImplementer extends ClobImplementer {
}
