package cn.sexycode.sql.dialect;

import cn.sexycode.sql.dialect.function.SQLFunction;

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

    String toBooleanValueString(Boolean value);

    Map<? extends String, ? extends SQLFunction> getFunctions();

    String quote(String name);
}
