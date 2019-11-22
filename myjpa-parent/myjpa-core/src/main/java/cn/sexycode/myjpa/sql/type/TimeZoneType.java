package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.LiteralType;
import cn.sexycode.myjpa.sql.type.StringType;
import cn.sexycode.myjpa.sql.type.descriptor.java.TimeZoneTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.TimeZone;

/**
 * A type mapping {@link java.sql.Types#VARCHAR VARCHAR} and {@link TimeZone}
 *
 */
public class TimeZoneType
        extends AbstractSingleColumnStandardBasicType<TimeZone>
        implements LiteralType<TimeZone> {

    public static final TimeZoneType INSTANCE = new TimeZoneType();

    public TimeZoneType() {
        super(VarcharTypeDescriptor.INSTANCE, TimeZoneTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "timezone";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    public String objectToSQLString(TimeZone value, Dialect dialect) throws Exception {
        return StringType.INSTANCE.objectToSQLString(value.getID(), dialect);
    }

}
