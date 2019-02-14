package cn.sexycode.sql.type;

/**
 * @author qinzaizhen
 */
public class TypeException extends RuntimeException {
    /**
     * Constructs a MappingException using the given information.
     *
     * @param message A message explaining the exception condition
     * @param cause   The underlying cause
     */
    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a MappingException using the given information.
     *
     * @param cause The underlying cause
     */
    public TypeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a MappingException using the given information.
     *
     * @param message A message explaining the exception condition
     */
    public TypeException(String message) {
        super(message);
    }
}
