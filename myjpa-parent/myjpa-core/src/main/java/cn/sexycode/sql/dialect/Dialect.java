package cn.sexycode.sql.dialect;

import cn.sexycode.sql.LockMode;
import cn.sexycode.sql.LockOptions;
import cn.sexycode.sql.dialect.function.SQLFunction;
import cn.sexycode.sql.dialect.identity.IdentityColumnSupport;
import cn.sexycode.sql.sql.JoinFragment;

import java.util.Map;
import java.util.Set;

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

    String toBooleanValueString(Boolean value);

    Map<? extends String, ? extends SQLFunction> getFunctions();

    String quote(String name);

    boolean forUpdateOfColumns();

    String getForUpdateString(LockOptions lockOptions);

    String getForUpdateString(String toString, LockOptions lockOptions);

    String getForUpdateString(LockMode lockMode);

    String getForUpdateNowaitString(String aliases);

    String getForUpdateSkipLockedString(String aliases);

    String getForUpdateString(String aliases);

    boolean supportsNoColumnsInsert();

    String getNoColumnsInsertString();

    IdentityColumnSupport getIdentityColumnSupport();

    JoinFragment createOuterJoinFragment();

    String transformSelectString(String toString);

    boolean requiresCastingOfParametersInSelectClause();

    String cast(String s, int jdbcTypeCode, int length);

    String cast(String s, int jdbcTypeCode, int precision, int scale);

    String appendLockHint(LockOptions lockOptions, String tableName);

    String applyLocksToSql(String buf, LockOptions lockOptions, Object o);

    char openQuote();

    char closeQuote();

    Set<String> getKeywords();

    boolean isTypeNameRegistered(String lcToken);

    boolean qualifyIndexName();

    int getMaxAliasLength();

    String getTypeName(int sqlTypeCode, int length, int precision, int scale);
}
