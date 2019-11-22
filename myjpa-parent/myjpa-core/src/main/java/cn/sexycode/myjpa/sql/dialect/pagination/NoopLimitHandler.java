package cn.sexycode.myjpa.sql.dialect.pagination;

import cn.sexycode.myjpa.sql.dialect.pagination.AbstractLimitHandler;
import cn.sexycode.myjpa.sql.dialect.pagination.LimitHelper;
import cn.sexycode.myjpa.sql.model.RowSelection;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Handler not supporting query LIMIT clause. JDBC API is used to set maximum number of returned rows.
 *
 */
public class NoopLimitHandler extends AbstractLimitHandler {

    public static final NoopLimitHandler INSTANCE = new NoopLimitHandler();

    private NoopLimitHandler() {
        // NOP
    }

    @Override
    public String processSql(String sql, RowSelection selection) {
        return sql;
    }

    @Override
    public int bindLimitParametersAtStartOfQuery(RowSelection selection, PreparedStatement statement, int index) {
        return 0;
    }

    @Override
    public int bindLimitParametersAtEndOfQuery(RowSelection selection, PreparedStatement statement, int index) {
        return 0;
    }

    @Override
    public void setMaxRows(RowSelection selection, PreparedStatement statement) throws SQLException {
        if (cn.sexycode.myjpa.sql.dialect.pagination.LimitHelper.hasMaxRows(selection)) {
            int maxRows = selection.getMaxRows() + convertToFirstRowValue(LimitHelper.getFirstRow(selection));
            // Use Integer.MAX_VALUE on overflow
            if (maxRows < 0) {
                statement.setMaxRows(Integer.MAX_VALUE);
            } else {
                statement.setMaxRows(maxRows);
            }
        }
    }
}
