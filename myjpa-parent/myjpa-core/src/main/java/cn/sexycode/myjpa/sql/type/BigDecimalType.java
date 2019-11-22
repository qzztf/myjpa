package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.BigDecimalTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.NumericTypeDescriptor;

import java.math.BigDecimal;

/**
 * A type that maps between a {@link java.sql.Types#NUMERIC NUMERIC} and {@link BigDecimal}.
 *
 */
public class BigDecimalType extends AbstractSingleColumnStandardBasicType<BigDecimal> {
    public static final BigDecimalType INSTANCE = new BigDecimalType();

    public BigDecimalType() {
        super(NumericTypeDescriptor.INSTANCE, BigDecimalTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "big_decimal";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
