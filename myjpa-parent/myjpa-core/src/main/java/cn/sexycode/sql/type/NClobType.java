/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.sql.type.descriptor.java.NClobTypeDescriptor;

import java.sql.NClob;

/**
 * A type that maps between {@link java.sql.Types#CLOB CLOB} and {@link java.sql.Clob}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class NClobType extends AbstractSingleColumnStandardBasicType<NClob> {
    public static final NClobType INSTANCE = new NClobType();

    public NClobType() {
        super(cn.sexycode.sql.type.descriptor.sql.NClobTypeDescriptor.DEFAULT, NClobTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "nclob";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }


}
