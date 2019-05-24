package cn.sexycode.mybatis.jpa.binding;

import java.io.Serializable;

/**
 * 封装实体类和对应的statement
 *
 * @author qzz
 */

public class ModelProxy<T> implements Serializable {
    private final T model;
    private final String statement;

    public ModelProxy(T model, String statement) {
        this.model = model;
        this.statement = statement;
    }

    public T getModel() {
        return model;
    }

    public String getStatement() {
        return statement;
    }
}
