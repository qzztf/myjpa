package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.DiscriminatorType;
import cn.sexycode.myjpa.sql.type.PrimitiveType;
import cn.sexycode.myjpa.sql.type.StringType;
import cn.sexycode.myjpa.sql.type.descriptor.java.BooleanTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.CharTypeDescriptor;

import java.io.Serializable;

/**
 * A type that maps between {@link java.sql.Types#CHAR CHAR(1)} and {@link Boolean} (using 'Y' and 'N')
 *
 */
public class YesNoType
        extends AbstractSingleColumnStandardBasicType<Boolean>
        implements PrimitiveType<Boolean>, DiscriminatorType<Boolean> {

    public static final YesNoType INSTANCE = new YesNoType();

    public YesNoType() {
        super(CharTypeDescriptor.INSTANCE, BooleanTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "yes_no";
    }

    @Override
    public Class getPrimitiveClass() {
        return boolean.class;
    }

    @Override
    public Boolean stringToObject(String xml) throws Exception {
        return fromString(xml);
    }

    @Override
    public Serializable getDefaultValue() {
        return Boolean.FALSE;
    }

    @Override
    public String objectToSQLString(Boolean value, Dialect dialect) throws Exception {
        return StringType.INSTANCE.objectToSQLString(value ? "Y" : "N", dialect);
    }
}
