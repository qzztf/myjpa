package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.CalendarTimeTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.sql.TimeTypeDescriptor;

import java.util.Calendar;

/**
 * A type mapping {@link java.sql.Types#TIME TIME} and {@link Calendar}.
 * <p/>
 * For example, a Calendar attribute annotated with {@link javax.persistence.Temporal} and specifying
 * {@link javax.persistence.TemporalType#TIME}
 *
 */
public class CalendarTimeType extends AbstractSingleColumnStandardBasicType<Calendar> {
    public static final CalendarTimeType INSTANCE = new CalendarTimeType();

    public CalendarTimeType() {
        super(TimeTypeDescriptor.INSTANCE, CalendarTimeTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "calendar_time";
    }
}
