/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.LocalTimeJavaDescriptor;
import cn.sexycode.sql.type.descriptor.sql.TimeTypeDescriptor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A type that maps between {@link java.sql.Types#TIMESTAMP TIMESTAMP} and {@link java.time.LocalDateTime}.
 *
 * @author Steve Ebersole
 */
public class LocalTimeType
        extends AbstractSingleColumnStandardBasicType<LocalTime>
        implements LiteralType<LocalTime> {
    /**
     * Singleton access
     */
    public static final LocalTimeType INSTANCE = new LocalTimeType();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);

    public LocalTimeType() {
        super(TimeTypeDescriptor.INSTANCE, LocalTimeJavaDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return LocalTime.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String objectToSQLString(LocalTime value, Dialect dialect) throws Exception {
        return "{t '" + FORMATTER.format(value) + "'}";
    }
}
