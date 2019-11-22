package cn.sexycode.myjpa.sql.util;

import cn.sexycode.myjpa.sql.util.CharacterStream;

/**
 * Marker interface for non-contextually created {@link java.sql.Clob} instances..
 *
 */
public interface ClobImplementer {
    /**
     * Gets access to the data underlying this CLOB.
     *
     * @return Access to the underlying data.
     */
    CharacterStream getUnderlyingStream();
}
