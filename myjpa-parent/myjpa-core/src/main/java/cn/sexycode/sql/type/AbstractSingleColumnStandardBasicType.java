/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.sql.type.descriptor.java.JavaTypeDescriptor;
import cn.sexycode.sql.type.descriptor.sql.SqlTypeDescriptor;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public abstract class AbstractSingleColumnStandardBasicType<T>
        extends AbstractStandardBasicType<T>
        implements SingleColumnType<T> {

    public AbstractSingleColumnStandardBasicType(SqlTypeDescriptor sqlTypeDescriptor, JavaTypeDescriptor<T> javaTypeDescriptor) {
        super(sqlTypeDescriptor, javaTypeDescriptor);
    }

    @Override
    public final int sqlType() {
        return getSqlTypeDescriptor().getSqlType();
    }

}
