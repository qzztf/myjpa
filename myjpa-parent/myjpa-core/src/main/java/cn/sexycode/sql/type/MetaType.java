/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.binding.MappingException;
import cn.sexycode.sql.Size;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gavin King
 */
public class MetaType extends AbstractType {
    public static final String[] REGISTRATION_KEYS = new String[0];

    private final Type baseType;
    private final Map<Object, String> discriminatorValuesToEntityNameMap;
    private final Map<String, Object> entityNameToDiscriminatorValueMap;

    public MetaType(Map<Object, String> discriminatorValuesToEntityNameMap, Type baseType) {
        this.baseType = baseType;
        this.discriminatorValuesToEntityNameMap = discriminatorValuesToEntityNameMap;
        this.entityNameToDiscriminatorValueMap = new HashMap<>();
        for (Map.Entry<Object, String> entry : discriminatorValuesToEntityNameMap.entrySet()) {
            entityNameToDiscriminatorValueMap.put(entry.getValue(), entry.getKey());
        }
    }

    public String[] getRegistrationKeys() {
        return REGISTRATION_KEYS;
    }

    public Map<Object, String> getDiscriminatorValuesToEntityNameMap() {
        return discriminatorValuesToEntityNameMap;
    }

    @Override
    public int[] sqlTypes(Mapping mapping) throws MappingException {
        return baseType.sqlTypes(mapping);
    }

    @Override
    public Size[] dictatedSizes(Mapping mapping) throws MappingException {
        return baseType.dictatedSizes(mapping);
    }

    @Override
    public Size[] defaultSizes(Mapping mapping) throws MappingException {
        return baseType.defaultSizes(mapping);
    }

    @Override
    public int getColumnSpan(Mapping mapping) throws MappingException {
        return baseType.getColumnSpan(mapping);
    }

    @Override
    public Class getReturnedClass() {
        return String.class;
    }

    @Override
    public Object nullSafeGet(
            ResultSet rs,
            String[] names,
            Object owner) throws TypeException, SQLException {
        Object key = baseType.nullSafeGet(rs, names, owner);
        return key == null ? null : discriminatorValuesToEntityNameMap.get(key);
    }

    @Override
    public Object nullSafeGet(
            ResultSet rs,
            String name,
            Object owner) throws TypeException, SQLException {
        Object key = baseType.nullSafeGet(rs, name, owner);
        return key == null ? null : discriminatorValuesToEntityNameMap.get(key);
    }

    @Override
    public void nullSafeSet(
            PreparedStatement st,
            Object value,
            int index) throws TypeException, SQLException {
        baseType.nullSafeSet(st, value == null ? null : entityNameToDiscriminatorValueMap.get(value), index);
    }


    @Override
    public String toLoggableString(Object value) throws TypeException {
        return toXMLString(value);
    }

    public String toXMLString(Object value) throws TypeException {
        return (String) value; //value is the entity name
    }

    public Object fromXMLString(String xml, Mapping factory) throws TypeException {
        return xml; //xml is the entity name
    }

    @Override
    public String getName() {
        return baseType.getName(); //TODO!
    }


    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public boolean[] toColumnNullness(Object value, Mapping mapping) {
        throw new UnsupportedOperationException();
    }

}
