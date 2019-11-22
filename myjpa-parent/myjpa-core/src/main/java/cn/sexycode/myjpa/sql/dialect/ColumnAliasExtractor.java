package cn.sexycode.myjpa.sql.dialect;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Strategy for extracting the unique column alias out of a {@link ResultSetMetaData}.  This is used during the
 * "auto discovery" phase of native SQL queries.
 * <p/>
 * Generally this should be done via {@link ResultSetMetaData#getColumnLabel}, but not all drivers do this correctly.
 *
 */
public interface ColumnAliasExtractor {
    /**
     * Extract the unique column alias.
     *
     * @param metaData The result set metadata
     * @param position The column position
     * @return The alias
     * @throws SQLException Indicates a problem accessing the JDBC ResultSetMetaData
     */
    public String extractColumnAlias(ResultSetMetaData metaData, int position) throws SQLException;

    /**
     * An extractor which uses {@link ResultSetMetaData#getColumnLabel}
     */
    public static final ColumnAliasExtractor COLUMN_LABEL_EXTRACTOR = new ColumnAliasExtractor() {
        @Override
        public String extractColumnAlias(ResultSetMetaData metaData, int position) throws SQLException {
            return metaData.getColumnLabel(position);
        }
    };

    /**
     * An extractor which uses {@link ResultSetMetaData#getColumnName}
     */
    @SuppressWarnings("UnusedDeclaration")
    public static final ColumnAliasExtractor COLUMN_NAME_EXTRACTOR = new ColumnAliasExtractor() {
        @Override
        public String extractColumnAlias(ResultSetMetaData metaData, int position) throws SQLException {
            return metaData.getColumnName(position);
        }
    };
}
