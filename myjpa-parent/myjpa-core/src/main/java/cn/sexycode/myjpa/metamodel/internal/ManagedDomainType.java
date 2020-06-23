package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.ManagedType;

public interface ManagedDomainType<J> extends SimpleDomainType<J>, ManagedType<J> {
}