package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.AbstractSingleColumnStandardBasicType;
import cn.sexycode.myjpa.sql.type.AbstractStandardBasicType;
import cn.sexycode.myjpa.sql.type.descriptor.java.ImmutableMutabilityPlan;
import cn.sexycode.myjpa.sql.type.descriptor.java.MutabilityPlan;

/**
 * Optimize a mutable type, if the user promises not to mutable the
 * instances.
 *
 */
public class AdaptedImmutableType<T> extends AbstractSingleColumnStandardBasicType<T> {
    private final AbstractStandardBasicType<T> baseMutableType;

    public AdaptedImmutableType(AbstractStandardBasicType<T> baseMutableType) {
        super(baseMutableType.getSqlTypeDescriptor(), baseMutableType.getJavaTypeDescriptor());
        this.baseMutableType = baseMutableType;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected MutabilityPlan<T> getMutabilityPlan() {
        return ImmutableMutabilityPlan.INSTANCE;
    }

    @Override
    public String getName() {
        return "imm_" + baseMutableType.getName();
    }
}
