/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type.descriptor.java;


import cn.sexycode.mybatis.jpa.binding.MyJpaException;
import cn.sexycode.sql.util.ComparableComparator;
import cn.sexycode.sql.util.EqualsHelper;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Abstract adapter for Java type descriptors.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractTypeDescriptor<T> implements JavaTypeDescriptor<T>, Serializable {
    private final Class<T> type;
    private final MutabilityPlan<T> mutabilityPlan;
    private final Comparator<T> comparator;

    /**
     * Initialize a type descriptor for the given type.  Assumed immutable.
     *
     * @param type The Java type.
     * @see #AbstractTypeDescriptor(Class, MutabilityPlan)
     */
    @SuppressWarnings({"unchecked"})
    protected AbstractTypeDescriptor(Class<T> type) {
        this(type, (MutabilityPlan<T>) ImmutableMutabilityPlan.INSTANCE);
    }

    /**
     * Initialize a type descriptor for the given type.  Assumed immutable.
     *
     * @param type           The Java type.
     * @param mutabilityPlan The plan for handling mutability aspects of the java type.
     */
    @SuppressWarnings({"unchecked"})
    protected AbstractTypeDescriptor(Class<T> type, MutabilityPlan<T> mutabilityPlan) {
        this.type = type;
        this.mutabilityPlan = mutabilityPlan;
        this.comparator = Comparable.class.isAssignableFrom(type)
                ? (Comparator<T>) ComparableComparator.INSTANCE
                : null;
    }

    @Override
    public MutabilityPlan<T> getMutabilityPlan() {
        return mutabilityPlan;
    }

    @Override
    public Class<T> getJavaTypeClass() {
        return type;
    }

    @Override
    public int extractHashCode(T value) {
        return value.hashCode();
    }

    @Override
    public boolean areEqual(T one, T another) {
        return EqualsHelper.equals(one, another);
    }

    @Override
    public Comparator<T> getComparator() {
        return comparator;
    }

    @Override
    public String extractLoggableRepresentation(T value) {
        return (value == null) ? "null" : value.toString();
    }

    protected MyJpaException unknownUnwrap(Class conversionType) {
        throw new MyJpaException(
                "Unknown unwrap conversion requested: " + type.getName() + " to " + conversionType.getName()
        );
    }

    protected MyJpaException unknownWrap(Class conversionType) {
        throw new MyJpaException(
                "Unknown wrap conversion requested: " + conversionType.getName() + " to " + type.getName()
        );
    }
}
