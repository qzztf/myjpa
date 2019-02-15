/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.sql.type.descriptor.java.CurrencyTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.Currency;

/**
 * A type that maps between {@link java.sql.Types#VARCHAR VARCHAR} and {@link Currency}
 *
 * @author Gavin King
 * @author Steve Ebersole
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
