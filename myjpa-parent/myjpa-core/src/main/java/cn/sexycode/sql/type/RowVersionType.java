/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.sql.type.descriptor.java.RowVersionTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.VarbinaryTypeDescriptor;

import java.util.Comparator;

/**
 * A type that maps between a {@link java.sql.Types#VARBINARY VARBINARY} and {@code byte[]}
 * specifically for entity versions/timestamps.
 *
 * @author Gavin King
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class RowVersionType
        extends AbstractSingleColumnStandardBasicType<byte[]>
        implements VersionType<byte[]> {

    public static final RowVersionType INSTANCE = new RowVersionType();

    @Override
    public String getName() {
        return "row_version";
    }

    public RowVersionType() {
        super(VarbinaryTypeDescriptor.INSTANCE, RowVersionTypeDescriptor.INSTANCE);
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName()};
    }

    @Override
    public Comparator<byte[]> getComparator() {
        return getJavaTypeDescriptor().getComparator();
    }
}
