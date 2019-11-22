package cn.sexycode.myjpa.sql.type;

import java.util.Properties;

/**
 * Support for parameterizable types. A UserType or CustomUserType may be
 * made parameterizable by implementing this interface. Parameters for a
 * type may be set by using a nested type element for the property element
 * in the mapping file, or by defining a typedef.
 *
 */
public interface ParameterizedType {

    /**
     * Gets called by Hibernate to pass the configured type parameters to
     * the implementation.
     */
    void setParameterValues(Properties parameters);
}
