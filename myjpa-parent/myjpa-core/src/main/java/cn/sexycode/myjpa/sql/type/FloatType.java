package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.PrimitiveType;
import cn.sexycode.myjpa.sql.type.descriptor.java.FloatTypeDescriptor;

import java.io.Serializable;

/**
 * A type that maps between {@link java.sql.Types#FLOAT FLOAT} and {@link Float}
 *
 * @author qzz
 * @author qzz
 */
public class FloatType extends AbstractSingleColumnStandardBasicType<Float> implements PrimitiveType<Float> {
    public static final FloatType INSTANCE = new FloatType();

    public static final Float ZERO = 0.0f;

    public FloatType() {
        super(cn.sexycode.myjpa.sql.type.descriptor.sql.FloatTypeDescriptor.INSTANCE, FloatTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "float";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), float.class.getName(), Float.class.getName()};
    }

    @Override
    public Serializable getDefaultValue() {
        return ZERO;
    }

    @Override
    public Class getPrimitiveClass() {
        return float.class;
    }

    @Override
    public String objectToSQLString(Float value, Dialect dialect) throws Exception {
        return toString(value);
    }
}
