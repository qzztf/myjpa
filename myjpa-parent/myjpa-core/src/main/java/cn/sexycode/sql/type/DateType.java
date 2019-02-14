/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.JdbcDateTypeDescriptor;

import java.util.Date;

/**
 * A type that maps between {@link java.sql.Types#DATE DATE} and {@link java.sql.Date}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class DateType
        extends AbstractSingleColumnStandardBasicType<Date>
        implements IdentifierType<Date>, LiteralType<Date> {

    public static final DateType INSTANCE = new DateType();

    public DateType() {
        super(cn.sexycode.sql.type.descriptor.sql.DateTypeDescriptor.INSTANCE, JdbcDateTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "date";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{
                getName(),
                java.sql.Date.class.getName()
        };
    }

//	@Override
//	protected boolean registerUnderJavaType() {
//		return true;
//	}

    @Override
    public String objectToSQLString(Date value, Dialect dialect) throws Exception {
        final java.sql.Date jdbcDate = java.sql.Date.class.isInstance(value)
                ? (java.sql.Date) value
                : new java.sql.Date(value.getTime());
        // TODO : use JDBC date literal escape syntax? -> {d 'date-string'} in yyyy-mm-dd format
        return StringType.INSTANCE.objectToSQLString(jdbcDate.toString(), dialect);
    }

    @Override
    public Date stringToObject(String xml) {
        return fromString(xml);
    }
}
