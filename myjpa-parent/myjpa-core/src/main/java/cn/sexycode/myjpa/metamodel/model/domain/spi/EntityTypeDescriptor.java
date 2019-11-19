package cn.sexycode.myjpa.metamodel.model.domain.spi;

import cn.sexycode.myjpa.metamodel.model.domain.EntityDomainType;

import javax.persistence.metamodel.EntityType;

/**
 * Hibernate extension to the JPA {@link EntityType} descriptor
 *
 * @author Steve Ebersole
 */
public interface EntityTypeDescriptor<J> extends EntityDomainType<J>, IdentifiableTypeDescriptor<J> {

}
