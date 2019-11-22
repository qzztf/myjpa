package cn.sexycode.myjpa.sql.jdbc;

import cn.sexycode.util.core.service.Service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A contract for obtaining JDBC connections.
 * <p/>
 * Implementors might also implement connection pooling.
 * <p/>
 * Implementors should provide a public default constructor.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface ConnectionProvider extends Service {
    /**
     * Obtains a connection for Hibernate use according to the underlying strategy of this provider.
     *
     * @return The obtained JDBC connection
     * @throws SQLException                     Indicates a problem opening a connection
     * @throws org.hibernate.HibernateException Indicates a problem otherwise obtaining a connection.
     */
    Connection getConnection() throws SQLException;

    /**
     * Release a connection from Hibernate use.
     *
     * @param conn The JDBC connection to release
     * @throws SQLException                     Indicates a problem closing the connection
     * @throws org.hibernate.HibernateException Indicates a problem otherwise releasing a connection.
     */
    void closeConnection(Connection conn) throws SQLException;

    /**
     * Does this connection provider support aggressive release of JDBC
     * connections and re-acquisition of those connections (if need be) later?
     * <p/>
     * This is used in conjunction with {@link org.hibernate.cfg.Environment#RELEASE_CONNECTIONS}
     * to aggressively release JDBC connections.  However, the configured ConnectionProvider
     * must support re-acquisition of the same underlying connection for that semantic to work.
     * <p/>
     * Typically, this is only true in managed environments where a container
     * tracks connections by transaction or thread.
     * <p>
     * Note that JTA semantic depends on the fact that the underlying connection provider does
     * support aggressive release.
     *
     * @return {@code true} if aggressive releasing is supported; {@code false} otherwise.
     */
    boolean supportsAggressiveRelease();
}
