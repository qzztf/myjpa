/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.dialect;

import cn.sexycode.sql.Environment;
import cn.sexycode.sql.LockMode;
import cn.sexycode.sql.LockOptions;
import cn.sexycode.sql.dialect.function.*;
import cn.sexycode.sql.dialect.identity.AbstractTransactSQLIdentityColumnSupport;
import cn.sexycode.sql.dialect.identity.IdentityColumnSupport;
import cn.sexycode.sql.type.StandardBasicTypes;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * An abstract base class for Sybase and MS SQL Server dialects.
 *
 * @author Gavin King
 */
abstract class AbstractTransactSQLDialect extends AbstractDialect {
    public AbstractTransactSQLDialect() {
        super();
        registerColumnType(Types.BINARY, "binary($l)");
        registerColumnType(Types.BIT, "tinyint");
        registerColumnType(Types.BIGINT, "numeric(19,0)");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.TINYINT, "smallint");
        registerColumnType(Types.INTEGER, "int");
        registerColumnType(Types.CHAR, "char(1)");
        registerColumnType(Types.VARCHAR, "varchar($l)");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.DOUBLE, "double precision");
        registerColumnType(Types.DATE, "datetime");
        registerColumnType(Types.TIME, "datetime");
        registerColumnType(Types.TIMESTAMP, "datetime");
        registerColumnType(Types.VARBINARY, "varbinary($l)");
        registerColumnType(Types.NUMERIC, "numeric($p,$s)");
        registerColumnType(Types.BLOB, "image");
        registerColumnType(Types.CLOB, "text");

        registerFunction("ascii", new StandardSQLFunction("ascii", StandardBasicTypes.INTEGER));
        registerFunction("char", new StandardSQLFunction("char", StandardBasicTypes.CHARACTER));
        registerFunction("len", new StandardSQLFunction("len", StandardBasicTypes.LONG));
        registerFunction("lower", new StandardSQLFunction("lower"));
        registerFunction("upper", new StandardSQLFunction("upper"));
        registerFunction("str", new StandardSQLFunction("str", StandardBasicTypes.STRING));
        registerFunction("ltrim", new StandardSQLFunction("ltrim"));
        registerFunction("rtrim", new StandardSQLFunction("rtrim"));
        registerFunction("reverse", new StandardSQLFunction("reverse"));
        registerFunction("space", new StandardSQLFunction("space", StandardBasicTypes.STRING));

        registerFunction("user", new NoArgSQLFunction("user", StandardBasicTypes.STRING));

        registerFunction("current_timestamp", new NoArgSQLFunction("getdate", StandardBasicTypes.TIMESTAMP));
        registerFunction("current_time", new NoArgSQLFunction("getdate", StandardBasicTypes.TIME));
        registerFunction("current_date", new NoArgSQLFunction("getdate", StandardBasicTypes.DATE));

        registerFunction("getdate", new NoArgSQLFunction("getdate", StandardBasicTypes.TIMESTAMP));
        registerFunction("getutcdate", new NoArgSQLFunction("getutcdate", StandardBasicTypes.TIMESTAMP));
        registerFunction("day", new StandardSQLFunction("day", StandardBasicTypes.INTEGER));
        registerFunction("month", new StandardSQLFunction("month", StandardBasicTypes.INTEGER));
        registerFunction("year", new StandardSQLFunction("year", StandardBasicTypes.INTEGER));
        registerFunction("datename", new StandardSQLFunction("datename", StandardBasicTypes.STRING));

        registerFunction("abs", new StandardSQLFunction("abs"));
        registerFunction("sign", new StandardSQLFunction("sign", StandardBasicTypes.INTEGER));

        registerFunction("acos", new StandardSQLFunction("acos", StandardBasicTypes.DOUBLE));
        registerFunction("asin", new StandardSQLFunction("asin", StandardBasicTypes.DOUBLE));
        registerFunction("atan", new StandardSQLFunction("atan", StandardBasicTypes.DOUBLE));
        registerFunction("cos", new StandardSQLFunction("cos", StandardBasicTypes.DOUBLE));
        registerFunction("cot", new StandardSQLFunction("cot", StandardBasicTypes.DOUBLE));
        registerFunction("exp", new StandardSQLFunction("exp", StandardBasicTypes.DOUBLE));
        registerFunction("log", new StandardSQLFunction("log", StandardBasicTypes.DOUBLE));
        registerFunction("log10", new StandardSQLFunction("log10", StandardBasicTypes.DOUBLE));
        registerFunction("sin", new StandardSQLFunction("sin", StandardBasicTypes.DOUBLE));
        registerFunction("sqrt", new StandardSQLFunction("sqrt", StandardBasicTypes.DOUBLE));
        registerFunction("tan", new StandardSQLFunction("tan", StandardBasicTypes.DOUBLE));
        registerFunction("pi", new NoArgSQLFunction("pi", StandardBasicTypes.DOUBLE));
        registerFunction("square", new StandardSQLFunction("square"));
        registerFunction("rand", new StandardSQLFunction("rand", StandardBasicTypes.FLOAT));

        registerFunction("radians", new StandardSQLFunction("radians", StandardBasicTypes.DOUBLE));
        registerFunction("degrees", new StandardSQLFunction("degrees", StandardBasicTypes.DOUBLE));

        registerFunction("round", new StandardSQLFunction("round"));
        registerFunction("ceiling", new StandardSQLFunction("ceiling"));
        registerFunction("floor", new StandardSQLFunction("floor"));

        registerFunction("isnull", new StandardSQLFunction("isnull"));

        registerFunction("concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "(", "+", ")"));

        registerFunction("length", new StandardSQLFunction("len", StandardBasicTypes.INTEGER));
        registerFunction("trim", new SQLFunctionTemplate(StandardBasicTypes.STRING, "ltrim(rtrim(?1))"));
        registerFunction("locate", new CharIndexFunction());

        getDefaultProperties().setProperty(Environment.STATEMENT_BATCH_SIZE, NO_BATCH);
    }

    @Override
    public String getAddColumnString() {
        return "add";
    }

    @Override
    public String getNullColumnString() {
        return "";
    }

    @Override
    public boolean qualifyIndexName() {
        return false;
    }

    @Override
    public String getForUpdateString() {
        return "";
    }

    @Override
    public String appendLockHint(LockOptions lockOptions, String tableName) {
        return lockOptions.getLockMode().greaterThan(LockMode.READ) ? tableName + " holdlock" : tableName;
    }


    @Override
    public int registerResultSetOutParameter(CallableStatement statement, int col) throws SQLException {
        // sql server just returns automatically
        return col;
    }

    @Override
    public ResultSet getResultSet(CallableStatement ps) throws SQLException {
        boolean isResultSet = ps.execute();
        // This assumes you will want to ignore any update counts
        while (!isResultSet && ps.getUpdateCount() != -1) {
            isResultSet = ps.getMoreResults();
        }

        // You may still have other ResultSets or update counts left to process here
        // but you can't do it now or the ResultSet you just got will be closed
        return ps.getResultSet();
    }

    @Override
    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    @Override
    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    @Override
    public String getCurrentTimestampSelectString() {
        return "select getdate()";
    }


    @Override
    public String getSelectGUIDString() {
        return "select newid()";
    }

    // Overridden informational metadata ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean supportsEmptyInList() {
        return false;
    }

    @Override
    public boolean supportsUnionAll() {
        return true;
    }

    @Override
    public boolean supportsExistsInSelect() {
        return false;
    }

    @Override
    public boolean doesReadCommittedCauseWritersToBlockReaders() {
        return true;
    }

    @Override
    public boolean doesRepeatableReadCauseReadersToBlockWriters() {
        return true;
    }

    @Override
    public boolean supportsTupleDistinctCounts() {
        return false;
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new AbstractTransactSQLIdentityColumnSupport();
    }

    @Override
    public boolean supportsPartitionBy() {
        return true;
    }
}
