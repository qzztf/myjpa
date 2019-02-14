/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.binding;


/**
 * Describes the context in which the process of building Metadata out of MetadataSources occurs.
 * <p>
 * BindingContext are generally hierarchical getting more specific as we "go
 * down".  E.g.  global -> PU -> document -> mapping
 *
 * @author Steve Ebersole
 * @since 5.0
 */
public interface MetadataBuildingContext {
    /**
     * Access to the collector of metadata as we build it.
     *
     * @return The metadata collector.
     */
    public InFlightMetadataCollector getMetadataCollector();
}
