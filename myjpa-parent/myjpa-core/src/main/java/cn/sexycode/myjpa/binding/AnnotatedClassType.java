package cn.sexycode.myjpa.binding;

/**
 * Type of annotation of a class will give its type
 */
public enum AnnotatedClassType {
    /**
     * has no revelent top level annotation
     */
    NONE,
    /**
     * has @Entity annotation
     */
    ENTITY,
    /**
     * has a @Embeddable annotation
     */
    EMBEDDABLE,
    /**
     * has @EmbeddedSuperclass annotation
     */
    EMBEDDABLE_SUPERCLASS
}
