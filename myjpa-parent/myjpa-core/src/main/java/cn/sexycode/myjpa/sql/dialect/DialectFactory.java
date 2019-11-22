package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.dialect.DialectResolutionInfoSource;
import cn.sexycode.util.core.service.Service;


/**
 * A factory for generating Dialect instances.
 *
 */
public interface DialectFactory extends Service {
    /**
     * Builds an appropriate Dialect instance.
     * <p/>
     * If a dialect is explicitly named in the incoming properties, it should used. Otherwise, it is
     * determined by dialect resolvers based on the passed connection.
     * <p/>
     * An exception is thrown if a dialect was not explicitly set and no resolver could make
     * the determination from the given connection.
     *
     * @param configValues         The configuration properties.
     * @param resolutionInfoSource Access to DialectResolutionInfo used to resolve the Dialect to use if not
     *                             explicitly named
     * @return The appropriate dialect instance.
     */
    Dialect buildDialect(String dialect, DialectResolutionInfoSource resolutionInfoSource);
}
