package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RootImpl<X> implements Root<X> {

    CriteriaBuilder criteriaBuilder;
    EntityType<X> entityType;

    public RootImpl(CriteriaBuilder criteriaBuilder, EntityType<X> entityType) {
        this.criteriaBuilder = criteriaBuilder;
        this.entityType = entityType;
    }

    @Override
    public EntityType<X> getModel() {
        return null;
    }

    @Override
    public Path<?> getParentPath() {
        return null;
    }

    @Override
    public <Y> Path<Y> get(SingularAttribute<? super X, Y> attribute) {
        return null;
    }

    @Override
    public <E, C extends Collection<E>> Expression<C> get(PluralAttribute<X, C, E> collection) {
        return null;
    }

    @Override
    public <K, V, M extends Map<K, V>> Expression<M> get(MapAttribute<X, K, V> map) {
        return null;
    }

    @Override
    public Expression<Class<? extends X>> type() {
        return null;
    }

    @Override
    public <Y> Path<Y> get(String attributeName) {
        return null;
    }

    @Override
    public Set<Join<X, ?>> getJoins() {
        return null;
    }

    @Override
    public boolean isCorrelated() {
        return false;
    }

    @Override
    public From<X, X> getCorrelationParent() {
        return null;
    }

    @Override
    public <Y> Join<X, Y> join(SingularAttribute<? super X, Y> attribute) {
        return null;
    }

    @Override
    public <Y> Join<X, Y> join(SingularAttribute<? super X, Y> attribute, JoinType jt) {
        return null;
    }

    @Override
    public <Y> CollectionJoin<X, Y> join(CollectionAttribute<? super X, Y> collection) {
        return null;
    }

    @Override
    public <Y> SetJoin<X, Y> join(SetAttribute<? super X, Y> set) {
        return null;
    }

    @Override
    public <Y> ListJoin<X, Y> join(ListAttribute<? super X, Y> list) {
        return null;
    }

    @Override
    public <K, V> MapJoin<X, K, V> join(MapAttribute<? super X, K, V> map) {
        return null;
    }

    @Override
    public <Y> CollectionJoin<X, Y> join(CollectionAttribute<? super X, Y> collection, JoinType jt) {
        return null;
    }

    @Override
    public <Y> SetJoin<X, Y> join(SetAttribute<? super X, Y> set, JoinType jt) {
        return null;
    }

    @Override
    public <Y> ListJoin<X, Y> join(ListAttribute<? super X, Y> list, JoinType jt) {
        return null;
    }

    @Override
    public <K, V> MapJoin<X, K, V> join(MapAttribute<? super X, K, V> map, JoinType jt) {
        return null;
    }

    @Override
    public <X1, Y> Join<X1, Y> join(String attributeName) {
        return null;
    }

    @Override
    public <X1, Y> CollectionJoin<X1, Y> joinCollection(String attributeName) {
        return null;
    }

    @Override
    public <X1, Y> SetJoin<X1, Y> joinSet(String attributeName) {
        return null;
    }

    @Override
    public <X1, Y> ListJoin<X1, Y> joinList(String attributeName) {
        return null;
    }

    @Override
    public <X1, K, V> MapJoin<X1, K, V> joinMap(String attributeName) {
        return null;
    }

    @Override
    public <X1, Y> Join<X1, Y> join(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public <X1, Y> CollectionJoin<X1, Y> joinCollection(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public <X1, Y> SetJoin<X1, Y> joinSet(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public <X1, Y> ListJoin<X1, Y> joinList(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public <X1, K, V> MapJoin<X1, K, V> joinMap(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public Predicate isNull() {
        return null;
    }

    @Override
    public Predicate isNotNull() {
        return null;
    }

    @Override
    public Predicate in(Object... values) {
        return null;
    }

    @Override
    public Predicate in(Expression<?>... values) {
        return null;
    }

    @Override
    public Predicate in(Collection<?> values) {
        return null;
    }

    @Override
    public Predicate in(Expression<Collection<?>> values) {
        return null;
    }

    @Override
    public <X1> Expression<X1> as(Class<X1> type) {
        return null;
    }

    @Override
    public Set<Fetch<X, ?>> getFetches() {
        return null;
    }

    @Override
    public <Y> Fetch<X, Y> fetch(SingularAttribute<? super X, Y> attribute) {
        return null;
    }

    @Override
    public <Y> Fetch<X, Y> fetch(SingularAttribute<? super X, Y> attribute, JoinType jt) {
        return null;
    }

    @Override
    public <Y> Fetch<X, Y> fetch(PluralAttribute<? super X, ?, Y> attribute) {
        return null;
    }

    @Override
    public <Y> Fetch<X, Y> fetch(PluralAttribute<? super X, ?, Y> attribute, JoinType jt) {
        return null;
    }

    @Override
    public <X1, Y> Fetch<X1, Y> fetch(String attributeName) {
        return null;
    }

    @Override
    public <X1, Y> Fetch<X1, Y> fetch(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public Selection<X> alias(String name) {
        return null;
    }

    @Override
    public boolean isCompoundSelection() {
        return false;
    }

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        return null;
    }

    @Override
    public Class<? extends X> getJavaType() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
    }
}
