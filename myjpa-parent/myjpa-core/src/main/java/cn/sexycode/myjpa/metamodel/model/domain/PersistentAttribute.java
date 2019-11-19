package cn.sexycode.myjpa.metamodel.model.domain;

import javax.persistence.metamodel.Attribute;

/**
 * Hibernate extension to the JPA {@link Attribute} contract
 *
 * @author Steve Ebersole
 */
public interface PersistentAttribute<D, J> extends Attribute<D, J> {
    @Override
    ManagedDomainType<D> getDeclaringType();

    SimpleDomainType<?> getValueGraphType();

    SimpleDomainType<?> getKeyGraphType();
}
