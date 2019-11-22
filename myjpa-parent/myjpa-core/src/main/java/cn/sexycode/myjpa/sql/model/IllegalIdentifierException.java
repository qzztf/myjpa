package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.MysqlException;

/**
 * Indicates an attempted use of a name that was deemed illegal
 *
 */
public class IllegalIdentifierException extends MysqlException {
	public IllegalIdentifierException(String s) {
		super( s );
	}
}
