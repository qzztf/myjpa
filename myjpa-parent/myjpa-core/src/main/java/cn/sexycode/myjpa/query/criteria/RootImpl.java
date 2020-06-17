package cn.sexycode.myjpa.query.criteria;

import org.apache.ibatis.jdbc.SQL;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import java.util.Set;

public class RootImpl<X> extends AbstractPathImpl<X> implements Root<X> {

    CriteriaBuilder criteriaBuilder;
    EntityType<X> entityType;

    public RootImpl(CriteriaBuilder criteriaBuilder, EntityType<X> entityType) {
        super(criteriaBuilder, entityType.getJavaType());
        this.criteriaBuilder = criteriaBuilder;
        this.entityType = entityType;
    }

    /**
     * Get the attribute by name from the underlying model.  This allows subclasses to
     * define exactly how the attribute is derived.  Called from {@link #locateAttribute}
     * which also applies nullness checking for proper error reporting.
     *
     * @param attributeName The name of the attribute to locate
     * @return The attribute; may be null.
     * @throws IllegalArgumentException If no such attribute exists
     */
    @Override
    protected Attribute<X, ?> locateAttributeInternal(String attributeName) {
        return (Attribute<X, ?>) locateManagedType().getAttribute( attributeName );
    }

    protected ManagedType<? super X> locateManagedType() {
        // by default, this should be the model
        return getModel();
    }

    /**
     * Return the metamodel entity corresponding to the root.
     *
     * @return metamodel entity corresponding to the root
     */
    @Override
    public EntityType<X> getModel() {
        return entityType;
    }

    /**
     * Return the parent "node" in the path or null if no parent.
     *
     * @return parent
     */
    @Override
    public Path<?> getParentPath() {
        return null;
    }

    /**
     * Return the joins that have been made from this bound type.
     * Returns empty set if no joins have been made from this
     * bound type.
     * Modifications to the set do not affect the query.
     *
     * @return joins made from this type
     */
    @Override
    public Set<Join<X, ?>> getJoins() {
        return null;
    }

    /**
     * Whether the <code>From</code> object has been obtained as a result of
     * correlation (use of a <code>Subquery</code> <code>correlate</code>
     * method).
     *
     * @return boolean indicating whether the object has been
     * obtained through correlation
     */
    @Override
    public boolean isCorrelated() {
        return false;
    }

    /**
     * Returns the parent <code>From</code> object from which the correlated
     * <code>From</code> object has been obtained through correlation (use
     * of a <code>Subquery</code> <code>correlate</code> method).
     *
     * @return the parent of the correlated From object
     * @throws IllegalStateException if the From object has
     *                               not been obtained through correlation
     */
    @Override
    public From<X, X> getCorrelationParent() {
        return null;
    }

    /**
     * Create an inner join to the specified single-valued
     * attribute.
     *
     * @param attribute target of the join
     * @return the resulting join
     */
    @Override
    public <Y> Join<X, Y> join(SingularAttribute<? super X, Y> attribute) {
        return null;
    }

    /**
     * Create a join to the specified single-valued attribute
     * using the given join type.
     *
     * @param attribute target of the join
     * @param jt        join type
     * @return the resulting join
     */
    @Override
    public <Y> Join<X, Y> join(SingularAttribute<? super X, Y> attribute, JoinType jt) {
        return null;
    }

    /**
     * Create an inner join to the specified Collection-valued
     * attribute.
     *
     * @param collection target of the join
     * @return the resulting join
     */
    @Override
    public <Y> CollectionJoin<X, Y> join(CollectionAttribute<? super X, Y> collection) {
        return null;
    }

    /**
     * Create an inner join to the specified Set-valued attribute.
     *
     * @param set target of the join
     * @return the resulting join
     */
    @Override
    public <Y> SetJoin<X, Y> join(SetAttribute<? super X, Y> set) {
        return null;
    }

    /**
     * Create an inner join to the specified List-valued attribute.
     *
     * @param list target of the join
     * @return the resulting join
     */
    @Override
    public <Y> ListJoin<X, Y> join(ListAttribute<? super X, Y> list) {
        return null;
    }

    /**
     * Create an inner join to the specified Map-valued attribute.
     *
     * @param map target of the join
     * @return the resulting join
     */
    @Override
    public <K, V> MapJoin<X, K, V> join(MapAttribute<? super X, K, V> map) {
        return null;
    }

    /**
     * Create a join to the specified Collection-valued attribute
     * using the given join type.
     *
     * @param collection target of the join
     * @param jt         join type
     * @return the resulting join
     */
    @Override
    public <Y> CollectionJoin<X, Y> join(CollectionAttribute<? super X, Y> collection, JoinType jt) {
        return null;
    }

    /**
     * Create a join to the specified Set-valued attribute using
     * the given join type.
     *
     * @param set target of the join
     * @param jt  join type
     * @return the resulting join
     */
    @Override
    public <Y> SetJoin<X, Y> join(SetAttribute<? super X, Y> set, JoinType jt) {
        return null;
    }

