package cn.sexycode.myjpa;

import javax.persistence.PersistenceException;

/**
 * The base exception type for Hibernate exceptions.
 * <p/>
 * Note that all {@link java.sql.SQLException SQLExceptions} will be wrapped in some form of
 * {@link PersistenceException}.
 *
 */
public class MyjpaException extends PersistenceException {
    /**
     * Constructs a MyjpaException using the given exception message.
     *
     * @param message The message explaining the reason for the exception
     */
    public MyjpaException(String message) {
        super(message);
    }

    /**
     * Constructs a MyjpaException using the given message and underlying cause.
     *
     * @param cause The underlying cause.
     */
    public MyjpaException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a MyjpaException using the given message and underlying cause.
     *
     * @param message The message explaining the reason for the exception.
     * @param cause   The underlying cause.
     */
    public MyjpaException(String message, Throwable cause) {
        super(message, cause);
    }
}
