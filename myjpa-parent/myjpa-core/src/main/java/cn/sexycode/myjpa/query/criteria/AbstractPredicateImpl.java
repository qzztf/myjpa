package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * @author qzz
 */
public abstract class AbstractPredicateImpl extends AbstractExpressionImpl<Boolean> implements Predicate {

    public AbstractPredicateImpl(CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder, Boolean.class);
    }

    /**
     * Return the boolean operator for the predicate.
     * If the predicate is simple, this is <code>AND</code>.
     *
     * @return boolean operator for the predicate
     */
    @Override
    public BooleanOperator getOperator() {
        return BooleanOperator.AND;
    }

    /**
     * Whether the predicate has been created from another
     * predicate by applying the <code>Predicate.not()</code> method
     * or the <code>CriteriaBuilder.not()</code> method.
     *
     * @return boolean indicating if the predicate is
     * a negated predicate
     */
    @Override
    public boolean isNegated() {
        return false;
    }

    /**
     * Return the top-level conjuncts or disjuncts of the predicate.
     * Returns empty list if there are no top-level conjuncts or
     * disjuncts of the predicate.
     * Modifications to the list do not affect the query.
     *
     * @return list of boolean expressions forming the predicate
     */
    @Override
    public List<Expression<Boolean>> getExpressions() {
        return null;
    }

    /**
     * Create a negation of the predicate.
     *
     * @return negated predicate
     */
    @Override
    public Predicate not() {
        return null;
    }
}
