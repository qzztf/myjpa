package cn.sexycode.myjpa.sql.dialect.pagination;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.dialect.pagination.AbstractLimitHandler;
import cn.sexycode.myjpa.sql.dialect.pagination.LimitHelper;
import cn.sexycode.myjpa.sql.model.RowSelection;

/**
 * Limit handler that delegates all operations to the underlying dialect.
 *
 */
public class LegacyLimitHandler extends AbstractLimitHandler {
    private final Dialect dialect;

    /**
     * Constructs a LegacyLimitHandler
     *
     * @param dialect The dialect
     */
    public LegacyLimitHandler(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public boolean supportsLimit() {
        return dialect.supportsLimit();
    }

    @Override
    public boolean supportsLimitOffset() {
        return dialect.supportsLimitOffset();
    }

    @Override
    public boolean supportsVariableLimit() {
        return dialect.supportsVariableLimit();
    }

    @Override
    public boolean bindLimitParametersInReverseOrder() {
        return dialect.bindLimitParametersInReverseOrder();
    }

    @Override
    public boolean bindLimitParametersFirst() {
        return dialect.bindLimitParametersFirst();
    }

    @Override
    public boolean useMaxForLimit() {
        return dialect.useMaxForLimit();
    }

    @Override
    public boolean forceLimitUsage() {
        return dialect.forceLimitUsage();
    }



    @Override
    public String processSql(String sql, RowSelection selection) {
        final boolean useLimitOffset = supportsLimit()
                && supportsLimitOffset()
                && cn.sexycode.myjpa.sql.dialect.pagination.LimitHelper.hasFirstRow(selection)
                && cn.sexycode.myjpa.sql.dialect.pagination.LimitHelper.hasMaxRows(selection);
        return dialect.getLimitString(
                sql,
                useLimitOffset ? LimitHelper.getFirstRow(selection) : 0,
                getMaxOrLimit(selection)
        );
    }
}
