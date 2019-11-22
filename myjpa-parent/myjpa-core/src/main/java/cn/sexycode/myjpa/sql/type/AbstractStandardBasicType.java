package cn.sexycode.myjpa.sql.type;


import cn.sexycode.myjpa.sql.type.BasicType;
import cn.sexycode.myjpa.sql.type.Mapping;
import cn.sexycode.myjpa.sql.type.ProcedureParameterExtractionAware;
import cn.sexycode.myjpa.sql.type.ProcedureParameterNamedBinder;
import cn.sexycode.myjpa.sql.type.StringRepresentableType;
import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.util.Size;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.JavaTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.MutabilityPlan;
import cn.sexycode.myjpa.sql.type.descriptor.sql.SqlTypeDescriptor;
import cn.sexycode.util.core.collection.ArrayHelper;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Convenience base class for {@link cn.sexycode.myjpa.sql.type.BasicType} implementations
 *
 */
public abstract class AbstractStandardBasicType<T>
        implements BasicType, StringRepresentableType<T>, ProcedureParameterExtractionAware<T>,
        ProcedureParameterNamedBinder {

    private static final Size DEFAULT_SIZE = new Size(19, 2, 255, Size.LobMultiplier.NONE); // to match legacy behavior
    private final Size dictatedSize = new Size();

    // Don't use final here.  Need to initialize afterQuery-the-fact
    // by DynamicParameterizedTypes.
    private SqlTypeDescriptor sqlTypeDescriptor;
    private JavaTypeDescriptor<T> javaTypeDescriptor;
    // sqlTypes need always to be in sync with sqlTypeDescriptor
    private int[] sqlTypes;

    public AbstractStandardBasicType(SqlTypeDescriptor sqlTypeDescriptor, JavaTypeDescriptor<T> javaTypeDescriptor) {
        this.sqlTypeDescriptor = sqlTypeDescriptor;
        this.sqlTypes = new int[]{sqlTypeDescriptor.getSqlType()};
        this.javaTypeDescriptor = javaTypeDescriptor;
    }

    public T fromString(String string) {
        return javaTypeDescriptor.fromString(string);
    }

    @Override
    public String toString(T value) {
        return javaTypeDescriptor.toString(value);
    }

    @Override
    public T fromStringValue(String xml) throws TypeException {
        return fromString(xml);
    }


    @Override
    @SuppressWarnings({"unchecked"})
    public final String toLoggableString(Object value) {
        return javaTypeDescriptor.extractLoggableRepresentation((T) value);
    }

    protected MutabilityPlan<T> getMutabilityPlan() {
        return javaTypeDescriptor.getMutabilityPlan();
    }


    @Override
    public boolean[] toColumnNullness(Object value, cn.sexycode.myjpa.sql.type.Mapping mapping) {
        return value == null ? ArrayHelper.FALSE : ArrayHelper.TRUE;
    }

    @Override
    public String[] getRegistrationKeys() {
        return registerUnderJavaType()
                ? new String[]{getName(), javaTypeDescriptor.getJavaTypeClass().getName()}
                : new String[]{getName()};
    }

    protected boolean registerUnderJavaType() {
        return false;
    }

    protected static Size getDefaultSize() {
        return DEFAULT_SIZE;
    }

    protected Size getDictatedSize() {
        return dictatedSize;
    }

    // final implementations ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final JavaTypeDescriptor<T> getJavaTypeDescriptor() {
        return javaTypeDescriptor;
    }

    public final void setJavaTypeDescriptor(JavaTypeDescriptor<T> javaTypeDescriptor) {
        this.javaTypeDescriptor = javaTypeDescriptor;
    }

    public final SqlTypeDescriptor getSqlTypeDescriptor() {
        return sqlTypeDescriptor;
    }

    public final void setSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        this.sqlTypeDescriptor = sqlTypeDescriptor;
        this.sqlTypes = new int[]{sqlTypeDescriptor.getSqlType()};
    }

    @Override
    public final Class getReturnedClass() {
        return javaTypeDescriptor.getJavaTypeClass();
    }

    @Override
    public final int getColumnSpan(cn.sexycode.myjpa.sql.type.Mapping mapping)  {
        return 1;
    }

    @Override
    public final int[] sqlTypes(cn.sexycode.myjpa.sql.type.Mapping mapping) {
        return sqlTypes;
    }

    @Override
    public Size[] dictatedSizes(cn.sexycode.myjpa.sql.type.Mapping mapping) {
        return new Size[]{getDictatedSize()};
    }

    @Override
    public Size[] defaultSizes(Mapping mapping) {
        return new Size[]{getDefaultSize()};
    }

    @Override
    public final boolean isAssociationType() {
        return false;
    }


    @Override
    public final boolean isCollectionType() {
        return false;
    }
    @Override
    public final boolean isComponentType() {
        return false;
    }

    @Override
    public final boolean isEntityType() {
        return false;
    }

    @Override
    public final boolean isAnyType() {
        return false;
    }

    public final boolean isXMLElement() {
        return false;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public final boolean isSame(Object x, Object y) {
        return isEqual(x, y);
    }


    @Override
    @SuppressWarnings({"unchecked"})
    public final boolean isEqual(Object one, Object another) {
        return javaTypeDescriptor.areEqual((T) one, (T) another);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public final int getHashCode(Object x) {
        return javaTypeDescriptor.extractHashCode((T) x);
    }


    @Override
    @SuppressWarnings({"unchecked"})
    public final int compare(Object x, Object y) {
        return javaTypeDescriptor.getComparator().compare((T) x, (T) y);
    }

    @Override
    public final Object nullSafeGet(
            ResultSet rs,
            String[] names,
            Object owner) throws SQLException {
        return nullSafeGet(rs, names[0]);
    }

    @Override
    public final Object nullSafeGet(ResultSet rs, String name, Object owner)
            throws SQLException {
        return nullSafeGet(rs, name);
    }

    public final T nullSafeGet(ResultSet rs, String name) throws SQLException {
        return nullSafeGet(rs, name, null);
    }

    protected final T nullSafeGet(ResultSet rs, String name, WrapperOptions options) throws SQLException {
        return remapSqlTypeDescriptor(options).getExtractor(javaTypeDescriptor).extract(rs, name, options);
    }

    public Object get(ResultSet rs, String name) throws SQLException {
        return nullSafeGet(rs, name);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public final void nullSafeSet(
            PreparedStatement st,
            Object value,
            int index) throws SQLException {
        nullSafeSet(st, value, index, null);
    }

    @SuppressWarnings({"unchecked"})
    protected final void nullSafeSet(PreparedStatement st, Object value, int index, WrapperOptions options) throws SQLException {
        remapSqlTypeDescriptor(options).getBinder(javaTypeDescriptor).bind(st, (T) value, index, options);
    }

    protected SqlTypeDescriptor remapSqlTypeDescriptor(WrapperOptions options) {
        return options.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }


    @Override
    public final boolean isMutable() {
        return getMutabilityPlan().isMutable();
    }


    @Override
    public boolean canDoExtraction() {
        return true;
    }


    @Override
    public void nullSafeSet(CallableStatement st, Object value, String name) throws SQLException {
        nullSafeSet(st, value, name, null);
    }

    @SuppressWarnings("unchecked")
    protected final void nullSafeSet(CallableStatement st, Object value, String name, WrapperOptions options) throws SQLException {
        remapSqlTypeDescriptor(options).getBinder(javaTypeDescriptor).bind(st, (T) value, name, options);
    }


    public void set(PreparedStatement st, T value, int index) throws SQLException {
        nullSafeSet(st, value, index);
    }

    @Override
    public boolean canDoSetting() {
        return true;
    }
}
