/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import cn.sexycode.mybatis.jpa.binding.MappingException;
import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.mybatis.jpa.util.ArrayHelper;
import cn.sexycode.sql.Size;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Adapts {@link UserType} to the generic {@link Type} interface, in order
 * to isolate user code from changes in the internal Type contracts.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class CustomType
        extends AbstractType
        implements IdentifierType, DiscriminatorType, VersionType, BasicType, StringRepresentableType, ProcedureParameterNamedBinder, ProcedureParameterExtractionAware {

    private final UserType userType;
    private final String name;
    private final int[] types;
    private final Size[] dictatedSizes;
    private final Size[] defaultSizes;
    private final boolean customLogging;
    private final String[] registrationKeys;

    public CustomType(UserType userType) throws MappingException {
        this(userType, ArrayHelper.EMPTY_STRING_ARRAY);
    }

    public CustomType(UserType userType, String[] registrationKeys) throws MappingException {
        this.userType = userType;
        this.name = userType.getClass().getName();
        this.types = userType.sqlTypes();
        this.dictatedSizes = Sized.class.isInstance(userType)
                ? ((Sized) userType).dictatedSizes()
                : new Size[types.length];
        this.defaultSizes = Sized.class.isInstance(userType)
                ? ((Sized) userType).defaultSizes()
                : new Size[types.length];
        this.customLogging = LoggableUserType.class.isInstance(userType);
        this.registrationKeys = registrationKeys;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public String[] getRegistrationKeys() {
        return registrationKeys;
    }

    @Override
    public int[] sqlTypes(Mapping pi) {
        return types;
    }

    @Override
    public Size[] dictatedSizes(Mapping mapping) throws MappingException {
        return dictatedSizes;
    }

    @Override
    public Size[] defaultSizes(Mapping mapping) throws MappingException {
        return defaultSizes;
    }

    @Override
    public int getColumnSpan(Mapping session) {
        return types.length;
    }

    @Override
    public Class getReturnedClass() {
        return userType.returnedClass();
    }

    @Override
    public boolean isEqual(Object x, Object y) throws TypeException {
        return userType.equals(x, y);
    }

    @Override
    public int getHashCode(Object x) {
        return userType.hashCode(x);
    }

    @Override
    public Object nullSafeGet(
            ResultSet rs,
            String[] names,
            Object owner) throws SQLException {
        return userType.nullSafeGet(rs, names, owner);
    }

    @Override
    public Object nullSafeGet(
            ResultSet rs,
            String columnName,
            Object owner) throws SQLException {
        return nullSafeGet(rs, new String[]{columnName}, owner);
    }

    @Override
    public void nullSafeSet(
            PreparedStatement st,
            Object value,
            int index) throws SQLException {
        userType.nullSafeSet(st, value, index);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public String toXMLString(Object value) {
        return toString(value);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public Object fromXMLString(String xml, Mapping factory) {
        return fromStringValue(xml);
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public boolean isMutable() {
        return userType.isMutable();
    }

    @Override
    public Object stringToObject(String xml) {
        return fromStringValue(xml);
    }

    @Override
    public String objectToSQLString(Object value, Dialect dialect) throws Exception {
        return ((EnhancedUserType) userType).objectToSQLString(value);
    }

    @Override
    public Comparator getComparator() {
        return (Comparator) userType;
    }


    @Override
    public String toLoggableString(Object value)
            throws TypeException {
        if (value == null) {
            return "null";
        } else {
            return toXMLString(value);
        }
    }

    @Override
    public boolean[] toColumnNullness(Object value, Mapping mapping) {
        boolean[] result = new boolean[getColumnSpan(mapping)];
        if (value != null) {
            Arrays.fill(result, true);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString(Object value) throws TypeException {
        if (StringRepresentableType.class.isInstance(userType)) {
            return ((StringRepresentableType) userType).toString(value);
        }
        if (value == null) {
            return null;
        }

        return value.toString();
    }

    @Override
    public Object fromStringValue(String string) throws TypeException {
        if (StringRepresentableType.class.isInstance(userType)) {
            return ((StringRepresentableType) userType).fromStringValue(string);
        }
        if (EnhancedUserType.class.isInstance(userType)) {
            //noinspection deprecation
//			return ( (EnhancedUserType) userType ).fromXMLString( string );
        }
        throw new TypeException(
                String.format(
                        "Could not process #fromStringValue, UserType class [%s] did not implement %s or %s",
                        name,
                        StringRepresentableType.class.getName(),
                        EnhancedUserType.class.getName()
                )
        );
    }

    @Override
    public boolean canDoSetting() {
        if (ProcedureParameterNamedBinder.class.isInstance(userType)) {
            return ((ProcedureParameterNamedBinder) userType).canDoSetting();
        }
        return false;
    }

    @Override
    public void nullSafeSet(
            CallableStatement statement, Object value, String name) throws SQLException {
        if (canDoSetting()) {
            ((ProcedureParameterNamedBinder) userType).nullSafeSet(statement, value, name);
        } else {
            throw new UnsupportedOperationException(
                    "Type [" + userType + "] does support parameter binding by name"
            );
        }
    }

    @Override
    public boolean canDoExtraction() {
        if (ProcedureParameterExtractionAware.class.isInstance(userType)) {
            return ((ProcedureParameterExtractionAware) userType).canDoExtraction();
        }
        return false;
    }

}
