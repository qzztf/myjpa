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
import java.util.Arrays;

/**
 * Handles "any" mappings
 *
 * @author Gavin King
 */
public class AnyType extends AbstractType implements CompositeType {
    private final TypeFactory.TypeScope scope;
    private final Type identifierType;
    private final Type discriminatorType;

    /**
     * Intended for use only from legacy {@link ObjectType} type definition
     */
    protected AnyType(Type discriminatorType, Type identifierType) {
        this(null, discriminatorType, identifierType);
    }

    public AnyType(TypeFactory.TypeScope scope, Type discriminatorType, Type identifierType) {
        this.scope = scope;
        this.discriminatorType = discriminatorType;
        this.identifierType = identifierType;
    }

    public Type getIdentifierType() {
        return identifierType;
    }

    public Type getDiscriminatorType() {
        return discriminatorType;
    }


    // general Type metadata ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String getName() {
        return "object";
    }

    @Override
    public Class getReturnedClass() {
        return Object.class;
    }

    @Override
    public int[] sqlTypes(Mapping mapping) throws MappingException {
        return ArrayHelper.join(discriminatorType.sqlTypes(mapping), identifierType.sqlTypes(mapping));
    }

    @Override
    public Size[] dictatedSizes(Mapping mapping) throws MappingException {
        return ArrayHelper.join(discriminatorType.dictatedSizes(mapping), identifierType.dictatedSizes(mapping));
    }

    @Override
    public Size[] defaultSizes(Mapping mapping) throws MappingException {
        return ArrayHelper.join(discriminatorType.defaultSizes(mapping), identifierType.defaultSizes(mapping));
    }

    @Override
    public boolean isAnyType() {
        return true;
    }

    @Override
    public boolean isAssociationType() {
        return true;
    }

    @Override
    public boolean isComponentType() {
        return true;
    }

    @Override
    public boolean isEmbedded() {
        return false;
    }

    @Override
    public boolean isMutable() {
        return false;
    }


    // general Type functionality ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    @Override
    public boolean isSame(Object x, Object y) throws TypeException {
        return x == y;
    }


    @Override
    public boolean[] toColumnNullness(Object value, Mapping mapping) {
        final boolean[] result = new boolean[getColumnSpan(mapping)];
        if (value != null) {
            Arrays.fill(result, true);
        }
        return result;
    }

    @Override
    public int getColumnSpan(Mapping session) {
        return 2;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
            throws TypeException, SQLException {
        return resolveAny(
                (String) discriminatorType.nullSafeGet(rs, names[0], owner),
                (Serializable) identifierType.nullSafeGet(rs, names[1], owner)
        );
    }


    private Object resolveAny(String entityName, Serializable id)
            throws TypeException {
        return entityName == null || id == null
                ? null
                : null;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String name, Object owner) {
        throw new UnsupportedOperationException("object is a multicolumn type");
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws SQLException {

    }

    @Override
    public String toLoggableString(Object value) throws TypeException {
        return null;
    }


    // CompositeType implementation ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean isMethodOf(Method method) {
        return false;
    }

    private static final String[] PROPERTY_NAMES = new String[]{"class", "id"};

    @Override
    public String[] getPropertyNames() {
        return PROPERTY_NAMES;
    }

    @Override
    public int getPropertyIndex(String name) {
        if (PROPERTY_NAMES[0].equals(name)) {
            return 0;
        } else if (PROPERTY_NAMES[1].equals(name)) {
            return 1;
        }

        throw new PropertyNotFoundException("Unable to locate property named " + name + " on AnyType");
    }

    private static final boolean[] NULLABILITY = new boolean[]{false, false};

    @Override
    public boolean[] getPropertyNullability() {
        return NULLABILITY;
    }

    @Override
    public boolean hasNotNullProperty() {
        // both are non-nullable
        return true;
    }

    @Override
    public Type[] getSubtypes() {
        return new Type[]{discriminatorType, identifierType};
    }

    /**
     * Used to externalize discrimination per a given identifier.  For example, when writing to
     * second level cache we write the discrimination resolved concrete type for each entity written.
     */
    public static final class ObjectTypeCacheEntry implements Serializable {
        final String entityName;
        final Serializable id;

        ObjectTypeCacheEntry(String entityName, Serializable id) {
            this.entityName = entityName;
            this.id = id;
        }
    }
}
