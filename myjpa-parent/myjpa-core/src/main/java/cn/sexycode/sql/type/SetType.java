/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import java.util.HashSet;

public class SetType extends CollectionType {

    public SetType(TypeFactory.TypeScope typeScope, String role, String propertyRef) {
        super(typeScope, role, propertyRef);
    }


    @Override
    public Class getReturnedClass() {
        return java.util.Set.class;
    }


    @Override
    public Object instantiate(int anticipatedSize) {
        return anticipatedSize <= 0
                ? new HashSet()
                : new HashSet(anticipatedSize + (int) (anticipatedSize * .75f), .75f);
    }

}
