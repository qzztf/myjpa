/*******************************************************************************
 * Copyright (c) 2008 - 2013 Oracle Corporation. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Linda DeMichiel - Java Persistence 2.1
 *     Linda DeMichiel - Java Persistence 2.0
 *
 ******************************************************************************/
package cn.sexycode.sql.dialect;


import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.QueryTimeoutException;

/**
 * Thrown by the persistence provider when a problem occurs.
 * All instances of <code>PersistenceException</code> except for instances of
 * {@link NoResultException}, {@link NonUniqueResultException},
 * {@link LockTimeoutException}, and {@link QueryTimeoutException} will cause
 * the current transaction, if one is active and the persistence context has
 * been joined to it, to be marked for rollback.
 *
 * @since Java Persistence 1.0
 */
public class DialectException extends RuntimeException {

    /**
     * Constructs a new <code>PersistenceException</code> exception
     * with <code>null</code> as its detail message.
     */
    public DialectException() {
        super();
    }

    /**
     * Constructs a new <code>PersistenceException</code> exception
     * with the specified detail message.
     *
     * @param message the detail message.
     */
    public DialectException(String message) {
        super(message);
    }

    /**
     * Constructs a new <code>PersistenceException</code> exception
     * with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public DialectException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new <code>PersistenceException</code> exception
     * with the specified cause.
     *
     * @param cause the cause.
     */
    public DialectException(Throwable cause) {
        super(cause);
    }
}

