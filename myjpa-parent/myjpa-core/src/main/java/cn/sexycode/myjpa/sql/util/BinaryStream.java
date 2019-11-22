package cn.sexycode.myjpa.sql.util;

import java.io.InputStream;

/**
 * Wraps a binary stream to also provide the length which is needed when binding.
 *
 */
public interface BinaryStream {
    /**
     * Retrieve the input stream.
     *
     * @return The input stream
     */
    public InputStream getInputStream();

    /**
     * Access to the bytes.
     *
     * @return The bytes.
     */
    public byte[] getBytes();

    /**
     * Retrieve the length of the input stream
     *
     * @return The input stream length
     */
    public long getLength();

    /**
     * Release any underlying resources.
     */
    public void release();
}
