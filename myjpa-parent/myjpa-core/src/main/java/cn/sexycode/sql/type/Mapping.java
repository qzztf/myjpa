/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

/**
 * Defines operations common to "compiled" mappings (ie. <tt>SessionFactory</tt>)
 * and "uncompiled" mappings (ie. <tt>Configuration</tt>) that are used by
 * implementors of <tt>Type</tt>.
 *
 * @author Gavin King
 * @see org.hibernate.type.Type
 * @see org.hibernate.internal.SessionFactoryImpl
 * @see org.hibernate.cfg.Configuration
 */
public interface Mapping {

    Type getIdentifierType(String className) throws TypeException;

    String getIdentifierPropertyName(String className) throws TypeException;

    Type getReferencedPropertyType(String className, String propertyName) throws TypeException;
}
