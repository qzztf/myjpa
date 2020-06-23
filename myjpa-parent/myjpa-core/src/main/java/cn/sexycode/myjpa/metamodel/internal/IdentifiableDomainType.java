package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.IdentifiableType;

public interface IdentifiableDomainType<J> extends ManagedDomainType<J>, IdentifiableType<J> {
}