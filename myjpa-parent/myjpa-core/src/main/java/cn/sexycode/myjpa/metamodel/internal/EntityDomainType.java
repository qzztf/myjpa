package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.EntityType;

public interface EntityDomainType<J> extends IdentifiableDomainType<J>, EntityType<J> {
}