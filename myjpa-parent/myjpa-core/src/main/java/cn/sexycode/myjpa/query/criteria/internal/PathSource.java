/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal;

import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;

import javax.persistence.criteria.Path;

/**
 * Implementation contract for things which can be the source (parent, left-hand-side, etc) of a path
 *
 * @author Steve Ebersole
 */
public interface PathSource<X> extends Path<X> {
    public void prepareAlias(RenderingContext renderingContext);

    /**
     * Get the string representation of this path as a navigation from one of the
     * queries <tt>identification variables</tt>
     *
     * @return The path's identifier.
     */
    public String getPathIdentifier();
}
