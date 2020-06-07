package javax.persistence;

import org.apache.ibatis.mapping.ParameterMapping;

public class MyjpaParameterImpl implements Parameter {
    private ParameterMapping mapping;

    public MyjpaParameterImpl(ParameterMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public String getName() {
        return mapping.getExpression();
    }

    @Override
    public Integer getPosition() {
        return null;
    }

    @Override
    public Class getParameterType() {
        return mapping.getJavaType().getClass();
    }
}

