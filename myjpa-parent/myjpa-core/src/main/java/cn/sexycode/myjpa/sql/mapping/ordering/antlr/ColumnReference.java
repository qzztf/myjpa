package cn.sexycode.myjpa.sql.mapping.ordering.antlr;

import cn.sexycode.myjpa.sql.mapping.ordering.antlr.SqlValueReference;

/**
 * Reference to a column name.
 *
 */
public interface ColumnReference extends SqlValueReference {
	/**
	 * Retrieve the column name.
	 *
	 * @return THe column name
	 */
	public String getColumnName();
}
