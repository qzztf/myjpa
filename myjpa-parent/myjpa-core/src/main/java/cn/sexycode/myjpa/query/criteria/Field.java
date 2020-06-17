package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.Expression;

/**
 * 字段接口类。
 *
 * @author qzz
 */
public interface Field<T> extends WhereClause, Expression<T> {
    /**
     * 返回字段名
     *
     * @return String
     */
    String getField();

    /**
     * 比较符
     *
     * @return 比较符
     */
    OP getCompare();

    /**
     * 返回值
     *
     * @return 值
     */
    Object getValue();

}