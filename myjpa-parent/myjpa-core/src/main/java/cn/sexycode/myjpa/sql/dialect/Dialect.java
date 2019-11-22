package cn.sexycode.myjpa.sql.dialect;

import cn.sexycode.myjpa.sql.dialect.function.SQLFunction;
import cn.sexycode.myjpa.sql.dialect.identity.IdentityColumnSupport;
import cn.sexycode.myjpa.sql.mapping.JoinFragment;

import java.util.Map;

public interface Dialect {

    /**
     * Defines a default batch size constant
     */
    String DEFAULT_BATCH_SIZE = "15";

    /**
     * Defines a "no batching" batch size constant
     */
    String NO_BATCH = "0";

    /**
     * Characters used as opening for quoting SQL identifiers
     */
    String QUOTE = "`\"[";

    /**
     * Characters used as closing for quoting SQL identifiers
     */
    String CLOSED_QUOTE = "`\"]";

    String getAlias();

    String toBooleanValueString(Boolean value);

    Map<? extends String, ? extends SQLFunction> getFunctions();

    String quote(String name);

    void registerColumnType(int code, String name);

    char openQuote();

    char closeQuote();

    int getMaxAliasLength();

    String getTypeName(int sqlTypeCode, int length, int precision, int scale);

    boolean qualifyIndexName();

    String getAddForeignKeyConstraintString(String constraintName, String keyDefinition);

    String getAddForeignKeyConstraintString(String constraintName, String[] columnNames, String qualifiedName,
            String[] referencedColumnNames, boolean referenceToPrimaryKey);

    String getDropForeignKeyString();

    boolean supportsIfExistsBeforeConstraintName();

    boolean supportsIfExistsAfterConstraintName();

    String getAddPrimaryKeyConstraintString(String constraintName);

    boolean supportsCommentOn();

    boolean supportsLimit();

    boolean supportsLimitOffset();

    boolean supportsVariableLimit();

    boolean bindLimitParametersInReverseOrder();

    boolean bindLimitParametersFirst();

    boolean useMaxForLimit();

    boolean forceLimitUsage();

    String getLimitString(String sql, int i, int maxOrLimit);

    IdentityColumnSupport getIdentityColumnSupport();

    boolean supportsNoColumnsInsert();

    String getNoColumnsInsertString();

    JoinFragment createOuterJoinFragment();

    String transformSelectString(String string);
}
