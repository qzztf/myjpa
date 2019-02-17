/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.binding;


import cn.sexycode.mybatis.jpa.mapping.*;
import cn.sexycode.mybatis.jpa.util.CollectionHelper;
import cn.sexycode.sql.dialect.Dialect;

import javax.persistence.MappedSuperclass;
import javax.persistence.criteria.Join;
import java.io.Serializable;
import java.util.*;

/**
 * The implementation of the in-flight Metadata collector contract.
 * <p>
 * The usage expectation is that this class is used until all Metadata info is
 * collected and then {@link #buildMetadataInstance} is called to generate
 * the complete (and immutable) Metadata object.
 *
 * @author Steve Ebersole
 */
public class InFlightMetadataCollectorImpl implements InFlightMetadataCollector {

    private final UUID uuid;

    private final Map<String, PersistentClass> entityBindingMap = new HashMap<String, PersistentClass>();
    private final Map<String, Collection> collectionBindingMap = new HashMap<String, Collection>();

    private final Map<String, String> imports = new HashMap<String, String>();


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // All the annotation-processing-specific state :(
    private final Set<String> defaultIdentifierGeneratorNames = new HashSet<String>();
    private final Set<String> defaultNamedQueryNames = new HashSet<String>();
    private final Set<String> defaultNamedNativeQueryNames = new HashSet<String>();
    private final Set<String> defaultSqlResultSetMappingNames = new HashSet<String>();
    private final Set<String> defaultNamedProcedureNames = new HashSet<String>();
    private Map<Class, MappedSuperclass> mappedSuperClasses;
    private Map<String, String> mappedByResolver;
    private Map<String, String> propertyRefResolver;
    private Set<DelayedPropertyReferenceHandler> delayedPropertyReferenceHandlers;

