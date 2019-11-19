/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package cn.sexycode.myjpa.metamodel.model.domain.spi;

import org.hibernate.metamodel.model.domain.EntityDomainType;

import javax.persistence.metamodel.EntityType;

/**
 * Hibernate extension to the JPA {@link EntityType} descriptor
 *
 * @author Steve Ebersole
 */
public interface EntityTypeDescriptor<J> extends EntityDomainType<J>, IdentifiableTypeDescriptor<J> {

}
