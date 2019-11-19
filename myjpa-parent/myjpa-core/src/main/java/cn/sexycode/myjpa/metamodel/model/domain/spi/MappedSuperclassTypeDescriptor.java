package cn.sexycode.myjpa.metamodel.model.domain.spi;

import cn.sexycode.myjpa.metamodel.model.domain.MappedSuperclassDomainType;

import javax.persistence.metamodel.MappedSuperclassType;

/**
 * Hibernate extension to the JPA {@link MappedSuperclassType} descriptor
 *
 * @author Steve Ebersole
 */
public interface MappedSuperclassTypeDescriptor<J>
        extends MappedSuperclassDomainType<J>, IdentifiableTypeDescriptor<J> {
}
