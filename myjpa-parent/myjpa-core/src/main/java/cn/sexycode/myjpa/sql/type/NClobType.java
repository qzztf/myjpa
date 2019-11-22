package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.NClobTypeDescriptor;

import java.sql.NClob;

/**
 * A type that maps between {@link java.sql.Types#CLOB CLOB} and {@link java.sql.Clob}
 *
 */
public class NClobType extends AbstractSingleColumnStandardBasicType<NClob> {
    public static final NClobType INSTANCE = new NClobType();

    public NClobType() {
        super(cn.sexycode.myjpa.sql.type.descriptor.sql.NClobTypeDescriptor.DEFAULT, NClobTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "nclob";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }


}
