/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.sql.type.descriptor.java.BigDecimalTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.NumericTypeDescriptor;

import java.math.BigDecimal;

/**
 * A type that maps between a {@link java.sql.Types#NUMERIC NUMERIC} and {@link BigDecimal}.
 *
 * @author Gavin King
 * @author Steve Ebersole
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
