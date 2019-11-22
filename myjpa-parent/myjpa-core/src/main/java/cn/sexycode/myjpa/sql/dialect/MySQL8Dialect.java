package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.MySQL57Dialect;

/**
 */
public class MySQL8Dialect extends MySQL57Dialect {

    public MySQL8Dialect() {
        // MySQL doesn't add the new reserved keywords to their JDBC driver to preserve backward compatibility.

        registerKeyword("CUME_DIST");
        registerKeyword("DENSE_RANK");
        registerKeyword("EMPTY");
        registerKeyword("EXCEPT");
        registerKeyword("FIRST_VALUE");
        registerKeyword("GROUPS");
        registerKeyword("JSON_TABLE");
        registerKeyword("LAG");
        registerKeyword("LAST_VALUE");
        registerKeyword("LEAD");
        registerKeyword("NTH_VALUE");
        registerKeyword("NTILE");
        registerKeyword("PERSIST");
        registerKeyword("PERCENT_RANK");
        registerKeyword("PERSIST_ONLY");
        registerKeyword("RANK");
        registerKeyword("ROW_NUMBER");
    }


    @Override
    public String getForUpdateSkipLockedString() {
        return " for update skip locked";
    }

    @Override
    public String getForUpdateSkipLockedString(String aliases) {
        return getForUpdateString() + " of " + aliases + " skip locked";
    }

    @Override
    public String getForUpdateNowaitString() {
        return getForUpdateString() + " nowait ";
    }

    @Override
    public String getForUpdateNowaitString(String aliases) {
        return getForUpdateString(aliases) + " nowait ";
    }

    @Override
    public String getForUpdateString(String aliases) {
        return getForUpdateString() + " of " + aliases;
    }

    @Override
    public boolean supportsSkipLocked() {
        return true;
    }

    public boolean supportsNoWait() {
        return true;
    }
}
