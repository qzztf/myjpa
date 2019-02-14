/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.LocalDateTimeJavaDescriptor;
import cn.sexycode.sql.type.descriptor.sql.TimestampTypeDescriptor;
import cn.sexycode.sql.util.ComparableComparator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

/**
 * A type that maps between {@link java.sql.Types#TIMESTAMP TIMESTAMP} and {@link LocalDateTime}.
 *
 * @author Steve Ebersole
 */
public class LocalDateTimeType
        extends AbstractSingleColumnStandardBasicType<LocalDateTime>
        implements VersionType<LocalDateTime>, LiteralType<LocalDateTime> {
    /**
     * Singleton access
     */
    public static final LocalDateTimeType INSTANCE = new LocalDateTimeType();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH);

    public LocalDateTimeType() {
        super(TimestampTypeDescriptor.INSTANCE, LocalDateTimeJavaDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return LocalDateTime.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String objectToSQLString(LocalDateTime value, Dialect dialect) throws Exception {
        return "{ts '" + FORMATTER.format(value) + "'}";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<LocalDateTime> getComparator() {
        return ComparableComparator.INSTANCE;
    }
}
