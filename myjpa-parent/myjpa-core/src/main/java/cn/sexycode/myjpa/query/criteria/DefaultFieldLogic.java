package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询条件组合
 * @author qzz
 */
public class DefaultFieldLogic extends AbstractPredicateImpl implements FieldLogic {

    /**
     * 查询字段组合列表
     */
    private List<WhereClause> whereClauses = new ArrayList<>();

    /**
     * 字段关系
     */
    private FieldRelation fieldRelation = FieldRelation.AND;
    private CriteriaBuilder criteriaBuilder;

    public DefaultFieldLogic() {
        super(null);
    }

    public DefaultFieldLogic(CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder);
        this.criteriaBuilder = criteriaBuilder;
    }

    public DefaultFieldLogic(FieldRelation fieldRelation) {
        super(null);
        this.fieldRelation = fieldRelation;
    }

    @Override
    public List<WhereClause> getWhereClauses() {
        return whereClauses;
    }

    @Override
    public String getSql() {
        if (whereClauses.size() == 0) {
            return "";
        }
        if (whereClauses.size() == 1 && !FieldRelation.NOT.equals(fieldRelation)) {
            return whereClauses.get(0).getSql();
        }

        StringBuilder sqlBuf = new StringBuilder("(");
        int i = 0;
        if (FieldRelation.NOT.equals(fieldRelation)) {
            sqlBuf.append(" NOT (");
            for (WhereClause clause : whereClauses) {
                if (i++ > 0) {
                    sqlBuf.append(" ").append(FieldRelation.AND).append(" ");
                }
                sqlBuf.append(clause.getSql());
            }
            sqlBuf.append(")");

            return sqlBuf.toString();
        }

        for (WhereClause clause : whereClauses) {
            if (i++ > 0) {
                sqlBuf.append(" ").append(fieldRelation).append(" ");
            }
            sqlBuf.append(clause.getSql());
        }
        sqlBuf.append(")");

        return sqlBuf.toString();
    }

    @Override
    public BooleanOperator getOperator() {
        if (fieldRelation == FieldRelation.AND) {
            return BooleanOperator.AND;
        } else if (fieldRelation == FieldRelation.OR) {
            return BooleanOperator.OR;
        }
        return null;
    }

    @Override
    public boolean isNegated() {
        return false;
    }

    @Override
    public List<Expression<Boolean>> getExpressions() {
        return whereClauses.stream().map(whereClause -> ((Expression<Boolean>) whereClause)).collect(Collectors.toList());
    }

    @Override
    public Predicate not() {
        fieldRelation = FieldRelation.NOT;
        return this;
    }

    @Override
    public Predicate isNull() {
        return this;
    }

    @Override
    public Predicate isNotNull() {
        return this;
    }

    @Override
    public Predicate in(Object... values) {
        return this;
    }

    @Override
    public Predicate in(Expression<?>... values) {
        return this;
    }

    @Override
    public Predicate in(Collection<?> values) {
        return this;
    }

    @Override
    public Predicate in(Expression<Collection<?>> values) {
        return this;
    }

}