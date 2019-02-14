/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.binding;

import javax.persistence.PersistenceException;

/**
 * The base exception type for Hibernate exceptions.
 * <p/>
 * Note that all {@link java.sql.SQLException SQLExceptions} will be wrapped in some form of
 * {@link JDBCException}.
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
