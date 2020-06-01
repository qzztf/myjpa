package cn.sexycode.myjpa.query;

import javax.persistence.TypedQuery;

/**
 * @author qinzaizhen
 */
public interface MybatisQuery<X> extends TypedQuery<X> {
    /**
     * 设置参数
     * @param values
     */
    void setParameterValues(Object[] values);
}
