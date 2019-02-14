/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.sql.type.descriptor.java.CalendarTimeTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.TimeTypeDescriptor;

import java.util.Calendar;

/**
 * A type mapping {@link java.sql.Types#TIME TIME} and {@link Calendar}.
 * <p/>
 * For example, a Calendar attribute annotated with {@link javax.persistence.Temporal} and specifying
 * {@link javax.persistence.TemporalType#TIME}
 *
 * @author Steve Ebersole
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
