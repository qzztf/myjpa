package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.MyjpaException;

/**
 * An exception that occurs while reading mapping sources (xml/annotations),usually as a result of something
 * screwy in the O-R mappings.
 *
 * @author Gavin King
 */
public class MappingException extends MyjpaException {
    /**
     * Constructs a MappingException using the given information.
     *
     * @param message A message explaining the exception condition
     * @param cause   The underlying cause
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a MappingException using the given information.
     *
     * @param cause The underlying cause
     */
    public MappingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a MappingException using the given information.
     *
     * @param message A message explaining the exception condition
     */
    public MappingException(String message) {
        super(message);
    }

}
