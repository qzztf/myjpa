/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.mybatis.jpa.mapping.Dialect;
import cn.sexycode.sql.type.descriptor.java.ByteTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.TinyIntTypeDescriptor;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A type that maps between {@link java.sql.Types#TINYINT TINYINT} and {@link Byte}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class ByteType
        extends AbstractSingleColumnStandardBasicType<Byte>
        implements PrimitiveType<Byte>, DiscriminatorType<Byte>, VersionType<Byte> {

    public static final ByteType INSTANCE = new ByteType();

    private static final Byte ZERO = (byte) 0;

    public ByteType() {
        super(TinyIntTypeDescriptor.INSTANCE, ByteTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "byte";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), byte.class.getName(), Byte.class.getName()};
    }

    @Override
    public Serializable getDefaultValue() {
        return ZERO;
    }

    @Override
    public Class getPrimitiveClass() {
        return byte.class;
    }

    @Override
    public String objectToSQLString(Byte value, Dialect dialect) {
        return toString(value);
    }

    @Override
    public Byte stringToObject(String xml) {
        return fromString(xml);
    }

    @Override
    public Byte fromStringValue(String xml) {
        return fromString(xml);
    }

    @Override
    public Comparator<Byte> getComparator() {
        return getJavaTypeDescriptor().getComparator();
    }
}
