package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.Database;
import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.dialect.DialectResolutionInfo;
import cn.sexycode.myjpa.sql.dialect.DialectResolver;

/**
 * The standard DialectResolver implementation
 *
 */
public final class StandardDialectResolver implements DialectResolver {

    @Override
    public cn.sexycode.myjpa.sql.dialect.Dialect resolveDialect(DialectResolutionInfo info) {

        for (cn.sexycode.myjpa.sql.dialect.Database database : Database.values()) {
            Dialect dialect = database.resolveDialect(info);
            if (dialect != null) {
                return dialect;
            }
        }

        return null;
    }
}
