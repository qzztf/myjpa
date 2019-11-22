package cn.sexycode.myjpa.sql.mapping.ordering.antlr;

/**
 * Given a column reference, resolve the table alias to apply to the column to qualify it.
 */
public interface OrderByAliasResolver {
	/**
	 * Given a column reference, resolve the table alias to apply to the column to qualify it.
	 *
	 */
	public String resolveTableAlias(String columnReference);
}
