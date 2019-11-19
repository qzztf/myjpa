/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package cn.sexycode.myjpa.metamodel.model.domain;

import org.hibernate.HibernateException;

import javax.persistence.metamodel.BasicType;
import java.util.Objects;

/**
 * Hibernate extension to the JPA {@link BasicType} contract.
 * <p>
 * Describes the mapping between a Java type and a SQL type.
 *
 * @author Steve Ebersole
 * @apiNote Again, like {@link CollectionDomainType} and
 * {@link EmbeddedDomainType}, this is a per-usage descriptor as it
 * encompasses both the Java and SQL types.
 */
public interface BasicDomainType<J> extends SimpleDomainType<J>, BasicType<J> {
    @Override
    default PersistenceType getPersistenceType() {
        return PersistenceType.BASIC;
    }

    default boolean areEqual(J x, J y) throws HibernateException {
        return Objects.equals(x, y);
    }
}
