package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.CollectionType;
import cn.sexycode.myjpa.sql.type.TypeFactory;

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
