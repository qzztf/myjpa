package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.mapping.Value;

/**
 * Represents an identifying key of a table: the value for primary key
 * of an entity, or a foreign key of a collection or join table or
 * joined subclass table.
 *
 */
public interface KeyValue extends Value {

    public void createForeignKeyOfEntity(String entityName);

    public boolean isCascadeDeleteEnabled();

    public String getNullValue();

    public boolean isUpdateable();
}
