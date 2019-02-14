/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.sql.type.descriptor.java.ClassTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.VarcharTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#VARCHAR VARCHAR} and {@link Class}
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class ClassType extends AbstractSingleColumnStandardBasicType<Class> {
    public static final ClassType INSTANCE = new ClassType();

    public ClassType() {
        super(VarcharTypeDescriptor.INSTANCE, ClassTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "class";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

}
