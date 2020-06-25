package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.BasicType;
import java.util.Objects;

/**
 * Hibernate extension to the JPA {@link BasicType} contract.
 *
 * Describes the mapping between a Java type and a SQL type.
 *
 * @apiNote Again, like {@link CollectionDomainType} and
 * {@link EmbeddedDomainType}, this is a per-usage descriptor as it
 * encompasses both the Java and SQL types.
 *
 * @author Steve Ebersole
 */
public interface BasicDomainType<J> extends SimpleDomainType<J>, BasicType<J> {
	@Override
	default PersistenceType getPersistenceType() {
		return PersistenceType.BASIC;
	}

	default boolean areEqual(J x, J y) {
		return Objects.equals( x, y );
	}
}