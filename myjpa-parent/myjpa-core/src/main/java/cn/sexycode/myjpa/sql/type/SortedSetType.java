package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.SetType;
import cn.sexycode.myjpa.sql.type.TypeFactory;

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
