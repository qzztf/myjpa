package cn.sexycode.mybatis.jpa.binding;

import cn.sexycode.mybatis.jpa.MyJpaException;
import cn.sexycode.mybatis.jpa.mapping.PersistentClass;
import cn.sexycode.mybatis.jpa.session.SessionFactory;
import cn.sexycode.mybatis.jpa.session.SessionFactoryBuilder;
import cn.sexycode.sql.dialect.function.SQLFunction;
import cn.sexycode.sql.mapping.Column;
import cn.sexycode.sql.mapping.Table;
import cn.sexycode.sql.model.Database;
import cn.sexycode.sql.model.Identifier;
import cn.sexycode.sql.type.TypeResolver;
import cn.sexycode.util.core.cls.XClass;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.*;

/**
 * An in-flight representation of Metadata while Metadata is being built.
 *
 *
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

    UUID getUUID();

    MetadataBuildingOptions getMetadataBuildingOptions();

    TypeResolver getTypeResolver();

    Database getDatabase();

    Map<String, SQLFunction> getSqlFunctionMap();

    void validate() throws MappingException;

    Set<MappedSuperclass> getMappedSuperclassMappingsCopy();

    SessionFactoryBuilder getSessionFactoryBuilder();

    SessionFactory buildSessionFactory();

    /**
     * Needed for SecondPass handling
     */
    Map<String, PersistentClass> getEntityBindingMap();

    void addIdentifierGenerator(IdentifierGeneratorDefinition generator);

    void addDefaultIdentifierGenerator(IdentifierGeneratorDefinition generator);

    Map<String, String> getImports();

    /**
     * Adds an import (HQL entity rename).
     *
     * @param entityName The entity name being renamed.
     * @param rename     The rename
     * @throws DuplicateMappingException If rename already is mapped to another
     *                                   entity name in this repository.
     */
    void addImport(String entityName, String rename) throws DuplicateMappingException;

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


    String getPhysicalColumnName(Table table, Identifier logicalName) throws MappingException;

    void addColumnNameBinding(Table table, String logicalName, Column column) throws DuplicateMappingException;

    void addColumnNameBinding(Table table, Identifier logicalName, Column column) throws DuplicateMappingException;

    String getPhysicalColumnName(Table table, String logicalName) throws MappingException;

    String getLogicalColumnName(Table table, Identifier physicalName);

    String getLogicalColumnName(Table table, String physicalName);


    void addMappedSuperclass(Class type, MappedSuperclass mappedSuperclass);

    MappedSuperclass getMappedSuperclass(Class type);


    boolean isInSecondPass();

    AnnotatedClassType getClassType(XClass clazz);

    AnnotatedClassType addClassType(XClass clazz);

    interface DelayedPropertyReferenceHandler extends Serializable {
        void process(InFlightMetadataCollector metadataCollector);
    }

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

}
