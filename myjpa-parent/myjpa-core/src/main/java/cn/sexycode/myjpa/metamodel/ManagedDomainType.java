package cn.sexycode.myjpa.metamodel;

import javax.persistence.metamodel.ManagedType;

/**
 * @author Steve Ebersole
 */
public interface ManagedDomainType<J> extends SimpleDomainType<J>, ManagedType<J> {
}
