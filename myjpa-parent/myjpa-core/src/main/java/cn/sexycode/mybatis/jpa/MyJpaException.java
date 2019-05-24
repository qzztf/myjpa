package cn.sexycode.mybatis.jpa;

import javax.persistence.PersistenceException;

/**
 * The base exception type for Hibernate exceptions.
 * <p/>
 * Note that all {@link java.sql.SQLException SQLExceptions} will be wrapped in some form of
 * {@link PersistenceException}.
 *
 * @author Gavin King
 */
public class MyJpaException extends PersistenceException {
    /**
     * Constructs a MyJpaException using the given exception message.
     *
     * @param message The message explaining the reason for the exception
     */
    public MyJpaException(String message) {
        super(message);
    }

    /**
     * Constructs a MyJpaException using the given message and underlying cause.
     *
     * @param cause The underlying cause.
     */
    public MyJpaException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a MyJpaException using the given message and underlying cause.
     *
     * @param message The message explaining the reason for the exception.
     * @param cause   The underlying cause.
     */
    public MyJpaException(String message, Throwable cause) {
        super(message, cause);
    }
}
