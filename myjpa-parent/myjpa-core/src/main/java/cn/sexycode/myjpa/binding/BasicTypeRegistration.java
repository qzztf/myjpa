package cn.sexycode.myjpa.binding;

import cn.sexycode.sql.type.*;

/**
 * @author Steve Ebersole
 */
public class BasicTypeRegistration {
    private final BasicType basicType;

    private final String[] registrationKeys;

    public BasicTypeRegistration(BasicType basicType) {
        this(basicType, basicType.getRegistrationKeys());
    }

    public BasicTypeRegistration(BasicType basicType, String[] registrationKeys) {
        this.basicType = basicType;
        this.registrationKeys = registrationKeys;
    }

    public BasicTypeRegistration(UserType type, String[] keys) {
        this(new CustomType(type, keys), keys);
    }

    public BasicTypeRegistration(CompositeUserType type, String[] keys) {
        this(new CompositeCustomType(type, keys), keys);
    }

    public BasicType getBasicType() {
        return basicType;
    }

    public String[] getRegistrationKeys() {
        return registrationKeys;
    }
}
