package cn.sexycode.myjpa.sql.type.descriptor.java;

import java.io.Serializable;

/**
 * Describes the mutability aspects of a Java type.  The term mutability refers to the fact that generally speaking
 * the aspects described by this contract are defined by whether the Java type's internal state is mutable or not.
 *
 * @author qzz
 */
public interface MutabilityPlan<T> extends Serializable {
    /**
     * Can the internal state of instances of <tt>T</tt> be changed?
     *
     * @return True if the internal state can be changed; false otherwise.
     */
    public boolean isMutable();

    /**
     * Return a deep copy of the value.
     *
     * @param value The value to deep copy
     * @return The deep copy.
     */
    public T deepCopy(T value);

    /**
     * Return a "disassembled" representation of the value.  This is used to push values onto the
     * second level cache.  Compliment to {@link #assemble}
     *
     * @param value The value to disassemble
     * @return The disassembled value.
     * @see #assemble
     */
    public Serializable disassemble(T value);

    /**
     * Assemble a previously {@linkplain #disassemble disassembled} value.  This is used when pulling values from the
     * second level cache.  Compliment to {@link #disassemble}
     *
     * @param cached The disassembled state
     * @return The re-assembled value.
     * @see #disassemble
     */
    public T assemble(Serializable cached);
}
