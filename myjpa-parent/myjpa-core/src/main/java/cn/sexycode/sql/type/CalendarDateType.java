/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.sql.type.descriptor.java.CalendarDateTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.DateTypeDescriptor;

import java.util.Calendar;

/**
 * A type mapping {@link java.sql.Types#DATE DATE} and {@link Calendar}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class CalendarDateType extends AbstractSingleColumnStandardBasicType<Calendar> {
    public static final CalendarDateType INSTANCE = new CalendarDateType();

    public CalendarDateType() {
        super(DateTypeDescriptor.INSTANCE, CalendarDateTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "calendar_date";
    }

}
