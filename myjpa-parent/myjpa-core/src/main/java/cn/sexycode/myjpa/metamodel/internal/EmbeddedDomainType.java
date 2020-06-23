package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.EmbeddableType;

/**
 * Hibernate extension to the JPA {@link } contract.
 *
 * @apiNote Notice that this describes {@link javax.persistence.Embedded}, not
 * {@link javax.persistence.Embeddable} - like {@link CollectionDomainType}, it includes
 * mapping information per usage (attribute)
 *
 * @author Steve Ebersole
 */
public interface EmbeddedDomainType<J> extends SimpleDomainType<J>, EmbeddableType<J> {
}