/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.sql.type.descriptor.java.PrimitiveByteArrayTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.VarbinaryTypeDescriptor;

import java.util.Comparator;

/**
 * A type that maps between a {@link java.sql.Types#VARBINARY VARBINARY} and {@code byte[]}
 * <p>
 * Implementation of the {@link VersionType} interface should be considered deprecated.
 * For binary entity versions/timestamps, {@link RowVersionType} should be used instead.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class BinaryType
        extends AbstractSingleColumnStandardBasicType<byte[]>
        implements VersionType<byte[]> {

    public static final BinaryType INSTANCE = new BinaryType();

    @Override
    public String getName() {
        return "binary";
    }

    public BinaryType() {
        super(VarbinaryTypeDescriptor.INSTANCE, PrimitiveByteArrayTypeDescriptor.INSTANCE);
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), "byte[]", byte[].class.getName()};
    }


    /**
     * Get a comparator for version values.
     *
     * @return The comparator to use to compare different version values.
     * @deprecated use {@link RowVersionType} for binary entity versions/timestamps
     */
    @Override
    @Deprecated
    public Comparator<byte[]> getComparator() {
        return PrimitiveByteArrayTypeDescriptor.INSTANCE.getComparator();
    }
}
