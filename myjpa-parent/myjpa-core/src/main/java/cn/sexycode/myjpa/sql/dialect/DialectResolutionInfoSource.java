package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.DialectResolutionInfo;

/**
 * Contract for the source of DialectResolutionInfo.
 *
 */
public interface DialectResolutionInfoSource {
    /**
     * Get the DialectResolutionInfo
     *
     * @return The DialectResolutionInfo
     */
    public DialectResolutionInfo getDialectResolutionInfo();
}
