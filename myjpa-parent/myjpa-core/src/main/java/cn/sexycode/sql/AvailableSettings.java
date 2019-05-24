package cn.sexycode.sql;

public interface AvailableSettings {
    String DIALECT = "hibernate.dialect";
    /**
     * Gives the JDBC driver a hint as to the number of rows that should be fetched from the database
     * when more rows are needed. If <tt>0</tt>, JDBC driver default settings will be used.
     */
    String STATEMENT_FETCH_SIZE = "hibernate.jdbc.fetch_size";

    /**
     * Maximum JDBC batch size. A nonzero value enables batch updates.
     */
    String STATEMENT_BATCH_SIZE = "hibernate.jdbc.batch_size";
}
