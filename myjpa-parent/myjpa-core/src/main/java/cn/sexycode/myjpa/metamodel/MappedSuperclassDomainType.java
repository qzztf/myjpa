package cn.sexycode.myjpa.metamodel;

import javax.persistence.metamodel.MappedSuperclassType;

/**
 * Extension of the JPA {@link MappedSuperclassType} contract
 *
 * @author Steve Ebersole
 */
public interface MappedSuperclassDomainType<J> extends IdentifiableDomainType<J>, MappedSuperclassType<J> {
}
