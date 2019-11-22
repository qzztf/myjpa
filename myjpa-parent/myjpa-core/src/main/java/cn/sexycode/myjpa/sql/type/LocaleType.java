package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.StringType;
import cn.sexycode.myjpa.sql.type.descriptor.java.LocaleTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.Locale;

/**
 * A type that maps between {@link java.sql.Types#VARCHAR VARCHAR} and @link Locale}
 *
 */
public class LocaleType extends AbstractSingleColumnStandardBasicType<Locale>
        implements LiteralType<Locale> {

    public static final LocaleType INSTANCE = new LocaleType();

    public LocaleType() {
        super(VarcharTypeDescriptor.INSTANCE, LocaleTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "locale";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String objectToSQLString(Locale value, Dialect dialect) throws Exception {
        return StringType.INSTANCE.objectToSQLString(toString(value), dialect);
    }
}
