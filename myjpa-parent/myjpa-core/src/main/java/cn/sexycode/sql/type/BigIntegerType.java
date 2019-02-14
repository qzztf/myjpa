/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.BigIntegerTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.NumericTypeDescriptor;

import java.math.BigInteger;

/**
 * A type that maps between a {@link java.sql.Types#NUMERIC NUMERIC} and {@link BigInteger}.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class BigIntegerType
        extends AbstractSingleColumnStandardBasicType<BigInteger>
        implements DiscriminatorType<BigInteger> {

    public static final BigIntegerType INSTANCE = new BigIntegerType();

    public BigIntegerType() {
        super(NumericTypeDescriptor.INSTANCE, BigIntegerTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "big_integer";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String objectToSQLString(BigInteger value, Dialect dialect) {
        return BigIntegerTypeDescriptor.INSTANCE.toString(value);
    }

    @Override
    public BigInteger stringToObject(String string) {
        return BigIntegerTypeDescriptor.INSTANCE.fromString(string);
    }
}
