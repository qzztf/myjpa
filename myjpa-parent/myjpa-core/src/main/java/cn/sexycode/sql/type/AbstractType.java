/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.sql.Size;
import cn.sexycode.sql.util.EqualsHelper;

/**
 * Abstract superclass of the built in Type hierarchy.
 *
 * @author Gavin King
 */
public abstract class AbstractType implements Type {
    protected static final Size LEGACY_DICTATED_SIZE = new Size();
    protected static final Size LEGACY_DEFAULT_SIZE = new Size(19, 2, 255, Size.LobMultiplier.NONE); // to match legacy behavior

    @Override
    public boolean isAssociationType() {
        return false;
    }

    @Override
    public boolean isComponentType() {
        return false;
    }

    @Override
    public boolean isEntityType() {
        return false;
    }

    @Override
    public int compare(Object x, Object y) {
        return ((Comparable) x).compareTo(y);
    }


    @Override
    public boolean isAnyType() {
        return false;
    }


    @Override
    public boolean isSame(Object x, Object y) throws TypeException {
        return isEqual(x, y);
    }

    @Override
    public boolean isEqual(Object x, Object y) {
        return EqualsHelper.equals(x, y);
    }

    @Override
    public int getHashCode(Object x) {
        return x.hashCode();
    }


}
