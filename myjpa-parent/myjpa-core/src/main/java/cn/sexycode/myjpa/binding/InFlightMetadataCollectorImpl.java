package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.mapping.PersistentClass;
import cn.sexycode.myjpa.session.SessionFactory;
import cn.sexycode.myjpa.session.SessionFactoryBuilder;
import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.sql.dialect.function.SQLFunction;
import cn.sexycode.sql.mapping.Column;
import cn.sexycode.sql.mapping.Index;
import cn.sexycode.sql.mapping.Table;
import cn.sexycode.sql.mapping.UniqueKey;
import cn.sexycode.sql.model.Database;
import cn.sexycode.sql.model.Identifier;
import cn.sexycode.sql.model.Namespace;
import cn.sexycode.sql.type.TypeResolver;
import cn.sexycode.util.core.cls.XClass;
import cn.sexycode.util.core.collection.CollectionUtils;
import cn.sexycode.util.core.exception.AnnotationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of the in-flight Metadata collector contract.
 * <p>
 * The usage expectation is that this class is used until all Metadata info is
 * collected and then {@link #buildMetadataInstance} is called to generate
 * the complete (and immutable) Metadata object.
 */
public class InFlightMetadataCollectorImpl implements InFlightMetadataCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(InFlightMetadataCollectorImpl.class);

    private final MetadataBuildingOptions options;

    private final TypeResolver typeResolver;

    private final UUID uuid;

    private final Map<String, PersistentClass> entityBindingMap = new HashMap<String, PersistentClass>();


    private final Map<String, String> imports = new HashMap<String, String>();

    private Database database;

    private final Map<String, IdentifierGeneratorDefinition> idGeneratorDefinitionMap = new HashMap<String, IdentifierGeneratorDefinition>();

    private Map<String, SQLFunction> sqlFunctionMap;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // All the annotation-processing-specific state :(
    private final Set<String> defaultIdentifierGeneratorNames = new HashSet<String>();

    private Map<Class, MappedSuperclass> mappedSuperClasses;

    public InFlightMetadataCollectorImpl(MetadataBuildingOptions options, TypeResolver typeResolver) {
        this.uuid = UUID.randomUUID();
        this.options = options;
        this.typeResolver = typeResolver;

        for (Map.Entry<String, SQLFunction> sqlFunctionEntry : options.getSqlFunctions().entrySet()) {
            if (sqlFunctionMap == null) {
                // we need this to be a ConcurrentHashMap for the one we ultimately pass along to the SF
                // but is this the reference that gets passed along?
                sqlFunctionMap = new ConcurrentHashMap<>(16, .75f, 1);
            }
            sqlFunctionMap.put(sqlFunctionEntry.getKey(), sqlFunctionEntry.getValue());
        }

    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public MetadataBuildingOptions getMetadataBuildingOptions() {
        return options;
    }

    @Override
    public TypeResolver getTypeResolver() {
        return typeResolver;
    }

    @Override
    public Database getDatabase() {
        // important to delay this instantiation until as late as possible.
        if (database == null) {
            this.database = new Database(options.getJdbcEnvironment());
        }
        return database;
    }

    @Override
    public Map<String, SQLFunction> getSqlFunctionMap() {
        return sqlFunctionMap;
    }

    @Override
    public void validate() throws MappingException {
        // nothing to do
    }

    @Override
    public Set<MappedSuperclass> getMappedSuperclassMappingsCopy() {
        return new HashSet<>(mappedSuperClasses.values());
    }

    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder() {
        throw new UnsupportedOperationException(
                "You should not be building a SessionFactory from an in-flight metadata collector; and of course "
                        + "we should better segment this in the API :)");
    }

    @Override
    public SessionFactory buildSessionFactory() {
        throw new UnsupportedOperationException(
                "You should not be building a SessionFactory from an in-flight metadata collector; and of course "
                        + "we should better segment this in the API :)");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Entity handling

    @Override
    public Collection<PersistentClass> getEntityBindings() {
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
    // identifier generators

    public IdentifierGeneratorDefinition getIdentifierGenerator(String name) {
        if (name == null) {
            throw new IllegalArgumentException("null is not a valid generator name");
        }
        return idGeneratorDefinitionMap.get(name);
    }

    public Collection<Table> collectTableMappings() {
        ArrayList<Table> tables = new ArrayList<Table>();
        for (Namespace namespace : getDatabase().getNamespaces()) {
            tables.addAll(namespace.getTables());
        }
        return tables;
    }

    @Override
    public void addIdentifierGenerator(IdentifierGeneratorDefinition generator) {
        if (generator == null || generator.getName() == null) {
            throw new IllegalArgumentException("ID generator object or name is null.");
        }

        if (defaultIdentifierGeneratorNames.contains(generator.getName())) {
            return;
        }

        final IdentifierGeneratorDefinition old = idGeneratorDefinitionMap.put(generator.getName(), generator);
		/*if ( old != null ) {
			LOGGER.duplicateGeneratorName( old.getName() );
		}*/
    }

    @Override
    public void addDefaultIdentifierGenerator(IdentifierGeneratorDefinition generator) {
        this.addIdentifierGenerator(generator);
        defaultIdentifierGeneratorNames.add(generator.getName());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // imports

    @Override
    public Map<String, String> getImports() {
        return imports;
    }

    @Override
    public void addImport(String importName, String entityName) {
        if (importName == null || entityName == null) {
            throw new IllegalArgumentException("Import name or entity name is null");
        }
        LOGGER.trace("Import: {0} -> {1}", importName, entityName);
        String old = imports.put(importName, entityName);
        if (old != null) {
            LOGGER.debug("import name [" + importName + "] overrode previous [{" + old + "}]");
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Table handling

    @Override
    public Table addTable(String schemaName, String catalogName, String name, String subselectFragment,
            boolean isAbstract) {
        final Namespace namespace = getDatabase()
                .locateNamespace(getDatabase().toIdentifier(catalogName), getDatabase().toIdentifier(schemaName));

        // annotation binding depends on the "table name" for @Subselect bindings
        // being set into the generated table (mainly to avoid later NPE), but for now we need to keep that :(
        final Identifier logicalName;
        if (name != null) {
            logicalName = getDatabase().toIdentifier(name);
        } else {
            logicalName = null;
        }

        if (subselectFragment != null) {
            return new Table(namespace, logicalName, isAbstract);
        } else {
            Table table = namespace.locateTable(logicalName);
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
    public void addTableNameBinding(Identifier logicalName, Table table) {
        logicalToPhysicalTableNameMap.put(logicalName, table.getNameIdentifier());
        physicalToLogicalTableNameMap.put(table.getNameIdentifier(), logicalName);
    }

    @Override
    public void addTableNameBinding(String schema, String catalog, String logicalName, String realTableName,
            Table denormalizedSuperTable) {
        final Identifier logicalNameIdentifier = getDatabase().toIdentifier(logicalName);
        final Identifier physicalNameIdentifier = getDatabase().toIdentifier(realTableName);

        logicalToPhysicalTableNameMap.put(logicalNameIdentifier, physicalNameIdentifier);
        physicalToLogicalTableNameMap.put(physicalNameIdentifier, logicalNameIdentifier);
    }

    @Override
    public String getLogicalTableName(Table ownerTable) {
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

        public void addBinding(Identifier logicalName, Column physicalColumn) {
            final String physicalNameString = physicalColumn.getQuotedName(getDatabase().getEnvironment().getDialect());

            bindLogicalToPhysical(logicalName, physicalNameString);
            bindPhysicalToLogical(logicalName, physicalNameString);
        }

        private void bindLogicalToPhysical(Identifier logicalName, String physicalName)
                throws DuplicateMappingException {
            final String existingPhysicalNameMapping = logicalToPhysical.put(logicalName, physicalName);
            if (existingPhysicalNameMapping != null) {
                final boolean areSame = logicalName.isQuoted()
                        ? physicalName.equals(existingPhysicalNameMapping)
                        : physicalName.equalsIgnoreCase(existingPhysicalNameMapping);
                if (!areSame) {
                    throw new DuplicateMappingException(String.format(Locale.ENGLISH,
                            "Table [%s] contains logical column name [%s] referring to multiple physical "
                                    + "column names: [%s], [%s]", tableName, logicalName, existingPhysicalNameMapping,
                            physicalName), DuplicateMappingException.Type.COLUMN_BINDING,
                            tableName + "." + logicalName);
                }
            }
        }

        private void bindPhysicalToLogical(Identifier logicalName, String physicalName)
                throws DuplicateMappingException {
            final Identifier existingLogicalName = physicalToLogical.put(physicalName, logicalName);
            if (existingLogicalName != null && !existingLogicalName.equals(logicalName)) {
                throw new DuplicateMappingException(String.format(Locale.ENGLISH,
                        "Table [%s] contains physical column name [%s] referred to by multiple physical "
                                + "column names: [%s], [%s]", tableName, physicalName, logicalName,
                        existingLogicalName), DuplicateMappingException.Type.COLUMN_BINDING,
                        tableName + "." + physicalName);
            }
        }
    }

    private Map<Table, TableColumnNameBinding> columnNameBindingByTableMap;

    @Override
    public void addColumnNameBinding(Table table, String logicalName, Column column) throws DuplicateMappingException {
        addColumnNameBinding(table, getDatabase().toIdentifier(logicalName), column);
    }

    @Override
    public void addColumnNameBinding(Table table, Identifier logicalName, Column column)
            throws DuplicateMappingException {
        TableColumnNameBinding binding = null;

        if (columnNameBindingByTableMap == null) {
            columnNameBindingByTableMap = new HashMap<Table, TableColumnNameBinding>();
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
    public String getPhysicalColumnName(Table table, String logicalName) throws MappingException {
        return getPhysicalColumnName(table, getDatabase().toIdentifier(logicalName));
    }

    @Override
    public String getPhysicalColumnName(Table table, Identifier logicalName) throws MappingException {
        if (logicalName == null) {
            throw new MappingException("Logical column name cannot be null");
        }

        Table currentTable = table;
        String physicalName = null;

        while (currentTable != null) {
            final TableColumnNameBinding binding = columnNameBindingByTableMap.get(currentTable);
            if (binding != null) {
                physicalName = binding.logicalToPhysical.get(logicalName);
                if (physicalName != null) {
                    break;
                }
            }

			/*if ( DenormalizedTable.class.isInstance( currentTable ) ) {
				currentTable = ( (DenormalizedTable) currentTable ).getIncludedTable();
			}
			else {
				currentTable = null;
			}*/
        }

        if (physicalName == null) {
            throw new MappingException(
                    "Unable to find column with logical name " + logicalName.render() + " in table " + table.getName());
        }
        return physicalName;
    }

    @Override
    public String getLogicalColumnName(Table table, String physicalName) throws MappingException {
        return getLogicalColumnName(table, getDatabase().toIdentifier(physicalName));
    }

    @Override
    public String getLogicalColumnName(Table table, Identifier physicalName) throws MappingException {
        final String physicalNameString = physicalName.render(getDatabase().getEnvironment().getDialect());
        Identifier logicalName = null;

        Table currentTable = table;
        while (currentTable != null) {
            final TableColumnNameBinding binding = columnNameBindingByTableMap.get(currentTable);

            if (binding != null) {
                logicalName = binding.physicalToLogical.get(physicalNameString);
                if (logicalName != null) {
                    break;
                }
            }

			/*if ( DenormalizedTable.class.isInstance( currentTable ) ) {
				currentTable = ( (DenormalizedTable) currentTable ).getIncludedTable();
			}
			else {
				currentTable = null;
			}*/
        }

        if (logicalName == null) {
            throw new MappingException(
                    "Unable to find column with physical name " + physicalNameString + " in table " + table.getName());
        }
        return logicalName.render();
    }

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

    private boolean inSecondPass = false;

    private List<Identifier> toIdentifiers(String[] names) {
        if (names == null) {
            return Collections.emptyList();
        }

        final List<Identifier> columnNames = CollectionUtils.arrayList(names.length);
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

        final List<Identifier> columnNames = CollectionUtils.arrayList(columns.size());
        for (Column column : (List<Column>) columns) {
            columnNames.add(getDatabase().toIdentifier(column.getQuotedName()));
        }
        return columnNames;

    }

    private void buildUniqueKeyFromColumnNames(Table table, String keyName, String[] columnNames,
            MetadataBuildingContext buildingContext) {
        buildUniqueKeyFromColumnNames(table, keyName, columnNames, null, true, buildingContext);
    }

    private void buildUniqueKeyFromColumnNames(final Table table, String keyName, final String[] columnNames,
            String[] orderings, boolean unique, final MetadataBuildingContext buildingContext) {
        int size = columnNames.length;
        Column[] columns = new Column[size];
        Set<Column> unbound = new HashSet<Column>();
        Set<Column> unboundNoLogical = new HashSet<Column>();
        for (int index = 0; index < size; index++) {
            final String logicalColumnName = columnNames[index];
            try {
                final String physicalColumnName = getPhysicalColumnName(table, logicalColumnName);
                columns[index] = new Column(physicalColumnName);
                unbound.add(columns[index]);
                //column equals and hashcode is based on column name
            } catch (MappingException e) {
                // If at least 1 columnName does exist, 'columns' will contain a mix of Columns and nulls.  In order
                // to exhaustively report all of the unbound columns at once, w/o an NPE in
                // Constraint#generateName's array sorting, simply create a fake Column.
                columns[index] = new Column(logicalColumnName);
                unboundNoLogical.add(columns[index]);
            }
        }

        final String originalKeyName = keyName;

        if (unique) {
			/*final Identifier keyNameIdentifier = getMetadataBuildingOptions().getImplicitNamingStrategy().determineUniqueKeyName(
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
						if ( columnNameIdentifiers == null ) {
							columnNameIdentifiers = toIdentifiers( columnNames );
						}
						return columnNameIdentifiers;
					}

					@Override
					public Identifier getUserProvidedIdentifier() {
						return originalKeyName != null ? Identifier.toIdentifier( originalKeyName ) : null;
					}
				}
			);
			keyName = keyNameIdentifier.render( getDatabase().getEnvironment().getDialect() );
*/
            UniqueKey uk = table.getOrCreateUniqueKey(keyName);
            for (int i = 0; i < columns.length; i++) {
                Column column = columns[i];
                String order = orderings != null ? orderings[i] : null;
                if (table.containsColumn(column)) {
                    uk.addColumn(column, order);
                    unbound.remove(column);
                }
            }
        } else {
			/*final Identifier keyNameIdentifier = getMetadataBuildingOptions().getImplicitNamingStrategy().determineIndexName(
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
						if ( columnNameIdentifiers == null ) {
							columnNameIdentifiers = toIdentifiers( columnNames );
						}
						return columnNameIdentifiers;
					}

					@Override
					public Identifier getUserProvidedIdentifier() {
						return originalKeyName != null ? Identifier.toIdentifier( originalKeyName ) : null;
					}
				}
			);
			keyName = keyNameIdentifier.render( getDatabase().getEnvironment().getDialect() );
*/
            Index index = table.getOrCreateIndex(keyName);
            for (int i = 0; i < columns.length; i++) {
                Column column = columns[i];
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
            for (Column column : unbound) {
                sb.append("'").append(column.getName()).append("', ");
            }
            for (Column column : unboundNoLogical) {
                sb.append("'").append(column.getName()).append("', ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(
                    " not found. Make sure that you use the correct column name which depends on the naming strategy in use (it may not be the same as the property name in the entity, especially for relational types)");
            throw new AnnotationException(sb.toString());
        }
    }

    @Override
    public boolean isInSecondPass() {
        return inSecondPass;
    }

    @Override
    public void addUniqueConstraints(Table table, List uniqueConstraints) {

    }

    /**
     * Builds the complete and immutable Metadata instance from the collected info.
     *
     * @return The complete and immutable Metadata instance
     */
    public MetadataImpl buildMetadataInstance(MetadataBuildingContext buildingContext) {

        processExportableProducers(buildingContext);

        try {
            return new MetadataImpl(uuid, options, typeResolver, entityBindingMap, mappedSuperClasses, sqlFunctionMap,
                    getDatabase());
        } finally {
        }
    }

    private void processExportableProducers(MetadataBuildingContext buildingContext) {
        // for now we only handle id generators as ExportableProducers

        //        final Dialect dialect = getDatabase().getEnvironment().getDialect();
        //        final String defaultCatalog = extractName(getDatabase().getDefaultNamespace().getName().getCatalog(), dialect);
        //        final String defaultSchema = extractName(getDatabase().getDefaultNamespace().getName().getSchema(), dialect);

        /*for (PersistentClass entityBinding : entityBindingMap.values()) {
            if (entityBinding.isInherited()) {
                continue;
            }
        }*/

    }

    private String extractName(Identifier identifier, Dialect dialect) {
        if (identifier == null) {
            return null;
        }
        return identifier.render(dialect);
    }

    private final Map<String, AnnotatedClassType> annotatedClassTypeMap = new HashMap<>();

    @Override
    public AnnotatedClassType getClassType(XClass clazz) {
        AnnotatedClassType type = annotatedClassTypeMap.get(clazz.getName());
        if (type == null) {
            return addClassType(clazz);
        } else {
            return type;
        }
    }

    @Override
    public AnnotatedClassType addClassType(XClass clazz) {
        AnnotatedClassType type;
        if (clazz.isAnnotationPresent(Entity.class)) {
            type = AnnotatedClassType.ENTITY;
        } else if (clazz.isAnnotationPresent(Embeddable.class)) {
            type = AnnotatedClassType.EMBEDDABLE;
        } else if (clazz.isAnnotationPresent(javax.persistence.MappedSuperclass.class)) {
            type = AnnotatedClassType.EMBEDDABLE_SUPERCLASS;
        } else {
            type = AnnotatedClassType.NONE;
        }
        annotatedClassTypeMap.put(clazz.getName(), type);
        return type;
    }
}
