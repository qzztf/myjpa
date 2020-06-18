package cn.sexycode.myjpa.query.criteria;

import java.io.Serializable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

/**
 * Models a <tt>BETWEEN</tt> {@link javax.persistence.criteria.Predicate}.
 *
 * @author Steve Ebersole
 */
public class BetweenPredicate<Y> extends AbstractPredicateImpl implements Serializable {
    private final Expression<? extends Y> expression;

    private final Expression<? extends Y> lowerBound;

    private final Expression<? extends Y> upperBound;

    public BetweenPredicate(CriteriaBuilder criteriaBuilder, Expression<? extends Y> expression, Y lowerBound,
            Y upperBound) {
        this(criteriaBuilder, expression, criteriaBuilder.literal(lowerBound), criteriaBuilder.literal(upperBound));
    }

    public BetweenPredicate(CriteriaBuilder criteriaBuilder, Expression<? extends Y> expression,
            Expression<? extends Y> lowerBound, Expression<? extends Y> upperBound) {
        super(criteriaBuilder);
        this.expression = expression;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Expression<? extends Y> getExpression() {
        return expression;
    }

    public Expression<? extends Y> getLowerBound() {
        return lowerBound;
    }

    public Expression<? extends Y> getUpperBound() {
        return upperBound;
    }

    @Override
    public String getSql() {
        return String.format("%s %s %s and %s", ((Clause) getExpression()).getSql(), OP.BETWEEN.getSql(),
                ((Clause) getLowerBound()).getSql(), ((Clause) getUpperBound()).getSql());
    }
}
