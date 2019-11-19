package cn.sexycode.myjpa.metamodel.model.domain;

import javax.persistence.metamodel.IdentifiableType;

/**
 * Extension to the JPA {@link IdentifiableType} contract
 *
 * @author Steve Ebersole
 */
public interface IdentifiableDomainType<J> extends ManagedDomainType<J>, IdentifiableType<J> {
}
