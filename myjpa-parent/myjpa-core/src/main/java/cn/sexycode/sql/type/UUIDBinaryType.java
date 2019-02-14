/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.sql.type.descriptor.java.UUIDTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.BinaryTypeDescriptor;

import java.util.UUID;

/**
 * A type mapping {@link java.sql.Types#BINARY} and {@link UUID}
 *
 * @author Steve Ebersole
 */
public class UUIDBinaryType extends AbstractSingleColumnStandardBasicType<UUID> {
    public static final UUIDBinaryType INSTANCE = new UUIDBinaryType();

    public UUIDBinaryType() {
        super(BinaryTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "uuid-binary";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }
}
