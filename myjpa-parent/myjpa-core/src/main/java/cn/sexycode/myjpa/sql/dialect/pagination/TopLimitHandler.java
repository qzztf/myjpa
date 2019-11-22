package cn.sexycode.myjpa.sql.dialect.pagination;

import cn.sexycode.myjpa.sql.dialect.pagination.AbstractLimitHandler;
import cn.sexycode.myjpa.sql.dialect.pagination.LimitHelper;
import cn.sexycode.myjpa.sql.model.RowSelection;

import java.util.Locale;



/**
 */
public class TopLimitHandler extends AbstractLimitHandler {

    private final boolean supportsVariableLimit;

    private final boolean bindLimitParametersFirst;

    public TopLimitHandler(boolean supportsVariableLimit, boolean bindLimitParametersFirst) {
        this.supportsVariableLimit = supportsVariableLimit;
        this.bindLimitParametersFirst = bindLimitParametersFirst;
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean useMaxForLimit() {
        return true;
    }

    @Override
    public boolean supportsLimitOffset() {
        return supportsVariableLimit;
    }

    @Override
    public boolean supportsVariableLimit() {
        return supportsVariableLimit;
    }

    @Override
    public boolean bindLimitParametersFirst() {
        return bindLimitParametersFirst;
    }

    @Override
    public String processSql(String sql, RowSelection selection) {
        if (LimitHelper.hasFirstRow(selection)) {
            throw new UnsupportedOperationException("query result offset is not supported");
        }

        final int selectIndex = sql.toLowerCase(Locale.ROOT).indexOf("select");
        final int selectDistinctIndex = sql.toLowerCase(Locale.ROOT).indexOf("select distinct");
        final int insertionPoint = selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);

        StringBuilder sb = new StringBuilder(sql.length() + 8)
                .append(sql);

        if (supportsVariableLimit) {
            sb.insert(insertionPoint, " TOP ? ");
        } else {
            sb.insert(insertionPoint, " TOP " + getMaxOrLimit(selection) + " ");
        }

        return sb.toString();
    }
}
