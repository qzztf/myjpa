/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.boot;

/**
 * Context for determining the implicit name of an entity's tenant identifier
 * column.
 *
 * @author Steve Ebersole
 */
public interface ImplicitTenantIdColumnNameSource extends ImplicitNameSource {
    /**
     * Access the entity name information
     *
     * @return The entity name information
     */
    EntityNaming getEntityNaming();
}
