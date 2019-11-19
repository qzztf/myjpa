package cn.sexycode.myjpa.metamodel.model.domain;

import javax.persistence.metamodel.EmbeddableType;

/**
 * Hibernate extension to the JPA {@link } contract.
 *
 * @author Steve Ebersole
 * @apiNote Notice that this describes {@link javax.persistence.Embedded}, not
 * {@link javax.persistence.Embeddable} - like {@link CollectionDomainType}, it includes
 * mapping information per usage (attribute)
 */
public interface EmbeddedDomainType<J> extends SimpleDomainType<J>, EmbeddableType<J> {
}
