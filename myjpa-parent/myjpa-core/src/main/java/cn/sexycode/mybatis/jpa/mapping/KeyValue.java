/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;


import cn.sexycode.mybatis.jpa.binding.MappingException;

/**
 * Represents an identifying key of a table: the value for primary key
 * of an entity, or a foreign key of a collection or join table or
 * joined subclass table.
 *
 * @author Gavin King
 */
public interface KeyValue extends Value {

    public void createForeignKeyOfEntity(String entityName);

    public boolean isCascadeDeleteEnabled();

    public String getNullValue();

    public boolean isUpdateable();
}
