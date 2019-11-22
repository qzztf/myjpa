package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.UserType;

/**
 * A custom type that may function as an identifier or discriminator type
 *
 */
public interface EnhancedUserType extends UserType {
    /**
     * Return an SQL literal representation of the value
     */
    String objectToSQLString(Object value);
}
