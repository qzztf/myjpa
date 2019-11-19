package cn.sexycode.myjpa.metamodel.model.domain.spi;

import cn.sexycode.myjpa.metamodel.model.domain.EmbeddedDomainType;

import javax.persistence.metamodel.EmbeddableType;

/**
 * Hibernate extension to the JPA {@link EmbeddableType} descriptor
 *
 * @author Steve Ebersole
 */
public interface EmbeddedTypeDescriptor<J> extends EmbeddedDomainType<J>, ManagedTypeDescriptor<J> {
    //    ComponentType getHibernateType();

    ManagedTypeDescriptor<?> getParent();
}
