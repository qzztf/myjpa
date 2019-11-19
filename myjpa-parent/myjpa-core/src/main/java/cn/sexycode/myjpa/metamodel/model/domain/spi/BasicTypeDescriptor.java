package cn.sexycode.myjpa.metamodel.model.domain.spi;

import cn.sexycode.myjpa.metamodel.model.domain.BasicDomainType;

import javax.persistence.metamodel.BasicType;

/**
 * Hibernate extension to the JPA {@link BasicType} descriptor
 *
 * @author Steve Ebersole
 */
public interface BasicTypeDescriptor<J> extends BasicDomainType<J>, SimpleTypeDescriptor<J> {
}
