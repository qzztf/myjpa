package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.DiscriminatorType;
import cn.sexycode.myjpa.sql.type.StringType;
import cn.sexycode.myjpa.sql.type.descriptor.java.UrlTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarcharTypeDescriptor;

import java.net.URL;

/**
 * A type that maps between {@link java.sql.Types#VARCHAR VARCHAR} and {@link URL}
 *
 * @author qzz
 */
public class UrlType extends AbstractSingleColumnStandardBasicType<URL> implements DiscriminatorType<URL> {
    public static final UrlType INSTANCE = new UrlType();

    public UrlType() {
        super(VarcharTypeDescriptor.INSTANCE, UrlTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "url";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String toString(URL value) {
        return UrlTypeDescriptor.INSTANCE.toString(value);
    }

    public String objectToSQLString(URL value, Dialect dialect) throws Exception {
        return StringType.INSTANCE.objectToSQLString(toString(value), dialect);
    }

    public URL stringToObject(String xml) throws Exception {
        return UrlTypeDescriptor.INSTANCE.fromString(xml);
    }
}
