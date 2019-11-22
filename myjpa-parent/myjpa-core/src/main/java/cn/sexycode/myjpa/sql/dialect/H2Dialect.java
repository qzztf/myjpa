package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.AbstractDialect;
import cn.sexycode.myjpa.sql.dialect.pagination.AbstractLimitHandler;
import cn.sexycode.myjpa.sql.dialect.pagination.LimitHandler;
import cn.sexycode.myjpa.sql.dialect.pagination.LimitHelper;
import cn.sexycode.myjpa.sql.model.RowSelection;
import cn.sexycode.util.core.object.ReflectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;

/**
 * A dialect compatible with the H2 database.
 */
public class H2Dialect extends AbstractDialect {
    private static final Logger LOG = LoggerFactory.getLogger(H2Dialect.class);

    private static final AbstractLimitHandler LIMIT_HANDLER = new AbstractLimitHandler() {
        @Override
        public String processSql(String sql, RowSelection selection) {
            final boolean hasOffset = LimitHelper.hasFirstRow(selection);
            return sql + (hasOffset ? " limit ? offset ?" : " limit ?");
        }

        @Override
        public boolean supportsLimit() {
            return true;
        }

        @Override
        public boolean bindLimitParametersInReverseOrder() {
            return true;
        }
    };

    private final String querySequenceString;

