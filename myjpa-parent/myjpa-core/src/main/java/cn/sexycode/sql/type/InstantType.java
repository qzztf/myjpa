/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.InstantJavaDescriptor;
import cn.sexycode.sql.type.descriptor.sql.TimestampTypeDescriptor;
import cn.sexycode.sql.util.ComparableComparator;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

/**
 * A type that maps between {@link java.sql.Types#TIMESTAMP TIMESTAMP} and {@link java.time.LocalDateTime}.
 *
 * @author Steve Ebersole
 */
public class InstantType
        extends AbstractSingleColumnStandardBasicType<Instant>
        implements VersionType<Instant>, LiteralType<Instant> {
    /**
     * Singleton access
     */
    public static final InstantType INSTANCE = new InstantType();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S 'Z'", Locale.ENGLISH);

    public InstantType() {
        super(TimestampTypeDescriptor.INSTANCE, InstantJavaDescriptor.INSTANCE);
    }

    @Override
    public String objectToSQLString(Instant value, Dialect dialect) throws Exception {
        return "{ts '" + FORMATTER.format(ZonedDateTime.ofInstant(value, ZoneId.of("UTC"))) + "'}";
    }


    @Override
    @SuppressWarnings("unchecked")
    public Comparator<Instant> getComparator() {
        return ComparableComparator.INSTANCE;
    }

    @Override
    public String getName() {
        return Instant.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
