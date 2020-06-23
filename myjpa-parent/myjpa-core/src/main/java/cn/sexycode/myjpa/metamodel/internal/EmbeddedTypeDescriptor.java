package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.EmbeddableType;

/**
 * Hibernate extension to the JPA {@link EmbeddableType} descriptor
 *
 * @author Steve Ebersole
 */
public interface EmbeddedTypeDescriptor<J> extends EmbeddedDomainType<J>, ManagedTypeDescriptor<J> {

	ManagedTypeDescriptor<?> getParent();
}