    /**
     * Constructs a H2Dialect
     */
    public H2Dialect() {
        super();

        String querySequenceString = "select sequence_name from information_schema.sequences";
        try {
            // HHH-2300
            final Class h2ConstantsClass = ReflectHelper.classForName("org.h2.engine.Constants");
            final int majorVersion = (Integer) h2ConstantsClass.getDeclaredField("VERSION_MAJOR").get(null);
            final int minorVersion = (Integer) h2ConstantsClass.getDeclaredField("VERSION_MINOR").get(null);
            final int buildId = (Integer) h2ConstantsClass.getDeclaredField("BUILD_ID").get(null);
            if (buildId < 32) {
                querySequenceString = "select name from information_schema.sequences";
            }
            if (!(majorVersion > 1 || minorVersion > 2 || buildId >= 139)) {
                LOG.warn("{}, {}, {}", majorVersion, minorVersion, buildId);
            }
        } catch (Exception e) {
            // probably H2 not in the classpath, though in certain app server environments it might just mean we are
            // not using the correct classloader
            LOG.error("error", e);
        }

        this.querySequenceString = querySequenceString;

        registerColumnType(Types.BOOLEAN, "boolean");
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.BINARY, "binary");
        registerColumnType(Types.BIT, "boolean");
        registerColumnType(Types.CHAR, "char($l)");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.DECIMAL, "decimal($p,$s)");
        registerColumnType(Types.NUMERIC, "decimal($p,$s)");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.LONGVARBINARY, "longvarbinary");
        // H2 does define "longvarchar", but it is a simple alias to "varchar"
        registerColumnType(Types.LONGVARCHAR, String.format("varchar(%d)", Integer.MAX_VALUE));
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.VARCHAR, "varchar($l)");
        registerColumnType(Types.VARBINARY, "binary($l)");
        registerColumnType(Types.BLOB, "blob");
        registerColumnType(Types.CLOB, "clob");

        // Aggregations ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //		registerFunction( "avg", new AvgWithArgumentCastFunction( "double" ) );

        // select topic, syntax from information_schema.help
        // where section like 'Function%' order by section, topic
        //
        // see also ->  http://www.h2database.com/html/functions.html

        // Numeric Functions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		/*registerFunction( "acos", new StandardSQLFunction( "acos", StandardBasicTypes.DOUBLE ) );
		registerFunction( "asin", new StandardSQLFunction( "asin", StandardBasicTypes.DOUBLE ) );
		registerFunction( "atan", new StandardSQLFunction( "atan", StandardBasicTypes.DOUBLE ) );
		registerFunction( "atan2", new StandardSQLFunction( "atan2", StandardBasicTypes.DOUBLE ) );
		registerFunction( "bitand", new StandardSQLFunction( "bitand", StandardBasicTypes.INTEGER ) );
		registerFunction( "bitor", new StandardSQLFunction( "bitor", StandardBasicTypes.INTEGER ) );
		registerFunction( "bitxor", new StandardSQLFunction( "bitxor", StandardBasicTypes.INTEGER ) );
		registerFunction( "ceiling", new StandardSQLFunction( "ceiling", StandardBasicTypes.DOUBLE ) );
		registerFunction( "cos", new StandardSQLFunction( "cos", StandardBasicTypes.DOUBLE ) );
		registerFunction( "compress", new StandardSQLFunction( "compress", StandardBasicTypes.BINARY ) );
		registerFunction( "cot", new StandardSQLFunction( "cot", StandardBasicTypes.DOUBLE ) );
		registerFunction( "decrypt", new StandardSQLFunction( "decrypt", StandardBasicTypes.BINARY ) );
		registerFunction( "degrees", new StandardSQLFunction( "degrees", StandardBasicTypes.DOUBLE ) );
		registerFunction( "encrypt", new StandardSQLFunction( "encrypt", StandardBasicTypes.BINARY ) );
		registerFunction( "exp", new StandardSQLFunction( "exp", StandardBasicTypes.DOUBLE ) );
		registerFunction( "expand", new StandardSQLFunction( "compress", StandardBasicTypes.BINARY ) );
		registerFunction( "floor", new StandardSQLFunction( "floor", StandardBasicTypes.DOUBLE ) );
		registerFunction( "hash", new StandardSQLFunction( "hash", StandardBasicTypes.BINARY ) );
		registerFunction( "log", new StandardSQLFunction( "log", StandardBasicTypes.DOUBLE ) );
		registerFunction( "log10", new StandardSQLFunction( "log10", StandardBasicTypes.DOUBLE ) );
		registerFunction( "pi", new NoArgSQLFunction( "pi", StandardBasicTypes.DOUBLE ) );
		registerFunction( "power", new StandardSQLFunction( "power", StandardBasicTypes.DOUBLE ) );
		registerFunction( "radians", new StandardSQLFunction( "radians", StandardBasicTypes.DOUBLE ) );
		registerFunction( "rand", new NoArgSQLFunction( "rand", StandardBasicTypes.DOUBLE ) );
		registerFunction( "round", new StandardSQLFunction( "round", StandardBasicTypes.DOUBLE ) );
		registerFunction( "roundmagic", new StandardSQLFunction( "roundmagic", StandardBasicTypes.DOUBLE ) );
		registerFunction( "sign", new StandardSQLFunction( "sign", StandardBasicTypes.INTEGER ) );
		registerFunction( "sin", new StandardSQLFunction( "sin", StandardBasicTypes.DOUBLE ) );
		registerFunction( "tan", new StandardSQLFunction( "tan", StandardBasicTypes.DOUBLE ) );
		registerFunction( "truncate", new StandardSQLFunction( "truncate", StandardBasicTypes.DOUBLE ) );

		// String Functions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		registerFunction( "ascii", new StandardSQLFunction( "ascii", StandardBasicTypes.INTEGER ) );
		registerFunction( "char", new StandardSQLFunction( "char", StandardBasicTypes.CHARACTER ) );
		registerFunction( "concat", new VarArgsSQLFunction( StandardBasicTypes.STRING, "(", "||", ")" ) );
		registerFunction( "difference", new StandardSQLFunction( "difference", StandardBasicTypes.INTEGER ) );
		registerFunction( "hextoraw", new StandardSQLFunction( "hextoraw", StandardBasicTypes.STRING ) );
		registerFunction( "insert", new StandardSQLFunction( "lower", StandardBasicTypes.STRING ) );
		registerFunction( "left", new StandardSQLFunction( "left", StandardBasicTypes.STRING ) );
		registerFunction( "lcase", new StandardSQLFunction( "lcase", StandardBasicTypes.STRING ) );
		registerFunction( "ltrim", new StandardSQLFunction( "ltrim", StandardBasicTypes.STRING ) );
		registerFunction( "octet_length", new StandardSQLFunction( "octet_length", StandardBasicTypes.INTEGER ) );
		registerFunction( "position", new StandardSQLFunction( "position", StandardBasicTypes.INTEGER ) );
		registerFunction( "rawtohex", new StandardSQLFunction( "rawtohex", StandardBasicTypes.STRING ) );
		registerFunction( "repeat", new StandardSQLFunction( "repeat", StandardBasicTypes.STRING ) );
		registerFunction( "replace", new StandardSQLFunction( "replace", StandardBasicTypes.STRING ) );
		registerFunction( "right", new StandardSQLFunction( "right", StandardBasicTypes.STRING ) );
		registerFunction( "rtrim", new StandardSQLFunction( "rtrim", StandardBasicTypes.STRING ) );
		registerFunction( "soundex", new StandardSQLFunction( "soundex", StandardBasicTypes.STRING ) );
		registerFunction( "space", new StandardSQLFunction( "space", StandardBasicTypes.STRING ) );
		registerFunction( "stringencode", new StandardSQLFunction( "stringencode", StandardBasicTypes.STRING ) );
		registerFunction( "stringdecode", new StandardSQLFunction( "stringdecode", StandardBasicTypes.STRING ) );
		registerFunction( "stringtoutf8", new StandardSQLFunction( "stringtoutf8", StandardBasicTypes.BINARY ) );
		registerFunction( "ucase", new StandardSQLFunction( "ucase", StandardBasicTypes.STRING ) );
		registerFunction( "utf8tostring", new StandardSQLFunction( "utf8tostring", StandardBasicTypes.STRING ) );

		// Time and Date Functions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		registerFunction( "curdate", new NoArgSQLFunction( "curdate", StandardBasicTypes.DATE ) );
		registerFunction( "curtime", new NoArgSQLFunction( "curtime", StandardBasicTypes.TIME ) );
		registerFunction( "curtimestamp", new NoArgSQLFunction( "curtimestamp", StandardBasicTypes.TIME ) );
		registerFunction( "current_date", new NoArgSQLFunction( "current_date", StandardBasicTypes.DATE ) );
		registerFunction( "current_time", new NoArgSQLFunction( "current_time", StandardBasicTypes.TIME ) );
		registerFunction( "current_timestamp", new NoArgSQLFunction( "current_timestamp", StandardBasicTypes.TIMESTAMP ) );
		registerFunction( "datediff", new StandardSQLFunction( "datediff", StandardBasicTypes.INTEGER ) );
		registerFunction( "dayname", new StandardSQLFunction( "dayname", StandardBasicTypes.STRING ) );
		registerFunction( "dayofmonth", new StandardSQLFunction( "dayofmonth", StandardBasicTypes.INTEGER ) );
		registerFunction( "dayofweek", new StandardSQLFunction( "dayofweek", StandardBasicTypes.INTEGER ) );
		registerFunction( "dayofyear", new StandardSQLFunction( "dayofyear", StandardBasicTypes.INTEGER ) );
		registerFunction( "monthname", new StandardSQLFunction( "monthname", StandardBasicTypes.STRING ) );
		registerFunction( "now", new NoArgSQLFunction( "now", StandardBasicTypes.TIMESTAMP ) );
		registerFunction( "quarter", new StandardSQLFunction( "quarter", StandardBasicTypes.INTEGER ) );
		registerFunction( "week", new StandardSQLFunction( "week", StandardBasicTypes.INTEGER ) );

		// System Functions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		registerFunction( "database", new NoArgSQLFunction( "database", StandardBasicTypes.STRING ) );
		registerFunction( "user", new NoArgSQLFunction( "user", StandardBasicTypes.STRING ) );

		getDefaultProperties().setProperty( AvailableSettings.STATEMENT_BATCH_SIZE, DEFAULT_BATCH_SIZE );
		// http://code.google.com/p/h2database/issues/detail?id=235
		getDefaultProperties().setProperty( AvailableSettings.NON_CONTEXTUAL_LOB_CREATION, "true" );*/
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public String getForUpdateString() {
        return " for update";
    }

    @Override
    public LimitHandler getLimitHandler() {
        return LIMIT_HANDLER;
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String sql, boolean hasOffset) {
        return sql + (hasOffset ? " limit ? offset ?" : " limit ?");
    }

    @Override
    public boolean bindLimitParametersInReverseOrder() {
        return true;
    }

    @Override
    public boolean bindLimitParametersFirst() {
        return false;
    }

    @Override
    public boolean supportsIfExistsAfterTableName() {
        return true;
    }

    @Override
    public String getAlias() {
        return "h2";
    }

    @Override
    public boolean supportsIfExistsBeforeConstraintName() {
        return true;
    }

    @Override
    public boolean supportsSequences() {
        return true;
    }

    @Override
    public boolean supportsPooledSequences() {
        return true;
    }

    @Override
    public String getCreateSequenceString(String sequenceName) {
        return "create sequence " + sequenceName;
    }

    @Override
    public String getDropSequenceString(String sequenceName) {
        return "drop sequence if exists " + sequenceName;
    }

    @Override
    public String getSelectSequenceNextValString(String sequenceName) {
        return "next value for " + sequenceName;
    }

    @Override
    public String getSequenceNextValString(String sequenceName) {
        return "call next value for " + sequenceName;
    }

    @Override
    public String getQuerySequencesString() {
        return querySequenceString;
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
        return "call current_timestamp()";
    }

    @Override
    public boolean supportsUnionAll() {
        return true;
    }

    // Overridden informational metadata ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean supportsLobValueChangePropogation() {
        return false;
    }

    @Override
    public boolean requiresParensForTupleDistinctCounts() {
        return true;
    }

    @Override
    public boolean doesReadCommittedCauseWritersToBlockReaders() {
        // see http://groups.google.com/group/h2-database/browse_thread/thread/562d8a49e2dabe99?hl=en
        return true;
    }

    @Override
    public boolean dropConstraints() {
        // We don't need to drop constraints before dropping tables, that just leads to error
        // messages about missing tables when we don't have a schema in the database
        return false;
    }

}
