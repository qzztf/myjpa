package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.DiscriminatorType;
import cn.sexycode.myjpa.sql.type.PrimitiveType;
import cn.sexycode.myjpa.sql.type.descriptor.java.BooleanTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;

import java.io.Serializable;

/**
 * A type that maps between {@link java.sql.Types#BOOLEAN BOOLEAN} and {@link Boolean}
 *
 */
public class BooleanType
        extends AbstractSingleColumnStandardBasicType<Boolean>
        implements PrimitiveType<Boolean>, DiscriminatorType<Boolean> {
    public static final BooleanType INSTANCE = new BooleanType();

    public BooleanType() {
        this(cn.sexycode.myjpa.sql.type.descriptor.sql.BooleanTypeDescriptor.INSTANCE, BooleanTypeDescriptor.INSTANCE);
    }

    protected BooleanType(SqlTypeDescriptor sqlTypeDescriptor, BooleanTypeDescriptor javaTypeDescriptor) {
        super(sqlTypeDescriptor, javaTypeDescriptor);
    }

    @Override
    public String getName() {
        return "boolean";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), boolean.class.getName(), Boolean.class.getName()};
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
        return dialect.toBooleanValueString(value);
    }
}
