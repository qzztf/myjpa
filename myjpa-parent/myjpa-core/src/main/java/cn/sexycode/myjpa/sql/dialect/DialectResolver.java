package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.dialect.DialectResolutionInfo;

/**
 * Contract for determining the {@link cn.sexycode.myjpa.sql.dialect.Dialect} to use based on information about the database / driver.
 *
 */
public interface DialectResolver  {
    /**
     * Determine the {@link cn.sexycode.myjpa.sql.dialect.Dialect} to use based on the given information.  Implementations are expected to return
     * the {@link cn.sexycode.myjpa.sql.dialect.Dialect} instance to use, or {@code null} if the they did not locate a match.
     *
     * @param info Access to the information about the database/driver needed to perform the resolution
     * @return The dialect to use, or null.
     */
    Dialect resolveDialect(DialectResolutionInfo info);
}