    /**
     * Create a join to the specified List-valued attribute using
     * the given join type.
     *
     * @param list target of the join
     * @param jt   join type
     * @return the resulting join
     */
    @Override
    public <Y> ListJoin<X, Y> join(ListAttribute<? super X, Y> list, JoinType jt) {
        return null;
    }

    /**
     * Create a join to the specified Map-valued attribute using
     * the given join type.
     *
     * @param map target of the join
     * @param jt  join type
     * @return the resulting join
     */
    @Override
    public <K, V> MapJoin<X, K, V> join(MapAttribute<? super X, K, V> map, JoinType jt) {
        return null;
    }

    /**
     * Create an inner join to the specified attribute.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> Join<X1, Y> join(String attributeName) {
        return null;
    }

    /**
     * Create an inner join to the specified Collection-valued
     * attribute.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> CollectionJoin<X1, Y> joinCollection(String attributeName) {
        return null;
    }

    /**
     * Create an inner join to the specified Set-valued attribute.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> SetJoin<X1, Y> joinSet(String attributeName) {
        return null;
    }

    /**
     * Create an inner join to the specified List-valued attribute.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> ListJoin<X1, Y> joinList(String attributeName) {
        return null;
    }

    /**
     * Create an inner join to the specified Map-valued attribute.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, K, V> MapJoin<X1, K, V> joinMap(String attributeName) {
        return null;
    }

    /**
     * Create a join to the specified attribute using the given
     * join type.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @param jt            join type
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> Join<X1, Y> join(String attributeName, JoinType jt) {
        return null;
    }

    /**
     * Create a join to the specified Collection-valued attribute
     * using the given join type.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @param jt            join type
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> CollectionJoin<X1, Y> joinCollection(String attributeName, JoinType jt) {
        return null;
    }

    /**
     * Create a join to the specified Set-valued attribute using
     * the given join type.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @param jt            join type
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> SetJoin<X1, Y> joinSet(String attributeName, JoinType jt) {
        return null;
    }

    /**
     * Create a join to the specified List-valued attribute using
     * the given join type.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @param jt            join type
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> ListJoin<X1, Y> joinList(String attributeName, JoinType jt) {
        return null;
    }

    /**
     * Create a join to the specified Map-valued attribute using
     * the given join type.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @param jt            join type
     * @return the resulting join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, K, V> MapJoin<X1, K, V> joinMap(String attributeName, JoinType jt) {
        return null;
    }

    /**
     * Return the fetch joins that have been made from this type.
     * Returns empty set if no fetch joins have been made from
     * this type.
     * Modifications to the set do not affect the query.
     *
     * @return fetch joins made from this type
     */
    @Override
    public Set<Fetch<X, ?>> getFetches() {
        return null;
    }

    /**
     * Create a fetch join to the specified single-valued attribute
     * using an inner join.
     *
     * @param attribute target of the join
     * @return the resulting fetch join
     */
    @Override
    public <Y> Fetch<X, Y> fetch(SingularAttribute<? super X, Y> attribute) {
        return null;
    }

    /**
     * Create a fetch join to the specified single-valued attribute
     * using the given join type.
     *
     * @param attribute target of the join
     * @param jt        join type
     * @return the resulting fetch join
     */
    @Override
    public <Y> Fetch<X, Y> fetch(SingularAttribute<? super X, Y> attribute, JoinType jt) {
        return null;
    }

    /**
     * Create a fetch join to the specified collection-valued
     * attribute using an inner join.
     *
     * @param attribute target of the join
     * @return the resulting join
     */
    @Override
    public <Y> Fetch<X, Y> fetch(PluralAttribute<? super X, ?, Y> attribute) {
        return null;
    }

    /**
     * Create a fetch join to the specified collection-valued
     * attribute using the given join type.
     *
     * @param attribute target of the join
     * @param jt        join type
     * @return the resulting join
     */
    @Override
    public <Y> Fetch<X, Y> fetch(PluralAttribute<? super X, ?, Y> attribute, JoinType jt) {
        return null;
    }

    /**
     * Create a fetch join to the specified attribute using an
     * inner join.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @return the resulting fetch join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> Fetch<X1, Y> fetch(String attributeName) {
        return null;
    }

    /**
     * Create a fetch join to the specified attribute using
     * the given join type.
     *
     * @param attributeName name of the attribute for the
     *                      target of the join
     * @param jt            join type
     * @return the resulting fetch join
     * @throws IllegalArgumentException if attribute of the given
     *                                  name does not exist
     */
    @Override
    public <X1, Y> Fetch<X1, Y> fetch(String attributeName, JoinType jt) {
        return null;
    }

    /**
     * 返回条件的sql片段
     *
     * @return SQL片段
     */
    @Override
    public String getSql() {
        return new SQL().FROM(getModel().getName()).toString();
    }
}
