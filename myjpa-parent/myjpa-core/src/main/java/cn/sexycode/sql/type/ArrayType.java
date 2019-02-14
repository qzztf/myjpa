/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A type for persistent arrays.
 *
 * @author Gavin King
 */
public class ArrayType extends CollectionType {

    private final Class elementClass;
    private final Class arrayClass;

    public ArrayType(TypeFactory.TypeScope typeScope, String role, String propertyRef, Class elementClass) {
        super(typeScope, role, propertyRef);
        this.elementClass = elementClass;
        arrayClass = Array.newInstance(elementClass, 0).getClass();
    }

    @Override
    public Class getReturnedClass() {
        return arrayClass;
    }


    /**
     * Not defined for collections of primitive type
     */
    @Override
    public Iterator getElementsIterator(Object collection) {
        return Arrays.asList((Object[]) collection).iterator();
    }


    @Override
    public boolean isArrayType() {
        return true;
    }

    @Override
    public String toLoggableString(Object value) throws TypeException {
        if (value == null) {
            return "null";
        }
        int length = Array.getLength(value);
        List list = new ArrayList(length);

        return list.toString();
    }

    @Override
    public Object instantiateResult(Object original) {
        return Array.newInstance(elementClass, Array.getLength(original));
    }


    @Override
    public Object instantiate(int anticipatedSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object indexOf(Object array, Object element) {
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            //TODO: proxies!
            if (Array.get(array, i) == element) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean initializeImmediately() {
        return true;
    }

    @Override
    public boolean hasHolder() {
        return true;
    }

}
