package cn.sexycode.myjpa.sql.mapping.ordering.antlr;

import cn.sexycode.myjpa.sql.MysqlException;
import cn.sexycode.myjpa.sql.mapping.ordering.antlr.SqlValueReference;

/**
 * Contract for mapping a (an assumed) property reference to its columns.
 *
 */
public interface ColumnMapper {
	/**
	 * Resolve the property reference to its underlying columns.
	 *
	 * @param reference The property reference name.
	 *
	 * @return References to the columns/formulas that define the value mapping for the given property, or null
	 * if the property reference is unknown.
	 *
     * @throws MysqlException Generally indicates that the property reference is unknown; interpretation
	 * should be the same as a null return.
	 */
    public SqlValueReference[] map(String reference) throws MysqlException;
}
