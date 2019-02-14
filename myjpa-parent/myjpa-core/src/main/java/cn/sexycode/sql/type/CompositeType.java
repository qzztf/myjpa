/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import java.lang.reflect.Method;

/**
 * Contract for value types to hold collections and have cascades, etc.  The notion is that of composition.  JPA terms
 * this an embeddable.
 *
 * @author Steve Ebersole
 */
public interface CompositeType extends Type {
    /**
     * Get the types of the component properties
     *
     * @return The component property types.
     */
    Type[] getSubtypes();

    /**
     * Get the names of the component properties
     *
     * @return The component property names
     */
    String[] getPropertyNames();

    /**
     * Retrieve the indicators regarding which component properties are nullable.
     * <p/>
     * An optional operation
     *
     * @return nullability of component properties
     */
    boolean[] getPropertyNullability();


    /**
     * Is the given method a member of this component's class?
     *
     * @param method The method to check
     * @return True if the method is a member; false otherwise.
     */
    boolean isMethodOf(Method method);

    /**
     * Is this component embedded?  "embedded" indicates that the component is "virtual", that its properties are
     * "flattened" onto its owner
     *
     * @return True if this component is embedded; false otherwise.
     */
    boolean isEmbedded();

    /**
     * Convenience method to quickly check {@link #getPropertyNullability} for any non-nullable sub-properties.
     *
     * @return {@code true} if any of the properties are not-nullable as indicated by {@link #getPropertyNullability},
     * {@code false} otherwise.
     */
    boolean hasNotNullProperty();

    /**
     * Convenience method for locating the property index for a given property name.
     *
     * @param propertyName The (sub-)property name to find.
     * @return The (sub-)property index, relative to all the array-valued method returns defined on this contract.
     */
    int getPropertyIndex(String propertyName);
}
