package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.DiscriminatorType;
import cn.sexycode.myjpa.sql.type.descriptor.java.BigIntegerTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NumericTypeDescriptor;

import java.math.BigInteger;

/**
 * A type that maps between a {@link java.sql.Types#NUMERIC NUMERIC} and {@link BigInteger}.
 *
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
