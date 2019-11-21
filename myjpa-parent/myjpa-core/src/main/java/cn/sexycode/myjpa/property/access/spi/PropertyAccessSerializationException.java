package cn.sexycode.myjpa.property.access.spi;

import cn.sexycode.myjpa.MyjpaException;

/**
 * @author Steve Ebersole
 */
public class PropertyAccessSerializationException extends MyjpaException {
	public PropertyAccessSerializationException(String message) {
		super( message );
	}

	public PropertyAccessSerializationException(String message, Throwable cause) {
		super( message, cause );
	}
}
