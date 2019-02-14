/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


import java.util.Comparator;

/**
 * Additional contract for types which may be used to version (and optimistic lock) data.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface VersionType<T> extends Type {

    /**
     * Get a comparator for version values.
     *
     * @return The comparator to use to compare different version values.
     */
    Comparator<T> getComparator();
}
