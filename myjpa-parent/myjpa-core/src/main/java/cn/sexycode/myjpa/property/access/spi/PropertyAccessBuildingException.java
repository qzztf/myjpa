package cn.sexycode.myjpa.property.access.spi;

import cn.sexycode.myjpa.MyjpaException;

/**
 * Indicates a problem while building a PropertyAccess
 *
 * @author Steve Ebersole
 */
public class PropertyAccessBuildingException extends MyjpaException {
	public PropertyAccessBuildingException(String message) {
		super( message );
	}

	public PropertyAccessBuildingException(String message, Throwable cause) {
		super( message, cause );
	}
}
