package cn.sexycode.myjpa.sql.type.descriptor.java;

import cn.sexycode.myjpa.sql.type.descriptor.java.MutabilityPlan;

import java.io.Serializable;

/**
 * Mutability plan for immutable objects
 *
 * @author qzz
 */
public class ImmutableMutabilityPlan<T> implements MutabilityPlan<T> {
    public static final ImmutableMutabilityPlan INSTANCE = new ImmutableMutabilityPlan();

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public T deepCopy(T value) {
        return value;
    }

    @Override
    public Serializable disassemble(T value) {
        return (Serializable) value;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public T assemble(Serializable cached) {
        return (T) cached;
    }
}
