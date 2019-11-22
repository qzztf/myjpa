package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.*;
import cn.sexycode.myjpa.sql.dialect.Dialect;

/**
 * List all supported relational database systems.
 *
 */
public enum Database {

    MYSQL {
        @Override
        public Class<? extends cn.sexycode.myjpa.sql.dialect.Dialect> latestDialect() {
            return MySQL8Dialect.class;
        }

        @Override
        public cn.sexycode.myjpa.sql.dialect.Dialect resolveDialect(DialectResolutionInfo info) {
            final String databaseName = info.getDatabaseName();

            if ("MySQL".equals(databaseName)) {
                final int majorVersion = info.getDatabaseMajorVersion();
                final int minorVersion = info.getDatabaseMinorVersion();

                if (majorVersion < 5) {
                    return new MySQLDialect();
                } else if (majorVersion == 5) {
                    if (minorVersion < 5) {
                        return new MySQL5Dialect();
                    } else if (minorVersion < 7) {
                        return new MySQL55Dialect();
                    } else {
                        return new MySQL57Dialect();
                    }
                }

                return latestDialectInstance(this);
            }

            return null;
        }
    };

    public abstract Class<? extends cn.sexycode.myjpa.sql.dialect.Dialect> latestDialect();

    public abstract cn.sexycode.myjpa.sql.dialect.Dialect resolveDialect(DialectResolutionInfo info);

    private static Dialect latestDialectInstance(Database database) {
        try {
            return database.latestDialect().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DialectException(e);
        }
    }
}
