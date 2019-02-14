/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

/**
 * A custom type that may function as an identifier or discriminator type
 *
 * @author Gavin King
 */
public interface EnhancedUserType extends UserType {
    /**
     * Return an SQL literal representation of the value
     */
    String objectToSQLString(Object value);
}
