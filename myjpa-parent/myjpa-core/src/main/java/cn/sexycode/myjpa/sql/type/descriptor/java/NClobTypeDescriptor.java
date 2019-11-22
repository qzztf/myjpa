package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.DataHelper;
import cn.sexycode.myjpa.sql.type.descriptor.java.IncomparableComparator;
import cn.sexycode.myjpa.sql.type.descriptor.java.MutabilityPlan;
import cn.sexycode.myjpa.sql.util.CharacterStream;
import cn.sexycode.myjpa.sql.util.CharacterStreamImpl;
import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.util.NClobProxy;

import java.io.Serializable;
import java.sql.NClob;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * Descriptor for {@link NClob} handling.
 * <p/>
 * Note, {@link NClob nclobs} really are mutable (their internal state can in fact be mutated).  We simply
 * treat them as immutable because we cannot properly check them for changes nor deep copy them.
 *
 * @author qzz
 */
public class NClobTypeDescriptor extends AbstractTypeDescriptor<NClob> {
    public static final NClobTypeDescriptor INSTANCE = new NClobTypeDescriptor();

    public static class NClobMutabilityPlan implements MutabilityPlan<NClob> {
        public static final NClobMutabilityPlan INSTANCE = new NClobMutabilityPlan();

        public boolean isMutable() {
            return false;
        }

        public NClob deepCopy(NClob value) {
            return value;
        }

        public Serializable disassemble(NClob value) {
            throw new UnsupportedOperationException("Clobs are not cacheable");
        }

        public NClob assemble(Serializable cached) {
            throw new UnsupportedOperationException("Clobs are not cacheable");
        }
    }

    public NClobTypeDescriptor() {
        super(NClob.class, NClobMutabilityPlan.INSTANCE);
    }

    @Override
    public String extractLoggableRepresentation(NClob value) {
        return value == null ? "null" : "{nclob}";
    }

    public String toString(NClob value) {
        return cn.sexycode.myjpa.sql.type.descriptor.java.DataHelper.extractString(value);
    }

    public NClob fromString(String string) {
        return NClobProxy.generateProxy(string);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Comparator<NClob> getComparator() {
        return IncomparableComparator.INSTANCE;
    }

    @Override
    public int extractHashCode(NClob value) {
        return System.identityHashCode(value);
    }

    @Override
    public boolean areEqual(NClob one, NClob another) {
        return one == another;
    }

    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(final NClob value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        try {
            if (CharacterStream.class.isAssignableFrom(type)) {

                    // otherwise we need to build a BinaryStream...
                    return (X) new CharacterStreamImpl(DataHelper.extractString(value.getCharacterStream()));

            }
        } catch (SQLException e) {
            throw new TypeException("Unable to access nclob stream", e);
        }

        throw unknownUnwrap(type);
    }

    public <X> NClob wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }


        throw unknownWrap(value.getClass());
    }
}
