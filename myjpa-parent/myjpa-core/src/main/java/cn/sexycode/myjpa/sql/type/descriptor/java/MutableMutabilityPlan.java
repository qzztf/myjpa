package cn.sexycode.myjpa.sql.type.descriptor.java;

import cn.sexycode.myjpa.sql.type.descriptor.java.MutabilityPlan;

import java.io.Serializable;

/**
 * Mutability plan for mutable objects
 *
 * @author qzz
 */
public abstract class MutableMutabilityPlan<T> implements MutabilityPlan<T> {
    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(T value) {
        return (Serializable) deepCopy(value);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public T assemble(Serializable cached) {
        return deepCopy((T) cached);
    }

    @Override
    public final T deepCopy(T value) {
        return value == null ? null : deepCopyNotNull(value);
    }

    protected abstract T deepCopyNotNull(T value);
}
