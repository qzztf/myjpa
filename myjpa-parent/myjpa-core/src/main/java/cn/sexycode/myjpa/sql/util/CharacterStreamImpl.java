package cn.sexycode.myjpa.sql.util;


import cn.sexycode.myjpa.sql.type.descriptor.java.DataHelper;
import cn.sexycode.myjpa.sql.util.CharacterStream;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Implementation of {@link cn.sexycode.myjpa.sql.util.CharacterStream}
 *
 */
public class CharacterStreamImpl implements CharacterStream {
    private final long length;

    private Reader reader;
    private String string;

    /**
     * Constructs a CharacterStreamImpl
     *
     * @param chars The String of characters to use backing the CharacterStream
     */
    public CharacterStreamImpl(String chars) {
        this.string = chars;
        this.length = chars.length();
    }

    /**
     * Constructs a CharacterStreamImpl
     *
     * @param reader The Reader containing the characters to use backing the CharacterStream
     * @param length The length of the stream
     */
    public CharacterStreamImpl(Reader reader, long length) {
        this.reader = reader;
        this.length = length;
    }

    @Override
    public Reader asReader() {
        if (reader == null) {
            reader = new StringReader(string);
        }
        return reader;
    }

    @Override
    public String asString() {
        if (string == null) {
            string = DataHelper.extractString(reader);
        }
        return string;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public void release() {
        if (reader == null) {
            return;
        }
        try {
            reader.close();
        } catch (IOException ignore) {
        }
    }
}
