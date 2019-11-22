package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.Type;
import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.util.Size;
import cn.sexycode.util.core.object.EqualsHelper;

/**
 * Abstract superclass of the built in Type hierarchy.
 *
 */
public abstract class AbstractType implements Type {
    protected static final Size LEGACY_DICTATED_SIZE = new Size();
    protected static final Size LEGACY_DEFAULT_SIZE = new Size(19, 2, 255, Size.LobMultiplier.NONE); // to match legacy behavior

    @Override
    public boolean isAssociationType() {
        return false;
    }

    @Override
    public boolean isCollectionType() {
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
