package cn.sexycode.myjpa.query;

import cn.sexycode.sql.type.Type;

import javax.persistence.Parameter;

/**
 * NOTE: Consider this contract (and its sub-contracts) as incubating as we transition to 6.0 and SQM
 *
 * @author Steve Ebersole
 */
public abstract class AbstractParameterDescriptor implements Parameter {

    private final int sourceLocation;

    private Type expectedType;

    public AbstractParameterDescriptor(int sourceLocation, Type expectedType) {
        this.sourceLocation = sourceLocation;
        this.expectedType = expectedType;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getPosition() {
        return null;
    }

    @Override
    public Class getParameterType() {
        return expectedType == null ? null : expectedType.getReturnedClass();
    }

    public Type getExpectedType() {
        return expectedType;
    }

    public void resetExpectedType(Type expectedType) {
        this.expectedType = expectedType;
    }
}
