package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.AbstractDialect;
import cn.sexycode.myjpa.sql.dialect.MySQLStorageEngine;
import cn.sexycode.myjpa.sql.dialect.identity.IdentityColumnSupport;
import cn.sexycode.myjpa.sql.dialect.identity.MySQLIdentityColumnSupport;
import cn.sexycode.myjpa.sql.mapping.Column;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


/**
 * An SQL dialect for MySQL (prior to 5.x).
 *
 */
@SuppressWarnings("deprecation")
public class MySQLDialect extends AbstractDialect {

    private MySQLStorageEngine storageEngine;



    /**
     * Constructs a MySQLDialect
     */
    public MySQLDialect() {
        super();

        registerColumnType(Types.BIT, "bit");
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.CHAR, "char(1)");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.DOUBLE, "double precision");
        registerColumnType(Types.BOOLEAN, "bit"); // HHH-6935
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "datetime");
        registerColumnType(Types.VARBINARY, "longblob");
        registerColumnType(Types.VARBINARY, 16777215, "mediumblob");
        registerColumnType(Types.VARBINARY, 65535, "blob");
        registerColumnType(Types.VARBINARY, 255, "tinyblob");
        registerColumnType(Types.BINARY, "binary($l)");
        registerColumnType(Types.LONGVARBINARY, "longblob");
        registerColumnType(Types.LONGVARBINARY, 16777215, "mediumblob");
        registerColumnType(Types.NUMERIC, "decimal($p,$s)");
        registerColumnType(Types.BLOB, "longblob");
//		registerColumnType( Types.BLOB, 16777215, "mediumblob" );
//		registerColumnType( Types.BLOB, 65535, "blob" );
        registerColumnType(Types.CLOB, "longtext");
        registerColumnType(Types.NCLOB, "longtext");
//		registerColumnType( Types.CLOB, 16777215, "mediumtext" );
//		registerColumnType( Types.CLOB, 65535, "text" );
        registerVarcharTypes();

    }

    protected void registerVarcharTypes() {
        registerColumnType(Types.VARCHAR, "longtext");
//		registerColumnType( Types.VARCHAR, 16777215, "mediumtext" );
//		registerColumnType( Types.VARCHAR, 65535, "text" );
        registerColumnType(Types.VARCHAR, 255, "varchar($l)");
        registerColumnType(Types.LONGVARCHAR, "longtext");
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public boolean qualifyIndexName() {
        return false;
    }

    @Override
    public String getAddForeignKeyConstraintString(
            String constraintName,
            String[] foreignKey,
            String referencedTable,
            String[] primaryKey,
            boolean referencesPrimaryKey) {
        final String cols = String.join(", ", foreignKey);
        final String referencedCols = String.join(", ", primaryKey);
        return String.format(
                " add constraint %s foreign key (%s) references %s (%s)",
                constraintName,
                cols,
                referencedTable,
                referencedCols
        );
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getDropForeignKeyString() {
        return " drop foreign key ";
    }

    @Override
    public String getLimitString(String sql, boolean hasOffset) {
        return sql + (hasOffset ? " limit ?, ?" : " limit ?");
    }

    @Override
    public char closeQuote() {
        return '`';
    }

    @Override
    public char openQuote() {
        return '`';
    }

    @Override
    public boolean canCreateCatalog() {
        return true;
    }

    @Override
    public String[] getCreateCatalogCommand(String catalogName) {
        return new String[]{"create database " + catalogName};
    }

    @Override
    public String[] getDropCatalogCommand(String catalogName) {
        return new String[]{"drop database " + catalogName};
    }

    @Override
    public boolean canCreateSchema() {
        return false;
    }

    @Override
    public String[] getCreateSchemaCommand(String schemaName) {
        throw new UnsupportedOperationException("MySQL does not support dropping creating/dropping schemas in the JDBC sense");
    }

    @Override
    public String[] getDropSchemaCommand(String schemaName) {
        throw new UnsupportedOperationException("MySQL does not support dropping creating/dropping schemas in the JDBC sense");
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public String getSelectGUIDString() {
        return "select uuid()";
    }

    @Override
    public String getTableComment(String comment) {
        return " comment='" + comment + "'";
    }

    @Override
    public String getColumnComment(String comment) {
        return " comment '" + comment + "'";
    }

    /**
     * Determine the cast target for {@link Types#INTEGER}, {@link Types#BIGINT} and {@link Types#SMALLINT}
     *
     * @return The proper cast target type.
     */
    protected String smallIntegerCastTarget() {
        return "signed";
    }

    /**
     * Determine the cast target for {@link Types#FLOAT} and {@link Types#REAL} (DOUBLE)
     *
     * @return The proper cast target type.
     */
    protected String floatingPointNumberCastTarget() {
        // MySQL does not allow casting to DOUBLE nor FLOAT, so we have to cast these as DECIMAL.
        // MariaDB does allow casting to DOUBLE, although not FLOAT.
        return fixedPointNumberCastTarget();
    }

    /**
     * Determine the cast target for {@link Types#NUMERIC}
     *
     * @return The proper cast target type.
     */
    protected String fixedPointNumberCastTarget() {
        // NOTE : the precision/scale are somewhat arbitrary choices, but MySQL/MariaDB
        // effectively require *some* values
        return "decimal(" + Column.DEFAULT_PRECISION + "," + Column.DEFAULT_SCALE + ")";
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
        return "select now()";
    }

    @Override
    public int registerResultSetOutParameter(CallableStatement statement, int col) throws SQLException {
        return col;
    }

    @Override
    public ResultSet getResultSet(CallableStatement ps) throws SQLException {
        boolean isResultSet = ps.execute();
        while (!isResultSet && ps.getUpdateCount() != -1) {
            isResultSet = ps.getMoreResults();
        }
        return ps.getResultSet();
    }

    @Override
    public boolean supportsRowValueConstructorSyntax() {
        return true;
    }

    // locking support

    @Override
    public String getForUpdateString() {
        return " for update";
    }

    @Override
    public String getWriteLockString(int timeout) {
        return " for update";
    }

    @Override
    public String getReadLockString(int timeout) {
        return " lock in share mode";
    }


    // Overridden informational metadata ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean supportsEmptyInList() {
        return false;
    }

    @Override
    public boolean areStringComparisonsCaseInsensitive() {
        return true;
    }

    @Override
    public boolean supportsLobValueChangePropogation() {
        // note: at least my local MySQL 5.1 install shows this not working...
        return false;
    }

    @Override
    public boolean supportsSubqueryOnMutatingTable() {
        return false;
    }

    @Override
    public boolean supportsLockTimeouts() {
        // yes, we do handle "lock timeout" conditions in the exception conversion delegate,
        // but that's a hardcoded lock timeout period across the whole entire MySQL database.
        // MySQL does not support specifying lock timeouts as part of the SQL statement, which is really
        // what this meta method is asking.
        return false;
    }

    @Override
    public String getNotExpression(String expression) {
        return "not (" + expression + ")";
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new MySQLIdentityColumnSupport();
    }

    @Override
    public boolean isJdbcLogWarningsEnabledByDefault() {
        return false;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return storageEngine.supportsCascadeDelete();
    }


    @Override
    public boolean hasSelfReferentialForeignKeyBug() {
        return storageEngine.hasSelfReferentialForeignKeyBug();
    }

    @Override
    public boolean dropConstraints() {
        return storageEngine.dropConstraints();
    }

    @Override
    public String getAlias() {
        return "mysql5";
    }

    @Override
    public String toBooleanValueString(Boolean value) {
        return null;
    }

    @Override
    public void registerColumnType(int code, String name) {

    }
}
