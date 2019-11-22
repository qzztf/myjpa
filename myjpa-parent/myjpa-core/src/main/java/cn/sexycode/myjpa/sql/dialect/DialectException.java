package cn.sexycode.myjpa.sql.dialect;



/**
 * Thrown by the persistence provider when a problem occurs.
 * All instances of <code>PersistenceException</code> except for instances of
 * {@link NoResultException}, {@link NonUniqueResultException},
 * {@link LockTimeoutException}, and {@link QueryTimeoutException} will cause
 * the current transaction, if one is active and the persistence context has
 * been joined to it, to be marked for rollback.
 *
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

