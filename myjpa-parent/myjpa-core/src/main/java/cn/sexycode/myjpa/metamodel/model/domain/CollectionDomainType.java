package cn.sexycode.myjpa.metamodel.model.domain;

/**
 * Descriptor for persistent collections.  This includes mapping
 * information, so it is specific to each usage (attribute).  JPA
 * has no construct as a type for collections
 *
 * @author Steve Ebersole
 */
public interface CollectionDomainType<C, E> extends DomainType<C> {
    interface Element<E> {
        /**
         * The Java type of the collection elements.
         */
        Class<E> getJavaType();
    }

    Element<E> getElementDescriptor();
}
