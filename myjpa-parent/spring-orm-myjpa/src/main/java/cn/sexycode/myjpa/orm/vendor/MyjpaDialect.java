package cn.sexycode.myjpa.orm.vendor;

import cn.sexycode.myjpa.session.Session;
import org.springframework.jdbc.datasource.ConnectionHandle;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.DefaultJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * {@link org.springframework.orm.jpa.JpaDialect} implementation for Eclipse
 * Persistence Services (EclipseLink). Developed and tested against EclipseLink 2.7;
 * backwards-compatible with EclipseLink 2.5 and 2.6 at runtime.
 *
 * <p>By default, this class acquires an early EclipseLink transaction with an early
 * JDBC Connection for non-read-only transactions. This allows for mixing JDBC and
 * JPA/EclipseLink operations in the same transaction, with cross visibility of
 * their impact. If this is not needed, set the "lazyDatabaseTransaction" flag to
 * {@code true} or consistently declare all affected transactions as read-only.
 * As of Spring 4.1.2, this will reliably avoid early JDBC Connection retrieval
 * and therefore keep EclipseLink in shared cache mode.
 *
 * @author Juergen Hoeller
 * @see #setLazyDatabaseTransaction
 * @see org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
 * @since 2.5.2
 */
@SuppressWarnings("serial")
public class MyjpaDialect extends DefaultJpaDialect {


    @Override

    public Object beginTransaction(EntityManager entityManager, TransactionDefinition definition)
            throws PersistenceException, SQLException, TransactionException {
        Session session = entityManager.unwrap(Session.class);

        if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
            //            session.getTransaction().setTimeout(definition.getTimeout());
        }

        boolean isolationLevelNeeded = (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT);
        Integer previousIsolationLevel = null;
        Connection preparedCon = null;

        if (isolationLevelNeeded || definition.isReadOnly()) {
           /* if (this.prepareConnection) {
                preparedCon = MyJpaConnectionHandle.doGetConnection(session);
                previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(preparedCon, definition);
            }
            else if (isolationLevelNeeded) {
                throw new InvalidIsolationLevelException(getClass().getSimpleName() +
                        " does not support custom isolation levels since the 'prepareConnection' flag is off.");
            }*/
        }

        // Standard JPA transaction begin call for full JPA context setup...
        entityManager.getTransaction().begin();

        // Adapt flush mode and store previous isolation level, if any.
        //        FlushMode previousFlushMode = prepareFlushMode(session, definition.isReadOnly());
        /*if (definition instanceof ResourceTransactionDefinition &&
                ((ResourceTransactionDefinition) definition).isLocalResource()) {
            // As of 5.1, we explicitly optimize for a transaction-local EntityManager,
            // aligned with native HibernateTransactionManager behavior.
            previousFlushMode = null;
            if (definition.isReadOnly()) {
                session.setDefaultReadOnly(true);
            }
        }*/
        return new SessionTransactionData(session, preparedCon, previousIsolationLevel);

    }

    @Override
    public ConnectionHandle getJdbcConnection(EntityManager entityManager, boolean readOnly)
            throws PersistenceException, SQLException {

        // As of Spring 4.1.2, we're using a custom ConnectionHandle for lazy retrieval
        // of the underlying Connection (allowing for deferred internal transaction begin
        // within the EclipseLink EntityManager)
//        return new MyjpaConnectionHandle(entityManager);
        return null;
    }

    private static class SessionTransactionData {

        private final Session session;

        @Nullable
        private final Connection preparedCon;

        @Nullable
        private final Integer previousIsolationLevel;

        public SessionTransactionData(Session session,
                @Nullable
                        Connection preparedCon,
                @Nullable
                        Integer previousIsolationLevel) {

            this.session = session;
            this.preparedCon = preparedCon;
            this.previousIsolationLevel = previousIsolationLevel;
        }

    }

    /**
     * {@link ConnectionHandle} implementation that lazily fetches an
     * EclipseLink-provided Connection on the first {@code getConnection} call -
     * which may never come if no application code requests a JDBC Connection.
     * This is useful to defer the early transaction begin that obtaining a
     * JDBC Connection implies within an EclipseLink EntityManager.
     */
    private static class MyjpaConnectionHandle implements ConnectionHandle {

        private final EntityManager entityManager;

        private Connection connection;

        public MyjpaConnectionHandle(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        @Override
        public Connection getConnection() {
            if (this.connection == null) {
                entityManager.unwrap(Session.class).getConnection();
            }
            return this.connection;
        }

        @Override
        public void releaseConnection(Connection con) {
        }
    }

}
