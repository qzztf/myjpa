/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import cn.sexycode.mybatis.jpa.binding.MappingException;
import cn.sexycode.mybatis.jpa.util.ArrayHelper;
import cn.sexycode.sql.Size;
import cn.sexycode.sql.util.MarkerObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A type that handles Hibernate <tt>PersistentCollection</tt>s (including arrays).
 *
 * @author Gavin King
 */
public abstract class CollectionType extends AbstractType implements AssociationType {

    private static final Logger LOG = LoggerFactory.getLogger(CollectionType.class);

    private static final Object NOT_NULL_COLLECTION = new MarkerObject("NOT NULL COLLECTION");
    public static final Object UNFETCHED_COLLECTION = new MarkerObject("UNFETCHED COLLECTION");

    private final TypeFactory.TypeScope typeScope;
    private final String role;
    private final String foreignKeyPropertyName;

    public CollectionType(TypeFactory.TypeScope typeScope, String role, String foreignKeyPropertyName) {
        this.typeScope = typeScope;
        this.role = role;
        this.foreignKeyPropertyName = foreignKeyPropertyName;
    }

    public String getRole() {
        return role;
    }

    public Object indexOf(Object collection, Object element) {
        throw new UnsupportedOperationException("generic collections don't have indexes");
    }


    @Override
    public int compare(Object x, Object y) {
        // collections cannot be compared
        return 0;
    }

    @Override
    public int getHashCode(Object x) {
        throw new UnsupportedOperationException("cannot doAfterTransactionCompletion lookups on collections");
    }


    @Override
    public Object nullSafeGet(ResultSet rs, String name, Object owner) throws SQLException {
        return nullSafeGet(rs, new String[]{name}, owner);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] name, Object owner)
            throws TypeException, SQLException {
        return null;
    }


    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws TypeException, SQLException {
    }

    @Override
    public int[] sqlTypes(Mapping session) throws MappingException {
        return ArrayHelper.EMPTY_INT_ARRAY;
    }

    @Override
    public Size[] dictatedSizes(Mapping mapping) throws MappingException {
        return new Size[]{LEGACY_DICTATED_SIZE};
    }

    @Override
    public Size[] defaultSizes(Mapping mapping) throws MappingException {
        return new Size[]{LEGACY_DEFAULT_SIZE};
    }

    @Override
    public int getColumnSpan(Mapping session) throws MappingException {
        return 0;
    }

    @Override
    public String getName() {
        return getReturnedClass().getName() + '(' + getRole() + ')';
    }


    /**
     * Get an iterator over the element set of the collection in POJO mode
     *
     * @param collection The collection to be iterated
     * @return The iterator.
     */
    protected Iterator getElementsIterator(Object collection) {
        return ((Collection) collection).iterator();
    }

    @Override
    public boolean isMutable() {
        return false;
    }


    /**
     * Note: return true because this type is castable to <tt>AssociationType</tt>. Not because
     * all collections are associations.
     */
    @Override
    public boolean isAssociationType() {
        return true;
    }

    @Override
    public ForeignKeyDirection getForeignKeyDirection() {
        return ForeignKeyDirection.TO_PARENT;
    }


    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean useLHSPrimaryKey() {
        return foreignKeyPropertyName == null;
    }

    @Override
    public String getRHSUniqueKeyPropertyName() {
        return null;
    }


    /**
     * Instantiate a new "underlying" collection exhibiting the same capacity
     * charactersitcs and the passed "original".
     *
     * @param original The original collection.
     * @return The newly instantiated collection.
     */
    protected Object instantiateResult(Object original) {
        // by default just use an unanticipated capacity since we don't
        // know how to extract the capacity to use from original here...
        return instantiate(-1);
    }

    /**
     * Instantiate an empty instance of the "underlying" collection (not a wrapper),
     * but with the given anticipated size (i.e. accounting for initial capacity
     * and perhaps load factor).
     *
     * @param anticipatedSize The anticipated size of the instaniated collection
     *                        afterQuery we are done populating it.
     * @return A newly instantiated collection to be wrapped.
     */
    public abstract Object instantiate(int anticipatedSize);


    @Override
    public String toString() {
        return getClass().getName() + '(' + getRole() + ')';
    }


    public boolean hasHolder() {
        return false;
    }

    protected boolean initializeImmediately() {
        return false;
    }

    @Override
    public String getLHSPropertyName() {
        return foreignKeyPropertyName;
    }

    @Override
    public boolean[] toColumnNullness(Object value, Mapping mapping) {
        return ArrayHelper.EMPTY_BOOLEAN_ARRAY;
    }


    @Override
    public String toLoggableString(Object value)
            throws TypeException {
        if (value == null) {
            return "null";
        }

        return renderLoggableString(value);
    }


    protected String renderLoggableString(Object value) throws TypeException {


        final List<String> list = new ArrayList<String>();

        return list.toString();
    }
}
