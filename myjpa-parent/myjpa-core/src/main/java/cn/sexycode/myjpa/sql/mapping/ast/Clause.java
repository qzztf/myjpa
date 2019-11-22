package cn.sexycode.myjpa.sql.mapping.ast;


/**
 * Used to indicate which query clause we are currently processing
 *
 */
public enum Clause {
	/**
	 * The insert values clause
	 */
	INSERT,

	/**
	 * The update set clause
	 */
	UPDATE,

	/**
	 * Not used in 5.x.  Intended for use in 6+ as indicator
	 * of processing predicates (where clause) that occur in a
	 * delete
	 */
	DELETE,

	SELECT,
	FROM,
	WHERE,
	GROUP,
	HAVING,
	ORDER,
	LIMIT,
	CALL,

	/**
	 * Again, not used in 5.x.  Used in 6+
	 */
	IRRELEVANT

}
