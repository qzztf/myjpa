package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AnyType;
import cn.sexycode.myjpa.sql.type.BasicType;
import cn.sexycode.myjpa.sql.type.SerializableType;
import cn.sexycode.myjpa.sql.type.StringType;

/**
 * Specific adaptation of the "any" type to the old deprecated "object" type
 *
 */
public class ObjectType extends AnyType implements BasicType {
    /**
     * Singleton access
     */
    public static final ObjectType INSTANCE = new ObjectType();

    private ObjectType() {
        super(StringType.INSTANCE, SerializableType.INSTANCE);
    }

    @Override
    public String getName() {
        return "object";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName(), Object.class.getName()};
    }
}
