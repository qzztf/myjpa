/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.binding;

import cn.sexycode.mybatis.jpa.mapping.Column;
import cn.sexycode.mybatis.jpa.mapping.Identifier;
import cn.sexycode.mybatis.jpa.mapping.PersistentClass;
import cn.sexycode.mybatis.jpa.mapping.Table;

import javax.persistence.MappedSuperclass;
import javax.persistence.criteria.Join;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An in-flight representation of Metadata while Metadata is being built.
 *
 * @author Steve Ebersole
 * @since 5.0
 */
public interface InFlightMetadataCollector extends Metadata {

    /**
     * Add the PersistentClass for an entity mapping.
     *
     * @param persistentClass The entity metadata
     * @throws DuplicateMappingException Indicates there was already an entry
     *                                   corresponding to the given entity name.
     */
    void addEntityBinding(PersistentClass persistentClass) throws DuplicateMappingException;

    /**
     * Needed for SecondPass handling
     */
    Map<String, PersistentClass> getEntityBindingMap();

    /**
     * Adds table metadata to this repository returning the created
     * metadata instance.
     *
     * @param schema     The named schema in which the table belongs (or null).
     * @param catalog    The named catalog in which the table belongs (or null).
     * @param name       The table name
     * @param subselect  A select statement which defines a logical table, much
     *                   like a DB view.
     * @param isAbstract Is the table abstract (i.e. not really existing in the DB)?
     * @return The created table metadata, or the existing reference.
     */
    Table addTable(String schema, String catalog, String name, String subselect, boolean isAbstract);


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // stuff needed for annotation binding :(

    void addTableNameBinding(Identifier logicalName, Table table);

    void addTableNameBinding(
            String schema,
            String catalog,
            String logicalName,
            String realTableName,
            Table denormalizedSuperTable);

    String getLogicalTableName(Table ownerTable);

    String getPhysicalTableName(Identifier logicalName);

    String getPhysicalTableName(String logicalName);

    void addColumnNameBinding(Table table, Identifier logicalColumnName, Column column);

    void addColumnNameBinding(Table table, String logicalColumnName, Column column);

    String getPhysicalColumnName(Table table, Identifier logicalName) throws MappingException;

    String getPhysicalColumnName(Table table, String logicalName) throws MappingException;

    String getLogicalColumnName(Table table, Identifier physicalName);

    String getLogicalColumnName(Table table, String physicalName);


    void addMappedSuperclass(Class type, MappedSuperclass mappedSuperclass);

    MappedSuperclass getMappedSuperclass(Class type);


    boolean isInSecondPass();


    interface DelayedPropertyReferenceHandler extends Serializable {
        void process(InFlightMetadataCollector metadataCollector);
    }

    void addDelayedPropertyReferenceHandler(DelayedPropertyReferenceHandler handler);

    void addPropertyReference(String entityName, String propertyName);

    void addUniquePropertyReference(String entityName, String propertyName);

    void addPropertyReferencedAssociation(String s, String propertyName, String syntheticPropertyName);

    String getPropertyReferencedAssociation(String entityName, String mappedBy);

    void addMappedBy(String name, String mappedBy, String propertyName);

    String getFromMappedBy(String ownerEntityName, String propertyName);

    void addUniqueConstraints(Table table, List uniqueConstraints);


    class DuplicateSecondaryTableException extends MyJpaException {
        private final Identifier tableName;

        public DuplicateSecondaryTableException(Identifier tableName) {
            super(
                    String.format(
                            Locale.ENGLISH,
                            "Table with that name [%s] already associated with entity",
                            tableName.render()
                    )
            );
            this.tableName = tableName;
        }
    }

    Map<String, Join> getJoins(String entityName);
}
