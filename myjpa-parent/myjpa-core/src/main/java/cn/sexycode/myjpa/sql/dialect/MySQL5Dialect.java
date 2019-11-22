package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.MySQLDialect;
import cn.sexycode.myjpa.sql.dialect.hint.IndexQueryHintHandler;

import java.sql.Types;


/**
 * An SQL dialect for MySQL 5.x specific features.
 *
 */
public class MySQL5Dialect extends MySQLDialect {
    @Override
    protected void registerVarcharTypes() {
        registerColumnType(Types.VARCHAR, "longtext");
//		registerColumnType( Types.VARCHAR, 16777215, "mediumtext" );
        registerColumnType(Types.VARCHAR, 65535, "varchar($l)");
        registerColumnType(Types.LONGVARCHAR, "longtext");
    }

    @Override
    public boolean supportsColumnCheck() {
        return false;
    }


    protected String getEngineKeyword() {
        return "engine";
    }


    public String getQueryHintString(String query, String hints) {
        return IndexQueryHintHandler.INSTANCE.addQueryHints(query, hints);
    }

    @Override
    public boolean supportsUnionAll() {
        return true;
    }
}
