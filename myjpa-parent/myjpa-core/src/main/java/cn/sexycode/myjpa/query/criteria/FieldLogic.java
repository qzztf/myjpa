package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;

/**
 * 字段条件组合查询
 *
 * @author qzz
 */
public interface FieldLogic extends Clause, Predicate {
    default FieldLogic eq(String name, Object obj) {
        getClauses().add(new DefaultField(name, OP.EQUAL, obj));
        return this;
    }
    default FieldLogic neq(String name, Object obj) {
        getClauses().add(new DefaultField(name, OP.NOT_EQUAL, obj));
        return this;
    }

    default FieldLogic between(String name, Object x, Object y) {
        getClauses().add(new DefaultField(name, OP.BETWEEN, Arrays.asList(x,y)));
        return this;
    }

    default FieldLogic le(String name, Object obj) {
        getClauses().add(new DefaultField(name, OP.LESS_EQUAL, obj));
        return this;
    }

    default FieldLogic lt(String name, Object obj) {
        getClauses().add(new DefaultField(name, OP.LESS, obj));
        return this;
    }

    default FieldLogic ge(String name, Object obj) {
        getClauses().add(new DefaultField(name, OP.GREAT_EQUAL, obj));
        return this;
    }

    default FieldLogic gt(String name, Object obj) {
        getClauses().add(new DefaultField(name, OP.GREAT, obj));
        return this;
    }

    default FieldLogic isNull(String name) {
        getClauses().add(new DefaultField(name, OP.IS_NULL, null));
        return this;
    }

    /**
     * 字段条件
     *
     * @return 字段条件
     */
    List<Clause> getClauses();

    default FieldLogic isNotNull(String name){
        getClauses().add(new DefaultField(name, OP.NOTNULL, null));
        return this;
    }

    default FieldLogic in(String name, Object[] values){
        getClauses().add(new DefaultField(name, OP.IN, values));
        return this;
    }
}