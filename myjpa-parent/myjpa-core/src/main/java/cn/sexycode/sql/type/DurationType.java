/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.DurationJavaDescriptor;
import cn.sexycode.sql.type.descriptor.sql.BigIntTypeDescriptor;

import java.time.Duration;

/**
 * @author Steve Ebersole
 */
public class DurationType
        extends AbstractSingleColumnStandardBasicType<Duration>
        implements LiteralType<Duration> {
    /**
     * Singleton access
     */
    public static final DurationType INSTANCE = new DurationType();

    public DurationType() {
        super(BigIntTypeDescriptor.INSTANCE, DurationJavaDescriptor.INSTANCE);
    }

    @Override
    public String objectToSQLString(Duration value, Dialect dialect) throws Exception {
        return String.valueOf(value.toNanos());
    }

    @Override
    public String getName() {
        return Duration.class.getSimpleName();
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
