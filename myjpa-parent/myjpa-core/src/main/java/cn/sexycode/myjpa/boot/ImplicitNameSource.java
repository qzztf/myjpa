/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.boot;

import cn.sexycode.myjpa.binding.MetadataBuildingContext;

/**
 * Common contract for all implicit naming sources
 *
 * @author Steve Ebersole
 */
public interface ImplicitNameSource {
    /**
     * Access to the current building context.
     *
     * @return The building context
     */
    MetadataBuildingContext getBuildingContext();
}
