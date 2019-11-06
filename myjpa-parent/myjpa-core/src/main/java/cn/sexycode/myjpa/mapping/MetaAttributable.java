package cn.sexycode.myjpa.mapping;

/**
 * Common interface for things that can handle meta attributes.
 *
 * @since 3.0.1
 */
public interface MetaAttributable {

    java.util.Map getMetaAttributes();

    void setMetaAttributes(java.util.Map metas);

    MetaAttribute getMetaAttribute(String name);

}
