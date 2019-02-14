/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.sql.type.descriptor.java.BlobTypeDescriptor;

import java.sql.Blob;

/**
 * A type that maps between {@link java.sql.Types#BLOB BLOB} and {@link Blob}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class BlobType extends AbstractSingleColumnStandardBasicType<Blob> {
    public static final BlobType INSTANCE = new BlobType();

    public BlobType() {
        super(cn.sexycode.sql.type.descriptor.sql.BlobTypeDescriptor.DEFAULT, BlobTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "blob";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }


}
