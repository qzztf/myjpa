package cn.sexycode.myjpa.sql.mapping.ordering.antlr;

import cn.sexycode.myjpa.sql.mapping.ordering.antlr.ColumnMapper;
import cn.sexycode.myjpa.sql.mapping.ordering.antlr.ColumnReference;
import cn.sexycode.myjpa.sql.mapping.ordering.antlr.FormulaReference;

/**
 * Unifying interface between column and formula references mainly to give more strictly typed result
 * to {@link ColumnMapper#map(String)}
 *
 * @see ColumnReference
 * @see FormulaReference
 *
 */
public interface SqlValueReference {
}
