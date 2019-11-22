package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.descriptor.java.CurrencyTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.Currency;

/**
 * A type that maps between {@link java.sql.Types#VARCHAR VARCHAR} and {@link Currency}
 *
 */
public class CurrencyType
        extends AbstractSingleColumnStandardBasicType<Currency>
        implements LiteralType<Currency> {

    public static final CurrencyType INSTANCE = new CurrencyType();

    public CurrencyType() {
        super(VarcharTypeDescriptor.INSTANCE, CurrencyTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "currency";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String objectToSQLString(Currency value, Dialect dialect) throws Exception {
        return "\'" + toString(value) + "\'";
    }
}
