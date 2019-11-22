package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.Type;

/**
 * Additional contract for a {@link cn.sexycode.myjpa.sql.type.Type} may be used for a discriminator.  THis contract is used to process
 * the string representation as presented in metadata, especially in <tt>XML</tt> files.
 *
 */
public interface IdentifierType<T> extends Type {

    /**
     * Convert the value from the mapping file to a Java object.
     *
     * @param xml the value of <tt>discriminator-value</tt> or <tt>unsaved-value</tt> attribute
     * @return The converted value of the string representation.
     * @throws Exception Indicates a problem converting from the string
     */
    public T stringToObject(String xml) throws Exception;

}
