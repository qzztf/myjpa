package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.*;
import cn.sexycode.myjpa.sql.util.Size;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Defines a mapping between a Java type and one or more JDBC {@linkplain java.sql.Types types}, as well
 * as describing the in-memory semantics of the given java type (how do we check it for 'dirtiness', how do
 * we copy values, etc).
 * <p/>
 * Application developers needing custom types can implement this interface (either directly or via subclassing an
 * existing impl) or by the (slightly more stable, though more limited) {@link org.hibernate.usertype.UserType}
 * interface.
 * <p/>
 * Implementations of this interface must certainly be thread-safe.  It is recommended that they be immutable as
 * well, though that is difficult to achieve completely given the no-arg constructor requirement for custom types.
 *
 * @author qzz
 * @author qzz
 */
public interface Type extends Serializable {
    /**
     * Return true if the implementation is castable to {@link AssociationType}. This does not necessarily imply that
     * the type actually represents an association.  Essentially a polymorphic version of
     * {@code (type instanceof AssociationType.class)}
     *
     * @return True if this type is also an {@link AssociationType} implementor; false otherwise.
     */
    boolean isAssociationType();


    /**
     * Return true if the implementation is castable to {@link CollectionType}. Essentially a polymorphic version of
     * {@code (type instanceof CollectionType.class)}
     * <p/>
     * A {@link CollectionType} is additionally an {@link AssociationType}; so if this method returns true,
     * {@link #isAssociationType()} should also return true.
     *
     * @return True if this type is also a {@link CollectionType} implementor; false otherwise.
     */
    boolean isCollectionType();
    /**
     * Return true if the implementation is castable to {@link EntityType}. Essentially a polymorphic
     * version of {@code (type instanceof EntityType.class)}.
     * <p/>
     * An {@link EntityType} is additionally an {@link AssociationType}; so if this method returns true,
     * {@link #isAssociationType()} should also return true.
     *
     * @return True if this type is also an {@link EntityType} implementor; false otherwise.
     */
    boolean isEntityType();

    /**
     * Return true if the implementation is castable to {@link AnyType}. Essentially a polymorphic
     * version of {@code (type instanceof AnyType.class)}.
     * <p/>
     * An {@link AnyType} is additionally an {@link AssociationType}; so if this method returns true,
     * {@link #isAssociationType()} should also return true.
     *
     * @return True if this type is also an {@link AnyType} implementor; false otherwise.
     */
    boolean isAnyType();

    /**
     * Return true if the implementation is castable to {@link CompositeType}. Essentially a polymorphic
     * version of {@code (type instanceof CompositeType.class)}.  A component type may own collections or
     * associations and hence must provide certain extra functionality.
     *
     * @return True if this type is also an {@link CompositeType} implementor; false otherwise.
     */
    boolean isComponentType();

    /**
     * How many columns are used to persist this type.  Always the same as {@code sqlTypes(mapping).length}
     *
     * @param mapping The mapping object :/
     * @return The number of columns
     * @throws TypeException Generally indicates an issue accessing the passed mapping object.
     */
    int getColumnSpan(Mapping mapping) throws TypeException;

    /**
     * Return the JDBC types codes (per {@link java.sql.Types}) for the columns mapped by this type.
     * <p/>
     * NOTE: The number of elements in this array matches the return from {@link #getColumnSpan}.
     *
     * @param mapping The mapping object :/
     * @return The JDBC type codes.
     * @throws TypeException Generally indicates an issue accessing the passed mapping object.
     */
    int[] sqlTypes(Mapping mapping) throws TypeException;

    /**
     * Return the column sizes dictated by this type.  For example, the mapping for a {@code char}/{@link Character} would
     * have a dictated length limit of 1; for a string-based {@link java.util.UUID} would have a size limit of 36; etc.
     * <p/>
     * NOTE: The number of elements in this array matches the return from {@link #getColumnSpan}.
     *
     * @param mapping The mapping object :/
     * @return The dictated sizes.
     * @throws TypeException Generally indicates an issue accessing the passed mapping object.
     * @todo Would be much much better to have this aware of Dialect once the service/metamodel split is done
     */
    Size[] dictatedSizes(Mapping mapping) throws TypeException;

    /**
     * Defines the column sizes to use according to this type if the user did not explicitly say (and if no
     * {@link #dictatedSizes} were given).
     * <p/>
     * NOTE: The number of elements in this array matches the return from {@link #getColumnSpan}.
     *
     * @param mapping The mapping object :/
     * @return The default sizes.
     * @throws TypeException Generally indicates an issue accessing the passed mapping object.
     * @todo Would be much much better to have this aware of Dialect once the service/metamodel split is done
     */
    Size[] defaultSizes(Mapping mapping) throws TypeException;

