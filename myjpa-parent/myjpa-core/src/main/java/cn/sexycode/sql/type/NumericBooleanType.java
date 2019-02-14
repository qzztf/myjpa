/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.BooleanTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.IntegerTypeDescriptor;

import java.io.Serializable;

/**
 * A type that maps between {@link java.sql.Types#INTEGER INTEGER} and {@link Boolean} (using 1 and 0)
 *
 * @author Steve Ebersole
 */
public class NumericBooleanType
        extends AbstractSingleColumnStandardBasicType<Boolean>
        implements PrimitiveType<Boolean>, DiscriminatorType<Boolean> {

    public static final NumericBooleanType INSTANCE = new NumericBooleanType();

    public NumericBooleanType() {
        super(IntegerTypeDescriptor.INSTANCE, BooleanTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "numeric_boolean";
    }

    @Override
    public Class getPrimitiveClass() {
        return boolean.class;
    }

    @Override
    public Serializable getDefaultValue() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean stringToObject(String string) {
        return fromString(string);
    }

    @Override
    public String objectToSQLString(Boolean value, Dialect dialect) {
        return value ? "1" : "0";
    }
}
