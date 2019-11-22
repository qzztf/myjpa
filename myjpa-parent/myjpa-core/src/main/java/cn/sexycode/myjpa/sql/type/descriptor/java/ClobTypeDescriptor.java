package cn.sexycode.myjpa.sql.type.descriptor.java;


import cn.sexycode.myjpa.sql.type.TypeException;
import cn.sexycode.myjpa.sql.type.descriptor.WrapperOptions;
import cn.sexycode.myjpa.sql.type.descriptor.java.AbstractTypeDescriptor;
import cn.sexycode.myjpa.sql.type.descriptor.java.DataHelper;
import cn.sexycode.myjpa.sql.type.descriptor.java.IncomparableComparator;
import cn.sexycode.myjpa.sql.type.descriptor.java.MutabilityPlan;
import cn.sexycode.myjpa.sql.util.ClobProxy;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * Descriptor for {@link Clob} handling.
 * <p/>
 * Note, {@link Clob clobs} really are mutable (their internal state can in fact be mutated).  We simply
 * treat them as immutable because we cannot properly check them for changes nor deep copy them.
 *
 * @author qzz
 */
public class ClobTypeDescriptor extends AbstractTypeDescriptor<Clob> {
    public static final ClobTypeDescriptor INSTANCE = new ClobTypeDescriptor();

    public static class ClobMutabilityPlan implements MutabilityPlan<Clob> {
        public static final ClobMutabilityPlan INSTANCE = new ClobMutabilityPlan();

        public boolean isMutable() {
            return false;
        }

        public Clob deepCopy(Clob value) {
            return value;
        }

        public Serializable disassemble(Clob value) {
            throw new UnsupportedOperationException("Clobs are not cacheable");
        }

        public Clob assemble(Serializable cached) {
            throw new UnsupportedOperationException("Clobs are not cacheable");
        }
    }

    public ClobTypeDescriptor() {
        super(Clob.class, ClobMutabilityPlan.INSTANCE);
    }

    @Override
    public String extractLoggableRepresentation(Clob value) {
        return value == null ? "null" : "{clob}";
    }

    public String toString(Clob value) {
        return cn.sexycode.myjpa.sql.type.descriptor.java.DataHelper.extractString(value);
    }

    public Clob fromString(String string) {
        return ClobProxy.generateProxy(string);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Comparator<Clob> getComparator() {
        return IncomparableComparator.INSTANCE;
    }

    @Override
    public int extractHashCode(Clob value) {
        return System.identityHashCode(value);
    }

    @Override
    public boolean areEqual(Clob one, Clob another) {
        return one == another;
    }

    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(final Clob value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        try {

            if (String.class.isAssignableFrom(type)) {
                return (X) DataHelper.extractString(value.getCharacterStream());
            }
        } catch (SQLException e) {
            throw new TypeException("Unable to access clob stream", e);
        }

        throw unknownUnwrap(type);
    }

    public <X> Clob wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        throw unknownWrap(value.getClass());
    }
}
