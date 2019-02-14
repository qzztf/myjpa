/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.mybatis.jpa.binding.MappingException;
import cn.sexycode.mybatis.jpa.util.ArrayHelper;
import cn.sexycode.sql.PropertyNotFoundException;
import cn.sexycode.sql.Size;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Adapts {@link CompositeUserType} to the {@link Type} interface
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class CompositeCustomType extends AbstractType implements CompositeType, BasicType {
    private final CompositeUserType userType;
    private final String[] registrationKeys;
    private final String name;
    private final boolean customLogging;

    public CompositeCustomType(CompositeUserType userType) {
        this(userType, ArrayHelper.EMPTY_STRING_ARRAY);
    }

    public CompositeCustomType(CompositeUserType userType, String[] registrationKeys) {
        this.userType = userType;
        this.name = userType.getClass().getName();
        this.customLogging = LoggableUserType.class.isInstance(userType);
        this.registrationKeys = registrationKeys;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class getReturnedClass() {
        return userType.returnedClass();
    }

    @Override
    public boolean isMutable() {
        return userType.isMutable();
    }

    @Override
    public String[] getRegistrationKeys() {
        return registrationKeys;
    }

    public CompositeUserType getUserType() {
        return userType;
    }

    @Override
    public boolean isMethodOf(Method method) {
        return false;
    }

    @Override
    public Type[] getSubtypes() {
        return userType.getPropertyTypes();
    }

    @Override
    public String[] getPropertyNames() {
        return userType.getPropertyNames();
    }

    @Override
    public int getPropertyIndex(String name) {
        String[] names = getPropertyNames();
        for (int i = 0, max = names.length; i < max; i++) {
            if (names[i].equals(name)) {
                return i;
            }
        }
        throw new PropertyNotFoundException(
                "Unable to locate property named " + name + " on " + getReturnedClass().getName()
        );
    }


    @Override
    public boolean isComponentType() {
        return true;
    }

    @Override
    public boolean isEqual(Object x, Object y)
            throws TypeException {
        return userType.equals(x, y);
    }

    @Override
    public int getHashCode(Object x) {
        return userType.hashCode(x);
    }

    @Override
    public int getColumnSpan(Mapping mapping) throws MappingException {
        Type[] types = userType.getPropertyTypes();
        int n = 0;
        for (Type type : types) {
            n += type.getColumnSpan(mapping);
        }
        return n;
    }

    @Override
    public Object nullSafeGet(
            ResultSet rs,
            String columnName,
            Object owner) throws TypeException, SQLException {
        return userType.nullSafeGet(rs, new String[]{columnName}, owner);
    }

    @Override
    public Object nullSafeGet(
            ResultSet rs,
            String[] names,
            Object owner) throws TypeException, SQLException {
        return userType.nullSafeGet(rs, names, owner);
    }

    @Override
    public void nullSafeSet(
            PreparedStatement st,
            Object value,
            int index) throws TypeException, SQLException {
        userType.nullSafeSet(st, value, index);
    }

    @Override
    public int[] sqlTypes(Mapping mapping) throws MappingException {
        int[] result = new int[getColumnSpan(mapping)];
        int n = 0;
        for (Type type : userType.getPropertyTypes()) {
            for (int sqlType : type.sqlTypes(mapping)) {
                result[n++] = sqlType;
            }
        }
        return result;
    }

    @Override
    public Size[] dictatedSizes(Mapping mapping) throws MappingException {
        //Not called at runtime so doesn't matter if its slow :)
        final Size[] sizes = new Size[getColumnSpan(mapping)];
        int soFar = 0;
        for (Type propertyType : userType.getPropertyTypes()) {
            final Size[] propertySizes = propertyType.dictatedSizes(mapping);
            System.arraycopy(propertySizes, 0, sizes, soFar, propertySizes.length);
            soFar += propertySizes.length;
        }
        return sizes;
    }

    @Override
    public Size[] defaultSizes(Mapping mapping) throws MappingException {
        //Not called at runtime so doesn't matter if its slow :)
        final Size[] sizes = new Size[getColumnSpan(mapping)];
        int soFar = 0;
        for (Type propertyType : userType.getPropertyTypes()) {
            final Size[] propertySizes = propertyType.defaultSizes(mapping);
            System.arraycopy(propertySizes, 0, sizes, soFar, propertySizes.length);
            soFar += propertySizes.length;
        }
        return sizes;
    }

    @Override
    public String toLoggableString(Object value) throws TypeException {
        if (value == null) {
            return "null";
        } else {
            return value.toString();
        }
    }

    @Override
    public boolean[] getPropertyNullability() {
        return null;
    }

    @Override
    public boolean[] toColumnNullness(Object value, Mapping mapping) {
        boolean[] result = new boolean[getColumnSpan(mapping)];
        if (value == null) {
            return result;
        }
//		Object[] values = getPropertyValues( value, EntityMode.POJO ); //TODO!!!!!!!
        int loc = 0;
        Type[] propertyTypes = getSubtypes();
        for (int i = 0; i < propertyTypes.length; i++) {
//			boolean[] propertyNullness = propertyTypes[i].toColumnNullness( values[i], mapping );
//			System.arraycopy( propertyNullness, 0, result, loc, propertyNullness.length );
//			loc += propertyNullness.length;
        }
        return result;
    }


    @Override
    public boolean isEmbedded() {
        return false;
    }

    @Override
    public boolean hasNotNullProperty() {
        // We just don't know.  So assume nullable
        return false;
    }
}
