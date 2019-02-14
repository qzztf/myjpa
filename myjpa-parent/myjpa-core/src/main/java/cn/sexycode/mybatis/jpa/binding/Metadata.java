/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.binding;

import cn.sexycode.mybatis.jpa.SessionFactoryBuilder;
import cn.sexycode.mybatis.jpa.mapping.PersistentClass;
import cn.sexycode.mybatis.jpa.session.SessionFactory;

import java.util.UUID;

/**
 * Represents the ORM model as determined from all provided mapping sources.
 * <p>
 * NOTE : for the time being this is essentially a copy of the legacy Mappings contract, split between
 * reading the mapping information exposed here and collecting it via InFlightMetadataCollector
 *
 * @author Steve Ebersole
 * @since 5.0
 */
public interface Metadata {

    /**
     * Retrieves the PersistentClass entity metadata representation for known all entities.
     * <p>
     * Returned collection is immutable
     *
     * @return All PersistentClass representations.
     */
    java.util.Collection<PersistentClass> getEntityBindings();

    /**
     * Retrieves the PersistentClass entity mapping metadata representation for
     * the given entity name.
     *
     * @param entityName The entity name for which to retrieve the metadata.
     * @return The entity mapping metadata, or {@code null} if no matching entity found.
     */
    PersistentClass getEntityBinding(String entityName);


}
