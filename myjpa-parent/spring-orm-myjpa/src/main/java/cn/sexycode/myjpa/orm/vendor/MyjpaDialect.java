/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sexycode.myjpa.orm.vendor;

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
    @Nullable
    public Object beginTransaction(EntityManager entityManager, TransactionDefinition definition)
            throws PersistenceException, SQLException, TransactionException {
        entityManager.getTransaction().begin();
        return null;
    }

    @Override
    public ConnectionHandle getJdbcConnection(EntityManager entityManager, boolean readOnly)
            throws PersistenceException, SQLException {

        // As of Spring 4.1.2, we're using a custom ConnectionHandle for lazy retrieval
        // of the underlying Connection (allowing for deferred internal transaction begin
        // within the EclipseLink EntityManager)
        return new MyJpaConnectionHandle(entityManager);
    }


    /**
     * {@link ConnectionHandle} implementation that lazily fetches an
     * EclipseLink-provided Connection on the first {@code getConnection} call -
     * which may never come if no application code requests a JDBC Connection.
     * This is useful to defer the early transaction begin that obtaining a
     * JDBC Connection implies within an EclipseLink EntityManager.
     */
    private static class MyJpaConnectionHandle implements ConnectionHandle {

        private final EntityManager entityManager;

        @Nullable
        private Connection connection;

        public MyJpaConnectionHandle(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        @Override
        public Connection getConnection() {
            /*if (this.connection == null) {
                this.connection = this.entityManager.get(Connection.class);
            }*/
            return this.connection;
        }

        @Override
        public void releaseConnection(Connection con) {
        }
    }

}
