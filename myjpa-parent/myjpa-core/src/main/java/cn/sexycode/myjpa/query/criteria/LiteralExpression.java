package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;

public class LiteralExpression<T> extends AbstractExpressionImpl<T> implements Serializable {

    private final Object literal;

    public LiteralExpression(CriteriaBuilder criteriaBuilder, T literal) {
        this(criteriaBuilder, (Class<T>) determineClass(literal), literal);
    }

    private static Class determineClass(Object literal) {
        return literal == null ? null : literal.getClass();
    }

    public LiteralExpression(CriteriaBuilder criteriaBuilder, Class<T> type, T literal) {
        super(criteriaBuilder, type);
        this.literal = literal;
    }

    public T getLiteral() {
        return (T) literal;
    }

    @Override
    public String getSql() {
        return literal.toString();
    }
}