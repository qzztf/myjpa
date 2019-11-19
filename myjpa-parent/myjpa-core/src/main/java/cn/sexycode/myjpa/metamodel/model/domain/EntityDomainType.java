package cn.sexycode.myjpa.metamodel.model.domain;

import javax.persistence.metamodel.EntityType;

/**
 * Extension to the JPA {@link EntityType} contract
 *
 * @author Steve Ebersole
 */
public interface EntityDomainType<J> extends IdentifiableDomainType<J>, EntityType<J> {
}
