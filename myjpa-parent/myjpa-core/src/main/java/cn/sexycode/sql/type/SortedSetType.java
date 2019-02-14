/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import java.util.Comparator;
import java.util.TreeSet;

public class SortedSetType extends SetType {
    private final Comparator comparator;

    public SortedSetType(TypeFactory.TypeScope typeScope, String role, String propertyRef, Comparator comparator) {
        super(typeScope, role, propertyRef);
        this.comparator = comparator;
    }


    @Override
    public Class getReturnedClass() {
        return java.util.SortedSet.class;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Object instantiate(int anticipatedSize) {
        return new TreeSet(comparator);
    }


}
