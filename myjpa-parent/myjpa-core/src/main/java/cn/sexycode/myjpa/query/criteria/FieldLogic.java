package cn.sexycode.myjpa.query.criteria;

import java.util.List;

/**
 * 字段条件组合查询
 *
 * @author qzz
 */
public interface FieldLogic extends WhereClause {
    default FieldLogic eq(String name, Object obj) {
        getWhereClauses().add(new DefaultField(name, OP.EQUAL, obj));
        return this;
    }

    default FieldLogic between(String name, List obj) {
        getWhereClauses().add(new DefaultField(name, OP.BETWEEN, obj));
        return this;
    }

    default FieldLogic le(String name, Object obj) {
        getWhereClauses().add(new DefaultField(name, OP.LESS_EQUAL, obj));
        return this;
    }

    default FieldLogic lt(String name, Object obj) {
        getWhereClauses().add(new DefaultField(name, OP.LESS, obj));
        return this;
    }

    default FieldLogic ge(String name, Object obj) {
        getWhereClauses().add(new DefaultField(name, OP.GREAT_EQUAL, obj));
        return this;
    }

    default FieldLogic gt(String name, Object obj) {
        getWhereClauses().add(new DefaultField(name, OP.GREAT, obj));
        return this;
    }

    default FieldLogic isNull(String name) {
        getWhereClauses().add(new DefaultField(name, OP.IS_NULL, null));
        return this;
    }

    /**
     * 字段条件
     *
     * @return 字段条件
     */
    List<WhereClause> getWhereClauses();

    default FieldLogic isNotNull(String name){
        getWhereClauses().add(new DefaultField(name, OP.NOTNULL, null));
        return this;
    }

    default FieldLogic in(String name, Object[] values){
        getWhereClauses().add(new DefaultField(name, OP.IN, values));
        return this;
    }
}