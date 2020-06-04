package cn.sexycode.myjpa.mybatis;

public class NoSuchMapperMethodException extends NoSuchMethodException{
    /**
     * Constructs a <code>NoSuchMethodException</code> without a detail message.
     */
    public NoSuchMapperMethodException() {
    }

    /**
     * Constructs a <code>NoSuchMethodException</code> with a detail message.
     *
     * @param s the detail message.
     */
    public NoSuchMapperMethodException(String s) {
        super(s);
    }


}
