/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListType extends CollectionType {

    public ListType(TypeFactory.TypeScope typeScope, String role, String propertyRef) {
        super(typeScope, role, propertyRef);
    }

    @Override
    public Class getReturnedClass() {
        return List.class;
    }

    @Override
    public String toLoggableString(Object value) throws TypeException {
        return null;
    }


    @Override
    public Object instantiate(int anticipatedSize) {
        return anticipatedSize <= 0 ? new ArrayList() : new ArrayList(anticipatedSize + 1);
    }

    @Override
    public Object indexOf(Object collection, Object element) {
        List list = (List) collection;
        for (int i = 0; i < list.size(); i++) {
            //TODO: proxies!
            if (list.get(i) == element) {
                return i;
            }
        }
        return null;
    }
}