    /**
     * The class returned by {@link #nullSafeGet} methods. This is used to  establish the class of an array of
     * this type.
     *
     * @return The java type class handled by this type.
     */
    Class getReturnedClass();

    /**
     * Compare two instances of the class mapped by this type for persistence "equality" (equality of persistent
     * state) taking a shortcut for entity references.
     * <p/>
     * For most types this should equate to an {@link Object#equals equals} check on the values.  For associations
     * the implication is a bit different.  For most types it is conceivable to simply delegate to {@link #isEqual}
     *
     * @param x The first value
     * @param y The second value
     * @return True if there are considered the same (see discussion above).
     * @throws TypeException A problem occurred performing the comparison
     */
    boolean isSame(Object x, Object y) throws TypeException;

    /**
     * Compare two instances of the class mapped by this type for persistence "equality" (equality of persistent
     * state).
     * <p/>
     * This should always equate to some form of comparison of the value's internal state.  As an example, for
     * something like a date the comparison should be based on its internal "time" state based on the specific portion
     * it is meant to represent (timestamp, date, time).
     *
     * @param x The first value
     * @param y The second value
     * @return True if there are considered equal (see discussion above).
     * @throws TypeException A problem occurred performing the comparison
     */
    boolean isEqual(Object x, Object y) throws TypeException;

    /**
     * Get a hash code, consistent with persistence "equality".  Again for most types the normal usage is to
     * delegate to the value's {@link Object#hashCode hashCode}.
     *
     * @param x The value for which to retrieve a hash code
     * @return The hash code
     * @throws TypeException A problem occurred calculating the hash code
     */
    int getHashCode(Object x) throws TypeException;


    /**
     * Perform a {@link java.util.Comparator} style comparison between values
     *
     * @param x The first value
     * @param y The second value
     * @return The comparison result.  See {@link java.util.Comparator#compare} for a discussion.
     */
    int compare(Object x, Object y);


    /**
     * Extract a value of the {@link #getReturnedClass() mapped class} from the JDBC result set. Implementors
     * should handle possibility of null values.
     *
     * @param rs      The result set from which to extract value.
     * @param names   the column names making up this type value (use to read from result set)
     * @param session The originating session
     * @param owner   the parent entity
     * @return The extracted value
     * @throws TypeException An error from Hibernate
     * @throws SQLException  An error from the JDBC driver
     * @see Type#hydrate(ResultSet, String[], SharedSessionContractImplementor, Object) alternative, 2-phase property initialization
     */
    Object nullSafeGet(ResultSet rs, String[] names, Object owner)
            throws SQLException;

    /**
     * Extract a value of the {@link #getReturnedClass() mapped class} from the JDBC result set. Implementors
     * should handle possibility of null values.  This form might be called if the type is known to be a
     * single-column type.
     *
     * @param rs      The result set from which to extract value.
     * @param name    the column name making up this type value (use to read from result set)
     * @param session The originating session
     * @param owner   the parent entity
     * @return The extracted value
     * @throws TypeException An error from Hibernate
     * @throws SQLException  An error from the JDBC driver
     */
    Object nullSafeGet(ResultSet rs, String name, Object owner)
            throws SQLException;

    /**
     * Bind a value represented by an instance of the {@link #getReturnedClass() mapped class} to the JDBC prepared
     * statement.  Implementors should handle possibility of null values.  A multi-column type should bind parameters
     * starting from <tt>index</tt>.
     *
     * @param st      The JDBC prepared statement to which to bind
     * @param value   the object to write
     * @param index   starting parameter bind index
     * @param session The originating session
     * @throws TypeException An error from Hibernate
     * @throws SQLException  An error from the JDBC driver
     */
    void nullSafeSet(PreparedStatement st, Object value, int index)
            throws SQLException;

    /**
     * Generate a representation of the value for logging purposes.
     *
     * @param value   The value to be logged
     * @param factory The session factory
     * @return The loggable representation
     * @throws TypeException An error from Hibernate
     */
    String toLoggableString(Object value)
            throws TypeException;

    /**
     * Returns the abbreviated name of the type.
     *
     * @return String the Hibernate type name
     */
    String getName();


    /**
     * Are objects of this type mutable. (With respect to the referencing object ...
     * entities and collections are considered immutable because they manage their
     * own internal state.)
     *
     * @return boolean
     */
    boolean isMutable();

    /**
     * Given an instance of the type, return an array of boolean, indicating
     * which mapped columns would be null.
     *
     * @param value   an instance of the type
     * @param mapping The mapping abstraction
     * @return array indicating column nullness for a value instance
     */
    boolean[] toColumnNullness(Object value, Mapping mapping);

}
