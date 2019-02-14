/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.LocaleTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.Locale;

/**
 * A type that maps between {@link java.sql.Types#VARCHAR VARCHAR} and @link Locale}
 *
 * @author Gavin King
 * @author Steve Ebersole
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
