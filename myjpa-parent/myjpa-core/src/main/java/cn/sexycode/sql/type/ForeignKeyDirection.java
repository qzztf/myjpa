/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


/**
 * Represents directionality of the foreign key constraint
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public enum ForeignKeyDirection {
    /**
     * A foreign key from child to parent
     */
    TO_PARENT,
    /**
     * A foreign key from parent to child
     */
    FROM_PARENT;
}
