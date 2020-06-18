package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

/**
 * @author qzz
 */
public class ComparisonPredicate extends AbstractPredicateImpl {
    Operation operation;

    Expression<?> left;

    Expression<?> right;

    public ComparisonPredicate(CriteriaBuilder criteriaBuilder, Operation operation, Expression<?> left,
            Expression<?> right) {
        super(criteriaBuilder);
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    public ComparisonPredicate(CriteriaBuilder criteriaBuilder, Operation operation, Expression<?> left, Object right) {
        super(criteriaBuilder);
        this.operation = operation;
        this.left = left;
        this.right = new LiteralExpression<>(criteriaBuilder, right);
    }

    /**
     * 返回条件的sql片段
     *
     * @return SQL片段
     */
    @Override
    public String getSql() {
        return String
                .format("%s %s %s", ((Clause) left).getSql(), operation.getSql(), ((Clause) right).getSql());
    }
}
