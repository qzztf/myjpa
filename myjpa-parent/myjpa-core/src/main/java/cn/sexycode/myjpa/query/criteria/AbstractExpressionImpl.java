package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import java.util.Collection;
import java.util.List;

public abstract class AbstractExpressionImpl<T> implements Expression<T>, Clause {

    private CriteriaBuilder criteriaBuilder;
    private Class<T> javaType;

    public AbstractExpressionImpl(CriteriaBuilder criteriaBuilder, Class<T> javaType) {
        this.criteriaBuilder = criteriaBuilder;
        this.javaType = javaType;
    }

    public CriteriaBuilder criteriaBuilder(){
        return criteriaBuilder;
    }

    /**
     * Create a predicate to test whether the expression is null.
     *
     * @return predicate testing whether the expression is null
     */
    @Override
    public Predicate isNull() {
        return criteriaBuilder().isNull(this);
    }

    /**
     * Create a predicate to test whether the expression is
     * not null.
     *
     * @return predicate testing whether the expression is not null
     */
    @Override
    public Predicate isNotNull() {
        return null;
    }

    /**
     * Create a predicate to test whether the expression is a member
     * of the argument list.
     *
     * @param values values to be tested against
     * @return predicate testing for membership
     */
    @Override
    public Predicate in(Object... values) {
        return null;
    }

    /**
     * Create a predicate to test whether the expression is a member
     * of the argument list.
     *
     * @param values expressions to be tested against
     * @return predicate testing for membership
     */
    @Override
    public Predicate in(Expression<?>... values) {
        return null;
    }

    /**
     * Create a predicate to test whether the expression is a member
     * of the collection.
     *
     * @param values collection of values to be tested against
     * @return predicate testing for membership
     */
    @Override
    public Predicate in(Collection<?> values) {
        return null;
    }

    /**
     * Create a predicate to test whether the expression is a member
     * of the collection.
     *
     * @param values expression corresponding to collection to be
     *               tested against
     * @return predicate testing for membership
     */
    @Override
    public Predicate in(Expression<Collection<?>> values) {
        return null;
    }

    /**
     * Perform a typecast upon the expression, returning a new
     * expression object.
     * This method does not cause type conversion:
     * the runtime type is not changed.
     * Warning: may result in a runtime failure.
     *
     * @param type intended type of the expression
     * @return new expression of the given type
     */
    @Override
    public <X> Expression<X> as(Class<X> type) {
        return null;
    }

    /**
     * Assigns an alias to the selection item.
     * Once assigned, an alias cannot be changed or reassigned.
     * Returns the same selection item.
     *
     * @param name alias
     * @return selection item
     */
    @Override
    public Selection<T> alias(String name) {
        return null;
    }

    /**
     * Whether the selection item is a compound selection.
     *
     * @return boolean indicating whether the selection is a compound
     * selection
     */
    @Override
    public boolean isCompoundSelection() {
        return false;
    }

    /**
     * Return the selection items composing a compound selection.
     * Modifications to the list do not affect the query.
     *
     * @return list of selection items
     * @throws IllegalStateException if selection is not a
     *                               compound selection
     */
    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        return null;
    }

    /**
     * Return the Java type of the tuple element.
     *
     * @return the Java type of the tuple element
     */
    @Override
    public Class<? extends T> getJavaType() {
        return javaType;
    }

    /**
     * Return the alias assigned to the tuple element or null,
     * if no alias has been assigned.
     *
     * @return alias
     */
    @Override
    public String getAlias() {
        return null;
    }
}
