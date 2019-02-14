/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.sql.type.descriptor.java.ClobTypeDescriptor;

import java.sql.Clob;

/**
 * A type that maps between {@link java.sql.Types#CLOB CLOB} and {@link Clob}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class ClobType extends AbstractSingleColumnStandardBasicType<Clob> {
    public static final ClobType INSTANCE = new ClobType();

    public ClobType() {
        super(cn.sexycode.sql.type.descriptor.sql.ClobTypeDescriptor.DEFAULT, ClobTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "clob";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

}
