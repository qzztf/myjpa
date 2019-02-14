/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.mybatis.jpa.binding.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Properties;

/**
 * Used internally to build instances of {@link Type}, specifically it builds instances of
 * <p/>
 * <p/>
 * Used internally to obtain instances of <tt>Type</tt>. Applications should use static methods
 * and constants on <tt>org.hibernate.Hibernate</tt>.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
@SuppressWarnings({"unchecked"})
public final class TypeFactory implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(TypeFactory.class);
    private final TypeScopeImpl typeScope = new TypeScopeImpl();

    public static interface TypeScope extends Serializable {
    }

    private static class TypeScopeImpl implements TypeFactory.TypeScope {
    }

    public Type byClass(Class clazz, Properties parameters) {
        if (Type.class.isAssignableFrom(clazz)) {
            return type(clazz, parameters);
        }


        if (UserType.class.isAssignableFrom(clazz)) {
            return custom(clazz, parameters);
        }


        if (Serializable.class.isAssignableFrom(clazz)) {
            return serializable(clazz);
        }

        return null;
    }

    public Type type(Class<Type> typeClass, Properties parameters) {
        try {
            Type type = typeClass.newInstance();
            injectParameters(type, parameters);
            return type;
        } catch (Exception e) {
            throw new MappingException("Could not instantiate Type: " + typeClass.getName(), e);
        }
    }

    // todo : can a Properties be wrapped in unmodifiable in any way?
    private final static Properties EMPTY_PROPERTIES = new Properties();

    public static void injectParameters(Object type, Properties parameters) {
        if (ParameterizedType.class.isInstance(type)) {
            if (parameters == null) {
                ((ParameterizedType) type).setParameterValues(EMPTY_PROPERTIES);
            } else {
                ((ParameterizedType) type).setParameterValues(parameters);
            }
        } else if (parameters != null && !parameters.isEmpty()) {
            throw new MappingException("type is not parameterized: " + type.getClass().getName());
        }
    }


    public CustomType custom(Class<UserType> typeClass, Properties parameters) {
        return custom(typeClass, parameters, typeScope);
    }

    /**
     * @deprecated Only for use temporary use by {@link org.hibernate.Hibernate}
     */
    @Deprecated
    public static CustomType custom(Class<UserType> typeClass, Properties parameters, TypeScope scope) {
        try {
            UserType userType = typeClass.newInstance();
            injectParameters(userType, parameters);
            return new CustomType(userType);
        } catch (Exception e) {
            throw new MappingException("Unable to instantiate custom type: " + typeClass.getName(), e);
        }
    }

    /**
     * Build a {@link SerializableType} from the given {@link Serializable} class.
     *
     * @param serializableClass The {@link Serializable} class.
     * @param <T>               The actual class type (extends Serializable)
     * @return The built {@link SerializableType}
     */
    public static <T extends Serializable> SerializableType<T> serializable(Class<T> serializableClass) {
        return new SerializableType<T>(serializableClass);
    }


    // collection type builders ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public CollectionType array(String role, String propertyRef, Class elementClass) {
        return new ArrayType(typeScope, role, propertyRef, elementClass);
    }

    public CollectionType list(String role, String propertyRef) {
        return new ListType(typeScope, role, propertyRef);
    }

    public CollectionType set(String role, String propertyRef) {
        return new SetType(typeScope, role, propertyRef);
    }

    public CollectionType orderedSet(String role, String propertyRef) {
        return new OrderedSetType(typeScope, role, propertyRef);
    }

    public CollectionType sortedSet(String role, String propertyRef, Comparator comparator) {
        return new SortedSetType(typeScope, role, propertyRef, comparator);
    }


    // any type builder ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Type any(Type metaType, Type identifierType) {
        return new AnyType(typeScope, metaType, identifierType);
    }
}
