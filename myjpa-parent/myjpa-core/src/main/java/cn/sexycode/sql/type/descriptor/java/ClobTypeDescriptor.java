/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type.descriptor.java;


import cn.sexycode.sql.CharacterStream;
import cn.sexycode.sql.CharacterStreamImpl;
import cn.sexycode.sql.type.TypeException;
import cn.sexycode.sql.type.descriptor.WrapperOptions;
import cn.sexycode.sql.util.ClobProxy;

import java.io.Reader;
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
 * @author Steve Ebersole
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
        return DataHelper.extractString(value);
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
