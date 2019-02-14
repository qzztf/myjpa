/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

/**
 * Thrown when a property cannot be serializaed/deserialized
 *
 * @author Gavin King
 */
public class SerializationException extends RuntimeException {

    public SerializationException(String message, Exception root) {
        super(message, root);
    }

}