package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.MyjpaException;

/**
 * Enumeration of the types of sources of mapping metadata
 */
public enum MetadataSourceType {
    /**
     * Indicates metadata coming from either annotations, <tt>orx.xml</tt> or a combination of the two.
     */
    CLASS("class");

    private final String name;

    MetadataSourceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static MetadataSourceType parsePrecedence(String value) {

        if (CLASS.name.equalsIgnoreCase(value)) {
            return CLASS;
        }

        throw new MyjpaException("Unknown metadata source type value [" + value + "]");
    }
}
