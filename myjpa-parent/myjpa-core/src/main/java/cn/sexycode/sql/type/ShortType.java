/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.sql.type.descriptor.java.ShortTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.SmallIntTypeDescriptor;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A type that maps between {@link java.sql.Types#SMALLINT SMALLINT} and {@link Short}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class ShortType
        extends AbstractSingleColumnStandardBasicType<Short>
        implements PrimitiveType<Short>, DiscriminatorType<Short>, VersionType<Short> {

    public static final ShortType INSTANCE = new ShortType();

    private static final Short ZERO = (short) 0;

    public ShortType() {
        super(SmallIntTypeDescriptor.INSTANCE, ShortTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "short";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), short.class.getName(), Short.class.getName()};
    }

    @Override
    public Serializable getDefaultValue() {
        return ZERO;
    }

    @Override
    public Class getPrimitiveClass() {
        return short.class;
    }

    @Override
    public String objectToSQLString(Short value, Dialect dialect) throws Exception {
        return value.toString();
    }

    @Override
    public Short stringToObject(String xml) throws Exception {
        return Short.valueOf(xml);
    }

    @Override
    public Comparator<Short> getComparator() {
        return getJavaTypeDescriptor().getComparator();
    }

}
