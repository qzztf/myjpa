package cn.sexycode.myjpa.sql.dialect;

/**
 * This interface defines how various MySQL storage engines behave in regard to Hibernate functionality.
 *
 */
public interface MySQLStorageEngine {

    boolean supportsCascadeDelete();

    String getTableTypeString(String engineKeyword);

    boolean hasSelfReferentialForeignKeyBug();

    boolean dropConstraints();
}
