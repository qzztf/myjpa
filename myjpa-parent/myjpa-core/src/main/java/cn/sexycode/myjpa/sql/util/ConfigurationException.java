package cn.sexycode.myjpa.sql.util;

/**
 * TODO : javadoc
 *
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String string, Throwable root) {
        super(string, root);
    }

    public ConfigurationException(String s) {
        super(s);
    }
}
