package cn.sexycode.mybatis.jpa.binding;

/**
 * 封装实体类和对应的statement
 *
 * @author qzz
 */

public class ModelProxy<T> {
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
