package cn.sexycode.myjpa.query.criteria.internal.expression;

import javax.persistence.criteria.Expression;

/**
 * Contract for operators with two operands.
 *
 * @author Steve Ebersole
 */
public interface BinaryOperatorExpression<T> extends Expression<T> {
    /**
     * Get the right-hand operand.
     *
     * @return The right-hand operand.
     */
    public Expression<?> getRightHandOperand();

    /**
     * Get the left-hand operand.
     *
     * @return The left-hand operand.
     */
    public Expression<?> getLeftHandOperand();
}
