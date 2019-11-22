package cn.sexycode.myjpa.sql.dialect.hint;

/**
 * Contract defining how query hints get applied.
 *
 */
public interface QueryHintHandler {

    /**
     * Add query hints to the given query.
     *
     * @param query original query
     * @param hints hints to be applied
     * @return query with hints
     */
    String addQueryHints(String query, String hints);
}