    public InFlightMetadataCollectorImpl() {
        this.uuid = UUID.randomUUID();
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Entity handling

    @Override
    public java.util.Collection<PersistentClass> getEntityBindings() {
        return entityBindingMap.values();
    }

    @Override
    public Map<String, PersistentClass> getEntityBindingMap() {
        return entityBindingMap;
    }

    @Override
    public PersistentClass getEntityBinding(String entityName) {
        return entityBindingMap.get(entityName);
    }

    @Override
    public void addEntityBinding(PersistentClass persistentClass) throws DuplicateMappingException {
        final String entityName = persistentClass.getEntityName();
        if (entityBindingMap.containsKey(entityName)) {
            throw new DuplicateMappingException(DuplicateMappingException.Type.ENTITY, entityName);
        }
        entityBindingMap.put(entityName, persistentClass);
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Table handling

    @Override
    public cn.sexycode.mybatis.jpa.mapping.Table addTable(
            String schemaName,
            String catalogName,
            String name,
            String subselectFragment,
            boolean isAbstract) {


        // annotation binding depends on the "table name" for @Subselect bindings
        // being set into the generated table (mainly to avoid later NPE), but for now we need to keep that :(
        final Identifier logicalName;
        if (name != null) {
//			logicalName = getDatabase().toIdentifier( name );
        } else {
            logicalName = null;
        }

        if (subselectFragment != null) {
            return new cn.sexycode.mybatis.jpa.mapping.Table(namespace, logicalName, subselectFragment, isAbstract);
        } else {
            cn.sexycode.mybatis.jpa.mapping.Table table = namespace.locateTable(logicalName);
            if (table != null) {
                if (!isAbstract) {
                    table.setAbstract(false);
                }
                return table;
            }
            return namespace.createTable(logicalName, isAbstract);
        }
    }

    private Map<Identifier, Identifier> logicalToPhysicalTableNameMap = new HashMap<Identifier, Identifier>();
    private Map<Identifier, Identifier> physicalToLogicalTableNameMap = new HashMap<Identifier, Identifier>();

    @Override
    public void addTableNameBinding(Identifier logicalName, cn.sexycode.mybatis.jpa.mapping.Table table) {
        logicalToPhysicalTableNameMap.put(logicalName, table.getNameIdentifier());
        physicalToLogicalTableNameMap.put(table.getNameIdentifier(), logicalName);
    }

    @Override
    public void addTableNameBinding(String schema, String catalog, String logicalName, String realTableName, cn.sexycode.mybatis.jpa.mapping.Table denormalizedSuperTable) {
        final Identifier logicalNameIdentifier = getDatabase().toIdentifier(logicalName);
        final Identifier physicalNameIdentifier = getDatabase().toIdentifier(realTableName);

        logicalToPhysicalTableNameMap.put(logicalNameIdentifier, physicalNameIdentifier);
        physicalToLogicalTableNameMap.put(physicalNameIdentifier, logicalNameIdentifier);
    }

    @Override
    public String getLogicalTableName(cn.sexycode.mybatis.jpa.mapping.Table ownerTable) {
        final Identifier logicalName = physicalToLogicalTableNameMap.get(ownerTable.getNameIdentifier());
        if (logicalName == null) {
            throw new MappingException("Unable to find physical table: " + ownerTable.getName());
        }
        return logicalName.render();
    }

    @Override
    public String getPhysicalTableName(Identifier logicalName) {
        final Identifier physicalName = logicalToPhysicalTableNameMap.get(logicalName);
        return physicalName == null ? null : physicalName.render();
    }

    @Override
    public String getPhysicalTableName(String logicalName) {
        return getPhysicalTableName(getDatabase().toIdentifier(logicalName));
    }

    /**
     * Internal struct used to maintain xref between physical and logical column
     * names for a table.  Mainly this is used to ensure that the defined NamingStrategy
     * is not creating duplicate column names.
     */
    private class TableColumnNameBinding implements Serializable {
        private final String tableName;
        private Map<Identifier, String> logicalToPhysical = new HashMap<Identifier, String>();
        private Map<String, Identifier> physicalToLogical = new HashMap<String, Identifier>();

        private TableColumnNameBinding(String tableName) {
            this.tableName = tableName;
        }

        public void addBinding(Identifier logicalName, cn.sexycode.mybatis.jpa.mapping.Column physicalColumn) {
            final String physicalNameString = physicalColumn.getQuotedName(getDatabase().getJdbcEnvironment().getDialect());

            bindLogicalToPhysical(logicalName, physicalNameString);
            bindPhysicalToLogical(logicalName, physicalNameString);
        }

        private void bindLogicalToPhysical(Identifier logicalName, String physicalName) throws DuplicateMappingException {
            final String existingPhysicalNameMapping = logicalToPhysical.put(logicalName, physicalName);
            if (existingPhysicalNameMapping != null) {
                final boolean areSame = logicalName.isQuoted()
                        ? physicalName.equals(existingPhysicalNameMapping)
                        : physicalName.equalsIgnoreCase(existingPhysicalNameMapping);
                if (!areSame) {
                    throw new DuplicateMappingException(
                            String.format(
                                    Locale.ENGLISH,
                                    "Table [%s] contains logical column name [%s] referring to multiple physical " +
                                            "column names: [%s], [%s]",
                                    tableName,
                                    logicalName,
                                    existingPhysicalNameMapping,
                                    physicalName
                            ),
                            DuplicateMappingException.Type.COLUMN_BINDING,
                            tableName + "." + logicalName
                    );
                }
            }
        }

        private void bindPhysicalToLogical(Identifier logicalName, String physicalName) throws DuplicateMappingException {
            final Identifier existingLogicalName = physicalToLogical.put(physicalName, logicalName);
            if (existingLogicalName != null && !existingLogicalName.equals(logicalName)) {
                throw new DuplicateMappingException(
                        String.format(
                                Locale.ENGLISH,
                                "Table [%s] contains physical column name [%s] referred to by multiple physical " +
                                        "column names: [%s], [%s]",
                                tableName,
                                physicalName,
                                logicalName,
                                existingLogicalName
                        ),
                        DuplicateMappingException.Type.COLUMN_BINDING,
                        tableName + "." + physicalName
                );
            }
        }
    }

    private Map<cn.sexycode.mybatis.jpa.mapping.Table, TableColumnNameBinding> columnNameBindingByTableMap;

    @Override
    public void addColumnNameBinding(cn.sexycode.mybatis.jpa.mapping.Table table, String logicalName, cn.sexycode.mybatis.jpa.mapping.Column column) throws DuplicateMappingException {
        addColumnNameBinding(table, getDatabase().toIdentifier(logicalName), column);
    }

    @Override
    public void addColumnNameBinding(cn.sexycode.mybatis.jpa.mapping.Table table, Identifier logicalName, cn.sexycode.mybatis.jpa.mapping.Column column) throws DuplicateMappingException {
        TableColumnNameBinding binding = null;

        if (columnNameBindingByTableMap == null) {
            columnNameBindingByTableMap = new HashMap<>();
        } else {
            binding = columnNameBindingByTableMap.get(table);
        }

        if (binding == null) {
            binding = new TableColumnNameBinding(table.getName());
            columnNameBindingByTableMap.put(table, binding);
        }

        binding.addBinding(logicalName, column);
    }

    @Override
    public String getPhysicalColumnName(cn.sexycode.mybatis.jpa.mapping.Table table, String logicalName) throws MappingException {
        return getPhysicalColumnName(table, getDatabase().toIdentifier(logicalName));
    }

    @Override
    public String getPhysicalColumnName(cn.sexycode.mybatis.jpa.mapping.Table table, Identifier logicalName) throws MappingException {
        if (logicalName == null) {
            throw new MappingException("Logical column name cannot be null");
        }

        cn.sexycode.mybatis.jpa.mapping.Table currentTable = table;
        String physicalName = null;

        while (currentTable != null) {
            final TableColumnNameBinding binding = columnNameBindingByTableMap.get(currentTable);
            if (binding != null) {
                physicalName = binding.logicalToPhysical.get(logicalName);
                if (physicalName != null) {
                    break;
                }
            }

            if (DenormalizedTable.class.isInstance(currentTable)) {
                currentTable = ((DenormalizedTable) currentTable).getIncludedTable();
            } else {
                currentTable = null;
            }
        }

        if (physicalName == null) {
            throw new MappingException(
                    "Unable to find column with logical name " + logicalName.render() + " in table " + table.getName()
            );
        }
        return physicalName;
    }

    @Override
    public String getLogicalColumnName(cn.sexycode.mybatis.jpa.mapping.Table table, String physicalName) throws MappingException {
        return getLogicalColumnName(table, getDatabase().toIdentifier(physicalName));
    }


    @Override
    public String getLogicalColumnName(cn.sexycode.mybatis.jpa.mapping.Table table, Identifier physicalName) throws MappingException {
        final String physicalNameString = physicalName.render(getDatabase().getJdbcEnvironment().getDialect());
        Identifier logicalName = null;

        cn.sexycode.mybatis.jpa.mapping.Table currentTable = table;
        while (currentTable != null) {
            final TableColumnNameBinding binding = columnNameBindingByTableMap.get(currentTable);

            if (binding != null) {
                logicalName = binding.physicalToLogical.get(physicalNameString);
                if (logicalName != null) {
                    break;
                }
            }

            if (DenormalizedTable.class.isInstance(currentTable)) {
                currentTable = ((DenormalizedTable) currentTable).getIncludedTable();
            } else {
                currentTable = null;
            }
        }

        if (logicalName == null) {
            throw new MappingException(
                    "Unable to find column with physical name " + physicalNameString + " in table " + table.getName()
            );
        }
        return logicalName.render();
    }


    private final Map<String, AnnotatedClassType> annotatedClassTypeMap = new HashMap<String, AnnotatedClassType>();


    @Override
    public void addMappedSuperclass(Class type, MappedSuperclass mappedSuperclass) {
        if (mappedSuperClasses == null) {
            mappedSuperClasses = new HashMap<Class, MappedSuperclass>();
        }
        mappedSuperClasses.put(type, mappedSuperclass);
    }

    @Override
    public MappedSuperclass getMappedSuperclass(Class type) {
        if (mappedSuperClasses == null) {
            return null;
        }
        return mappedSuperClasses.get(type);
    }

    @Override
    public void addMappedBy(String entityName, String propertyName, String inversePropertyName) {
        if (mappedByResolver == null) {
            mappedByResolver = new HashMap<String, String>();
        }
        mappedByResolver.put(entityName + "." + propertyName, inversePropertyName);
    }

    @Override
    public String getFromMappedBy(String entityName, String propertyName) {
        if (mappedByResolver == null) {
            return null;
        }
        return mappedByResolver.get(entityName + "." + propertyName);
    }

    @Override
    public void addPropertyReferencedAssociation(String entityName, String propertyName, String propertyRef) {
        if (propertyRefResolver == null) {
            propertyRefResolver = new HashMap<String, String>();
        }
        propertyRefResolver.put(entityName + "." + propertyName, propertyRef);
    }

    @Override
    public String getPropertyReferencedAssociation(String entityName, String propertyName) {
        if (propertyRefResolver == null) {
            return null;
        }
        return propertyRefResolver.get(entityName + "." + propertyName);
    }

    private static class DelayedPropertyReferenceHandlerAnnotationImpl implements DelayedPropertyReferenceHandler {
        public final String referencedClass;
        public final String propertyName;
        public final boolean unique;

        public DelayedPropertyReferenceHandlerAnnotationImpl(String referencedClass, String propertyName, boolean unique) {
            this.referencedClass = referencedClass;
            this.propertyName = propertyName;
            this.unique = unique;
        }

        @Override
        public void process(InFlightMetadataCollector metadataCollector) {
            final PersistentClass clazz = metadataCollector.getEntityBinding(referencedClass);
            if (clazz == null) {
                throw new MappingException("property-ref to unmapped class: " + referencedClass);
            }

            final Property prop = clazz.getReferencedProperty(propertyName);
            if (unique) {
                ((SimpleValue) prop.getValue()).setAlternateUniqueKey(true);
            }
        }
    }

    @Override
    public void addPropertyReference(String referencedClass, String propertyName) {
        addDelayedPropertyReferenceHandler(
                new DelayedPropertyReferenceHandlerAnnotationImpl(referencedClass, propertyName, false)
        );
    }

    @Override
    public void addDelayedPropertyReferenceHandler(DelayedPropertyReferenceHandler handler) {
        if (delayedPropertyReferenceHandlers == null) {
            delayedPropertyReferenceHandlers = new HashSet<DelayedPropertyReferenceHandler>();
        }
        delayedPropertyReferenceHandlers.add(handler);
    }

    @Override
    public void addUniquePropertyReference(String referencedClass, String propertyName) {
        addDelayedPropertyReferenceHandler(
                new DelayedPropertyReferenceHandlerAnnotationImpl(referencedClass, propertyName, true)
        );
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void addUniqueConstraints(Table table, List uniqueConstraints) {
        List<UniqueConstraintHolder> constraintHolders = new ArrayList<UniqueConstraintHolder>(
                CollectionHelper.determineProperSizing(uniqueConstraints.size())
        );

        int keyNameBase = determineCurrentNumberOfUniqueConstraintHolders(table);
        for (String[] columns : (List<String[]>) uniqueConstraints) {
            final String keyName = "key" + keyNameBase++;
            constraintHolders.add(
                    new UniqueConstraintHolder().setName(keyName).setColumns(columns)
            );
        }
        addUniqueConstraintHolders(table, constraintHolders);
    }

    private int determineCurrentNumberOfUniqueConstraintHolders(cn.sexycode.mybatis.jpa.mapping.Table table) {
        List currentHolders = uniqueConstraintHoldersByTable == null ? null : uniqueConstraintHoldersByTable.get(table);
        return currentHolders == null
                ? 0
                : currentHolders.size();
    }


    private final Map<String, EntityTableXrefImpl> entityTableXrefMap = new HashMap<String, EntityTableXrefImpl>();


    @Override
    public Map<String, Join> getJoins(String entityName) {
        EntityTableXrefImpl xrefEntry = entityTableXrefMap.get(entityName);
        return xrefEntry == null ? null : xrefEntry.secondaryTableJoinMap;
    }


    private boolean inSecondPass = false;


    private List<Identifier> toIdentifiers(List<String> names) {
        if (names == null || names.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Identifier> columnNames = CollectionHelper.arrayList(names.size());
        for (String name : names) {
            columnNames.add(getDatabase().toIdentifier(name));
        }
        return columnNames;
    }

    private List<Identifier> toIdentifiers(String[] names) {
        if (names == null) {
            return Collections.emptyList();
        }

        final List<Identifier> columnNames = CollectionHelper.arrayList(names.length);
        for (String name : names) {
            columnNames.add(getDatabase().toIdentifier(name));
        }
        return columnNames;
    }

    @SuppressWarnings("unchecked")
    private List<Identifier> extractColumnNames(List columns) {
        if (columns == null || columns.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Identifier> columnNames = CollectionHelper.arrayList(columns.size());
        for (cn.sexycode.mybatis.jpa.mapping.Column column : (List<cn.sexycode.mybatis.jpa.mapping.Column>) columns) {
            columnNames.add(getDatabase().toIdentifier(column.getQuotedName()));
        }
        return columnNames;

    }

    private void processPropertyReferences() {
        if (delayedPropertyReferenceHandlers == null) {
            return;
        }
//        log.debug("Processing association property references");

        for (DelayedPropertyReferenceHandler delayedPropertyReferenceHandler : delayedPropertyReferenceHandlers) {
            delayedPropertyReferenceHandler.process(this);
        }

        delayedPropertyReferenceHandlers.clear();
    }


    private void buildUniqueKeyFromColumnNames(
            cn.sexycode.mybatis.jpa.mapping.Table table,
            String keyName,
            String[] columnNames,
            MetadataBuildingContext buildingContext) {
        buildUniqueKeyFromColumnNames(table, keyName, columnNames, null, true, buildingContext);
    }

    private void buildUniqueKeyFromColumnNames(
            final cn.sexycode.mybatis.jpa.mapping.Table table,
            String keyName,
            final String[] columnNames,
            String[] orderings,
            boolean unique,
            final MetadataBuildingContext buildingContext) {
        int size = columnNames.length;
        cn.sexycode.mybatis.jpa.mapping.Column[] columns = new cn.sexycode.mybatis.jpa.mapping.Column[size];
        Set<cn.sexycode.mybatis.jpa.mapping.Column> unbound = new HashSet<cn.sexycode.mybatis.jpa.mapping.Column>();
        Set<cn.sexycode.mybatis.jpa.mapping.Column> unboundNoLogical = new HashSet<cn.sexycode.mybatis.jpa.mapping.Column>();
        for (int index = 0; index < size; index++) {
            final String logicalColumnName = columnNames[index];
            try {
                final String physicalColumnName = getPhysicalColumnName(table, logicalColumnName);
                columns[index] = new cn.sexycode.mybatis.jpa.mapping.Column(physicalColumnName);
                unbound.add(columns[index]);
                //column equals and hashcode is based on column name
            } catch (MappingException e) {
                // If at least 1 columnName does exist, 'columns' will contain a mix of Columns and nulls.  In order
                // to exhaustively report all of the unbound columns at once, w/o an NPE in
                // Constraint#generateName's array sorting, simply create a fake Column.
                columns[index] = new cn.sexycode.mybatis.jpa.mapping.Column(logicalColumnName);
                unboundNoLogical.add(columns[index]);
            }
        }

        final String originalKeyName = keyName;

        if (unique) {
            final Identifier keyNameIdentifier = getMetadataBuildingOptions().getImplicitNamingStrategy().determineUniqueKeyName(
                    new ImplicitUniqueKeyNameSource() {
                        @Override
                        public MetadataBuildingContext getBuildingContext() {
                            return buildingContext;
                        }

                        @Override
                        public Identifier getTableName() {
                            return table.getNameIdentifier();
                        }

                        private List<Identifier> columnNameIdentifiers;

                        @Override
                        public List<Identifier> getColumnNames() {
                            // be lazy about building these
                            if (columnNameIdentifiers == null) {
                                columnNameIdentifiers = toIdentifiers(columnNames);
                            }
                            return columnNameIdentifiers;
                        }

                        @Override
                        public Identifier getUserProvidedIdentifier() {
                            return originalKeyName != null ? Identifier.toIdentifier(originalKeyName) : null;
                        }
                    }
            );
            keyName = keyNameIdentifier.render(getDatabase().getJdbcEnvironment().getDialect());

            UniqueKey uk = table.getOrCreateUniqueKey(keyName);
            for (int i = 0; i < columns.length; i++) {
                cn.sexycode.mybatis.jpa.mapping.Column column = columns[i];
                String order = orderings != null ? orderings[i] : null;
                if (table.containsColumn(column)) {
                    uk.addColumn(column, order);
                    unbound.remove(column);
                }
            }
        } else {
            final Identifier keyNameIdentifier = getMetadataBuildingOptions().getImplicitNamingStrategy().determineIndexName(
                    new ImplicitIndexNameSource() {
                        @Override
                        public MetadataBuildingContext getBuildingContext() {
                            return buildingContext;
                        }

                        @Override
                        public Identifier getTableName() {
                            return table.getNameIdentifier();
                        }

                        private List<Identifier> columnNameIdentifiers;

                        @Override
                        public List<Identifier> getColumnNames() {
                            // be lazy about building these
                            if (columnNameIdentifiers == null) {
                                columnNameIdentifiers = toIdentifiers(columnNames);
                            }
                            return columnNameIdentifiers;
                        }

                        @Override
                        public Identifier getUserProvidedIdentifier() {
                            return originalKeyName != null ? Identifier.toIdentifier(originalKeyName) : null;
                        }
                    }
            );
            keyName = keyNameIdentifier.render(getDatabase().getJdbcEnvironment().getDialect());

            Index index = table.getOrCreateIndex(keyName);
            for (int i = 0; i < columns.length; i++) {
                cn.sexycode.mybatis.jpa.mapping.Column column = columns[i];
                String order = orderings != null ? orderings[i] : null;
                if (table.containsColumn(column)) {
                    index.addColumn(column, order);
                    unbound.remove(column);
                }
            }
        }

        if (unbound.size() > 0 || unboundNoLogical.size() > 0) {
            StringBuilder sb = new StringBuilder("Unable to create ");
            if (unique) {
                sb.append("unique key constraint (");
            } else {
                sb.append("index (");
            }
            for (String columnName : columnNames) {
                sb.append(columnName).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(") on table ").append(table.getName()).append(": database column ");
            for (cn.sexycode.mybatis.jpa.mapping.Column column : unbound) {
                sb.append("'").append(column.getName()).append("', ");
            }
            for (Column column : unboundNoLogical) {
                sb.append("'").append(column.getName()).append("', ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(" not found. Make sure that you use the correct column name which depends on the naming strategy in use (it may not be the same as the property name in the entity, especially for relational types)");
            throw new AnnotationException(sb.toString());
        }
    }


    @Override
    public boolean isInSecondPass() {
        return inSecondPass;
    }

    /**
     * Builds the complete and immutable Metadata instance from the collected info.
     *
     * @return The complete and immutable Metadata instance
     */
    public MetadataImpl buildMetadataInstance(MetadataBuildingContext buildingContext) {
        processSecondPasses(buildingContext);
        processExportableProducers(buildingContext);

        try {
            return new MetadataImpl(
                    uuid,
                    options,
                    typeResolver,
                    identifierGeneratorFactory,
                    entityBindingMap,
                    mappedSuperClasses,
                    collectionBindingMap,
                    typeDefinitionMap,
                    filterDefinitionMap,
                    fetchProfileMap,
                    imports,
                    idGeneratorDefinitionMap,
                    namedQueryMap,
                    namedNativeQueryMap,
                    namedProcedureCallMap,
                    sqlResultSetMappingMap,
                    namedEntityGraphMap,
                    sqlFunctionMap,
                    getDatabase()
            );
        } finally {
            classmateContext.release();
        }
    }

    private void processExportableProducers(MetadataBuildingContext buildingContext) {
        // for now we only handle id generators as ExportableProducers

        final Dialect dialect = getDatabase().getJdbcEnvironment().getDialect();
        final String defaultCatalog = extractName(getDatabase().getDefaultNamespace().getName().getCatalog(), dialect);
        final String defaultSchema = extractName(getDatabase().getDefaultNamespace().getName().getSchema(), dialect);

        for (PersistentClass entityBinding : entityBindingMap.values()) {
            if (entityBinding.isInherited()) {
                continue;
            }

            handleIdentifierValueBinding(
                    entityBinding.getIdentifier(),
                    dialect,
                    defaultCatalog,
                    defaultSchema,
                    (RootClass) entityBinding
            );
        }

        for (Collection collection : collectionBindingMap.values()) {
            if (!IdentifierCollection.class.isInstance(collection)) {
                continue;
            }

            handleIdentifierValueBinding(
                    ((IdentifierCollection) collection).getIdentifier(),
                    dialect,
                    defaultCatalog,
                    defaultSchema,
                    null
            );
        }
    }

    private void handleIdentifierValueBinding(
            KeyValue identifierValueBinding,
            Dialect dialect,
            String defaultCatalog,
            String defaultSchema,
            RootClass entityBinding) {
        // todo : store this result (back into the entity or into the KeyValue, maybe?)
        // 		This process of instantiating the id-generator is called multiple times.
        //		It was done this way in the old code too, so no "regression" here; but
        //		it could be done better
        try {
            final IdentifierGenerator ig = identifierValueBinding.createIdentifierGenerator(
                    getIdentifierGeneratorFactory(),
                    dialect,
                    defaultCatalog,
                    defaultSchema,
                    entityBinding
            );

            if (ig instanceof ExportableProducer) {
                ((ExportableProducer) ig).registerExportables(getDatabase());
            }
        } catch (MappingException e) {
            // ignore this for now.  The reasoning being "non-reflective" binding as needed
            // by tools.  We want to hold off requiring classes being present until we
            // try to build a SF.  Here, just building the Metadata, it is "ok" for an
            // exception to occur, the same exception will happen later as we build the SF.
            log.debugf("Ignoring exception thrown when trying to build IdentifierGenerator as part of Metadata building", e);
        }
    }

    private String extractName(Identifier identifier, Dialect dialect) {
        if (identifier == null) {
            return null;
        }
        return identifier.render(dialect);
    }
}
