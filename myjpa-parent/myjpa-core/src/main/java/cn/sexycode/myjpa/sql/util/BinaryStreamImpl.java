package cn.sexycode.myjpa.sql.util;

import cn.sexycode.myjpa.sql.util.BinaryStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of {@link cn.sexycode.myjpa.sql.util.BinaryStream}
 *
 */
public class BinaryStreamImpl extends ByteArrayInputStream implements BinaryStream {
    private final int length;

    /**
     * Constructs a BinaryStreamImpl
     *
     * @param bytes The bytes to use backing the stream
     */
    public BinaryStreamImpl(byte[] bytes) {
        super(bytes);
        this.length = bytes.length;
    }

    public InputStream getInputStream() {
        return this;
    }

    public byte[] getBytes() {
        // from ByteArrayInputStream
        return buf;
    }

    public long getLength() {
        return length;
    }

    @Override
    public void release() {
        try {
            super.close();
        } catch (IOException ignore) {
        }
    }
}
