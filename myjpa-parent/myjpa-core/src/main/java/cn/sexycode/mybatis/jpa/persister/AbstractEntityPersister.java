/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.persister;

import cn.sexycode.mybatis.jpa.mapping.PersistentClass;
import cn.sexycode.mybatis.jpa.session.Session;
import com.sun.deploy.cache.CacheEntry;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.Alias;
import org.hibernate.*;
import org.hibernate.bytecode.enhance.spi.LazyPropertyInitializer;
import org.hibernate.bytecode.enhance.spi.interceptor.LazyAttributeDescriptor;
import org.hibernate.bytecode.enhance.spi.interceptor.LazyAttributeLoadingInterceptor;
import org.hibernate.bytecode.enhance.spi.interceptor.LazyAttributesMetadata;
import org.hibernate.bytecode.spi.BytecodeEnhancementMetadata;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.entry.*;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.lock.LockingStrategy;
import org.hibernate.engine.OptimisticLockStyle;
import org.hibernate.engine.internal.*;
import org.hibernate.engine.jdbc.batch.internal.BasicBatchKey;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.*;
import org.hibernate.engine.spi.PersistenceContext.NaturalIdHelper;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PostInsertIdentifierGenerator;
import org.hibernate.id.PostInsertIdentityPersister;
import org.hibernate.id.insert.Binder;
import org.hibernate.id.insert.InsertGeneratedIdentifierDelegate;
import org.hibernate.internal.CoreLogging;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.FilterHelper;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.jdbc.Expectation;
import org.hibernate.jdbc.Expectations;
import org.hibernate.jdbc.TooManyRowsAffectedException;
import org.hibernate.loader.entity.*;
import org.hibernate.mapping.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.spi.PersisterCreationContext;
import org.hibernate.persister.walking.internal.EntityIdentifierDefinitionHelper;
import org.hibernate.persister.walking.spi.AttributeDefinition;
import org.hibernate.persister.walking.spi.EntityIdentifierDefinition;
import org.hibernate.pretty.MessageHelper;
import org.hibernate.property.access.internal.PropertyAccessStrategyBackRefImpl;
import org.hibernate.sql.*;
import org.hibernate.tuple.*;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.tuple.entity.EntityTuplizer;
import org.hibernate.type.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorType;
import javax.persistence.Table;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.EntityType;
import javax.sql.rowset.Joinable;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Basic functionality for persisting an entity via JDBC
 * through either generated or custom SQL
 *
 * @author Gavin King
 */
public abstract class AbstractEntityPersister implements EntityPersister{


	public static final String ENTITY_CLASS = "class";

	// moved up from AbstractEntityPersister ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private final boolean isLazyPropertiesCacheable;
	private final CacheEntryHelper cacheEntryHelper;
	private final EntityMetamodel entityMetamodel;
	private final EntityTuplizer entityTuplizer;
	private final EntityEntryFactory entityEntryFactory;
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private final String[] rootTableKeyColumnNames;
	private final String[] rootTableKeyColumnReaders;
	private final String[] rootTableKeyColumnReaderTemplates;
	private final String[] identifierAliases;
	private final int identifierColumnSpan;
	private final String versionColumnName;
	private final boolean hasFormulaProperties;
	protected final int batchSize;
	private final boolean hasSubselectLoadableCollections;
	protected final String rowIdName;

	// The optional SQL string defined in the where attribute
	private final String sqlWhereString;
	private final String sqlWhereStringTemplate;

	//information about properties of this class,
	//including inherited properties
	//(only really needed for updatable/insertable properties)
	private final int[] propertyColumnSpans;
	private final String[] propertySubclassNames;
	private final String[][] propertyColumnAliases;
	private final String[][] propertyColumnNames;
	private final String[][] propertyColumnFormulaTemplates;
	private final String[][] propertyColumnReaderTemplates;
	private final String[][] propertyColumnWriters;
	private final boolean[][] propertyColumnUpdateable;
	private final boolean[][] propertyColumnInsertable;
	private final boolean[] propertyUniqueness;
	private final boolean[] propertySelectable;

	private final List<Integer> lobProperties = new ArrayList<Integer>();

	//information about lazy properties of this class
	private final String[] lazyPropertyNames;
	private final int[] lazyPropertyNumbers;
	private final String[][] lazyPropertyColumnAliases;

	//information about all properties in class hierarchy
	private final String[] subclassPropertyNameClosure;
	private final String[] subclassPropertySubclassNameClosure;
	private final String[][] subclassPropertyFormulaTemplateClosure;
	private final String[][] subclassPropertyColumnNameClosure;
	private final String[][] subclassPropertyColumnReaderClosure;
	private final String[][] subclassPropertyColumnReaderTemplateClosure;
	private final boolean[] subclassPropertyNullabilityClosure;
	private final boolean[] propertyDefinedOnSubclass;
	private final int[][] subclassPropertyColumnNumberClosure;
	private final int[][] subclassPropertyFormulaNumberClosure;

	//information about all columns/formulas in class hierarchy
	private final String[] subclassColumnClosure;
	private final boolean[] subclassColumnLazyClosure;
	private final String[] subclassColumnAliasClosure;
	private final boolean[] subclassColumnSelectableClosure;
	private final String[] subclassColumnReaderTemplateClosure;
	private final String[] subclassFormulaClosure;
	private final String[] subclassFormulaTemplateClosure;
	private final String[] subclassFormulaAliasClosure;
	private final boolean[] subclassFormulaLazyClosure;

	// dynamic filters attached to the class-level

	private final Set<String> affectingFetchProfileNames = new HashSet<String>();

	private final Map uniqueKeyLoaders = new HashMap();
	private final Map lockers = new HashMap();
	private final Map loaders = new HashMap();

	// SQL strings
	private String sqlVersionSelectString;
	private String sqlSnapshotSelectString;
	private Map<String,String> sqlLazySelectStringsByFetchGroup;

	private String sqlIdentityInsertString;
	private String sqlUpdateByRowIdString;
	private String sqlLazyUpdateByRowIdString;

	private String[] sqlDeleteStrings;
	private String[] sqlInsertStrings;
	private String[] sqlUpdateStrings;
	private String[] sqlLazyUpdateStrings;

	private String sqlInsertGeneratedValuesSelectString;
	private String sqlUpdateGeneratedValuesSelectString;

	//Custom SQL (would be better if these were private)
	protected boolean[] insertCallable;
	protected boolean[] updateCallable;
	protected boolean[] deleteCallable;
	protected String[] customSQLInsert;
	protected String[] customSQLUpdate;
	protected String[] customSQLDelete;


	private boolean[] tableHasColumns;

	private final String loaderName;


	private final Map subclassPropertyAliases = new HashMap();
	private final Map subclassPropertyColumnNames = new HashMap();

	/**
	 * Warning:
	 * When there are duplicated property names in the subclasses
	 * then propertyMapping will only contain one of those properties.
	 * To ensure correct results, propertyMapping should only be used
	 * for the concrete EntityPersister (since the concrete EntityPersister
	 * cannot have duplicated property names).
	 */

	private final boolean useReferenceCacheEntries;

	private boolean[] getTableHasColumns() {
		return tableHasColumns;
	}

	public String[] getRootTableKeyColumnNames() {
		return rootTableKeyColumnNames;
	}

	protected String[] getSQLUpdateByRowIdStrings() {
		if ( sqlUpdateByRowIdString == null ) {
//			throw new AssertionFailure( "no update by row id" );
		}
		String[] result = new String[getTableSpan() + 1];
		result[0] = sqlUpdateByRowIdString;
		System.arraycopy( sqlUpdateStrings, 0, result, 1, getTableSpan() );
		return result;
	}

	protected String[] getSQLLazyUpdateByRowIdStrings() {
		if ( sqlLazyUpdateByRowIdString == null ) {
			throw new AssertionFailure( "no update by row id" );
		}
		String[] result = new String[getTableSpan()];
		result[0] = sqlLazyUpdateByRowIdString;
		System.arraycopy( sqlLazyUpdateStrings, 1, result, 1, getTableSpan() - 1 );
		return result;
	}

	protected String getSQLSnapshotSelectString() {
		return sqlSnapshotSelectString;
	}

	protected String getSQLLazySelectString(String fetchGroup) {
		return sqlLazySelectStringsByFetchGroup.get( fetchGroup );
	}

	protected String[] getSQLDeleteStrings() {
		return sqlDeleteStrings;
	}

	protected String[] getSQLInsertStrings() {
		return sqlInsertStrings;
	}

	protected String[] getSQLUpdateStrings() {
		return sqlUpdateStrings;
	}

	protected String[] getSQLLazyUpdateStrings() {
		return sqlLazyUpdateStrings;
	}

	/**
	 * The query that inserts a row, letting the database generate an id
	 *
	 * @return The IDENTITY-based insertion query.
	 */
	protected String getSQLIdentityInsertString() {
		return sqlIdentityInsertString;
	}

	protected String getVersionSelectString() {
		return sqlVersionSelectString;
	}

	protected boolean isInsertCallable(int j) {
		return insertCallable[j];
	}

	protected boolean isUpdateCallable(int j) {
		return updateCallable[j];
	}

	protected boolean isDeleteCallable(int j) {
		return deleteCallable[j];
	}

	protected boolean isSubclassPropertyDeferred(String propertyName, String entityName) {
		return false;
	}

	protected boolean isSubclassTableSequentialSelect(int j) {
		return false;
	}

	public boolean hasSequentialSelect() {
		return false;
	}

	/**
	 * Decide which tables need to be updated.
	 * <p/>
	 * The return here is an array of boolean values with each index corresponding
	 * to a given table in the scope of this persister.
	 *
	 * @param dirtyProperties The indices of all the entity properties considered dirty.
	 * @param hasDirtyCollection Whether any collections owned by the entity which were considered dirty.
	 *
	 * @return Array of booleans indicating which table require updating.
	 */
	protected boolean[] getTableUpdateNeeded(final int[] dirtyProperties, boolean hasDirtyCollection) {

		if ( dirtyProperties == null ) {
			return getTableHasColumns(); // for objects that came in via update()
		}
		else {
			boolean[] updateability = getPropertyUpdateability();
			int[] propertyTableNumbers = getPropertyTableNumbers();
			boolean[] tableUpdateNeeded = new boolean[getTableSpan()];
			for ( int property : dirtyProperties ) {
				int table = propertyTableNumbers[property];
				tableUpdateNeeded[table] = tableUpdateNeeded[table] ||
						( getPropertyColumnSpan( property ) > 0 && updateability[property] );
			}
			if ( isVersioned() ) {
				tableUpdateNeeded[0] = tableUpdateNeeded[0] ||
						Versioning.isVersionIncrementRequired(
								dirtyProperties,
								hasDirtyCollection,
								getPropertyVersionability()
						);
			}
			return tableUpdateNeeded;
		}
	}

	public boolean hasRowId() {
		return rowIdName != null;
	}

	protected boolean[][] getPropertyColumnUpdateable() {
		return propertyColumnUpdateable;
	}

	protected boolean[][] getPropertyColumnInsertable() {
		return propertyColumnInsertable;
	}

	protected boolean[] getPropertySelectable() {
		return propertySelectable;
	}

	@SuppressWarnings("UnnecessaryBoxing")
	public AbstractEntityPersister(
			final PersistentClass persistentClass,
			final EntityRegionAccessStrategy cacheAccessStrategy,
			final NaturalIdRegionAccessStrategy naturalIdRegionAccessStrategy,
			final PersisterCreationContext creationContext) throws HibernateException {

		// moved up from AbstractEntityPersister ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		this.factory = creationContext.getSessionFactory();
		this.cacheAccessStrategy = cacheAccessStrategy;
		this.naturalIdRegionAccessStrategy = naturalIdRegionAccessStrategy;
		isLazyPropertiesCacheable = persistentClass.isLazyPropertiesCacheable();

		this.entityMetamodel = new EntityMetamodel( persistentClass, this, factory );
		this.entityTuplizer = this.entityMetamodel.getTuplizer();

		if ( entityMetamodel.isMutable() ) {
			this.entityEntryFactory = MutableEntityEntryFactory.INSTANCE;
		}
		else {
			this.entityEntryFactory = ImmutableEntityEntryFactory.INSTANCE;
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		final JdbcServices jdbcServices = factory.getServiceRegistry().getService( JdbcServices.class );
		final Dialect dialect = jdbcServices.getJdbcEnvironment().getDialect();

		int batch = persistentClass.getBatchSize();
		if ( batch == -1 ) {
			batch = factory.getSessionFactoryOptions().getDefaultBatchFetchSize();
		}
		batchSize = batch;
		hasSubselectLoadableCollections = persistentClass.hasSubselectLoadableCollections();

		propertyMapping = new BasicEntityPropertyMapping( this );

		// IDENTIFIER

		identifierColumnSpan = persistentClass.getIdentifier().getColumnSpan();
		rootTableKeyColumnNames = new String[identifierColumnSpan];
		rootTableKeyColumnReaders = new String[identifierColumnSpan];
		rootTableKeyColumnReaderTemplates = new String[identifierColumnSpan];
		identifierAliases = new String[identifierColumnSpan];

		rowIdName = persistentClass.getRootTable().getRowId();

		loaderName = persistentClass.getLoaderName();

		Iterator iter = persistentClass.getIdentifier().getColumnIterator();
		int i = 0;
		while ( iter.hasNext() ) {
			Column col = (Column) iter.next();
			rootTableKeyColumnNames[i] = col.getQuotedName( dialect );
			rootTableKeyColumnReaders[i] = col.getReadExpr( dialect );
			rootTableKeyColumnReaderTemplates[i] = col.getTemplate(
					dialect,
					factory.getSqlFunctionRegistry()
			);
			identifierAliases[i] = col.getAlias( dialect, persistentClass.getRootTable() );
			i++;
		}

		// VERSION

		if ( persistentClass.isVersioned() ) {
			versionColumnName = ( (Column) persistentClass.getVersion().getColumnIterator().next() ).getQuotedName( dialect );
		}
		else {
			versionColumnName = null;
		}

		//WHERE STRING

		sqlWhereString = StringHelper.isNotEmpty( persistentClass.getWhere() ) ?
				"( " + persistentClass.getWhere() + ") " :
				null;
		sqlWhereStringTemplate = sqlWhereString == null ?
				null :
				Template.renderWhereStringTemplate(
						sqlWhereString,
						dialect,
						factory.getSqlFunctionRegistry()
				);

		// PROPERTIES

		final boolean lazyAvailable = isInstrumented();

		int hydrateSpan = entityMetamodel.getPropertySpan();
		propertyColumnSpans = new int[hydrateSpan];
		propertySubclassNames = new String[hydrateSpan];
		propertyColumnAliases = new String[hydrateSpan][];
		propertyColumnNames = new String[hydrateSpan][];
		propertyColumnFormulaTemplates = new String[hydrateSpan][];
		propertyColumnReaderTemplates = new String[hydrateSpan][];
		propertyColumnWriters = new String[hydrateSpan][];
		propertyUniqueness = new boolean[hydrateSpan];
		propertySelectable = new boolean[hydrateSpan];
		propertyColumnUpdateable = new boolean[hydrateSpan][];
		propertyColumnInsertable = new boolean[hydrateSpan][];
		HashSet thisClassProperties = new HashSet();

		ArrayList lazyNames = new ArrayList();
		ArrayList lazyNumbers = new ArrayList();
		ArrayList lazyTypes = new ArrayList();
		ArrayList lazyColAliases = new ArrayList();

		iter = persistentClass.getPropertyClosureIterator();
		i = 0;
		boolean foundFormula = false;
		while ( iter.hasNext() ) {
			Property prop = (Property) iter.next();
			thisClassProperties.add( prop );

			int span = prop.getColumnSpan();
			propertyColumnSpans[i] = span;
			propertySubclassNames[i] = prop.getPersistentClass().getEntityName();
			String[] colNames = new String[span];
			String[] colAliases = new String[span];
			String[] colReaderTemplates = new String[span];
			String[] colWriters = new String[span];
			String[] formulaTemplates = new String[span];
			Iterator colIter = prop.getColumnIterator();
			int k = 0;
			while ( colIter.hasNext() ) {
				Selectable thing = (Selectable) colIter.next();
				colAliases[k] = thing.getAlias( dialect, prop.getValue().getTable() );
				if ( thing.isFormula() ) {
					foundFormula = true;
					formulaTemplates[k] = thing.getTemplate( dialect, factory.getSqlFunctionRegistry() );
				}
				else {
					Column col = (Column) thing;
					colNames[k] = col.getQuotedName( dialect );
					colReaderTemplates[k] = col.getTemplate( dialect, factory.getSqlFunctionRegistry() );
					colWriters[k] = col.getWriteExpr();
				}
				k++;
			}
			propertyColumnNames[i] = colNames;
			propertyColumnFormulaTemplates[i] = formulaTemplates;
			propertyColumnReaderTemplates[i] = colReaderTemplates;
			propertyColumnWriters[i] = colWriters;
			propertyColumnAliases[i] = colAliases;

			if ( lazyAvailable && prop.isLazy() ) {
				lazyNames.add( prop.getName() );
				lazyNumbers.add( i );
				lazyTypes.add( prop.getValue().getType() );
				lazyColAliases.add( colAliases );
			}

			propertyColumnUpdateable[i] = prop.getValue().getColumnUpdateability();
			propertyColumnInsertable[i] = prop.getValue().getColumnInsertability();

			propertySelectable[i] = prop.isSelectable();

			propertyUniqueness[i] = prop.getValue().isAlternateUniqueKey();

			if ( prop.isLob() && dialect.forceLobAsLastValue() ) {
				lobProperties.add( i );
			}

			i++;

		}
		hasFormulaProperties = foundFormula;
		lazyPropertyColumnAliases = ArrayHelper.to2DStringArray( lazyColAliases );
		lazyPropertyNames = ArrayHelper.toStringArray( lazyNames );
		lazyPropertyNumbers = ArrayHelper.toIntArray( lazyNumbers );
		lazyPropertyTypes = ArrayHelper.toTypeArray( lazyTypes );

		// SUBCLASS PROPERTY CLOSURE

		ArrayList columns = new ArrayList();
		ArrayList columnsLazy = new ArrayList();
		ArrayList columnReaderTemplates = new ArrayList();
		ArrayList aliases = new ArrayList();
		ArrayList formulas = new ArrayList();
		ArrayList formulaAliases = new ArrayList();
		ArrayList formulaTemplates = new ArrayList();
		ArrayList formulasLazy = new ArrayList();
		ArrayList types = new ArrayList();
		ArrayList names = new ArrayList();
		ArrayList classes = new ArrayList();
		ArrayList templates = new ArrayList();
		ArrayList propColumns = new ArrayList();
		ArrayList propColumnReaders = new ArrayList();
		ArrayList propColumnReaderTemplates = new ArrayList();
		ArrayList joinedFetchesList = new ArrayList();
		ArrayList cascades = new ArrayList();
		ArrayList definedBySubclass = new ArrayList();
		ArrayList propColumnNumbers = new ArrayList();
		ArrayList propFormulaNumbers = new ArrayList();
		ArrayList columnSelectables = new ArrayList();
		ArrayList propNullables = new ArrayList();

		iter = persistentClass.getSubclassPropertyClosureIterator();
		while ( iter.hasNext() ) {
			Property prop = (Property) iter.next();
			names.add( prop.getName() );
			classes.add( prop.getPersistentClass().getEntityName() );
			boolean isDefinedBySubclass = !thisClassProperties.contains( prop );
			definedBySubclass.add( Boolean.valueOf( isDefinedBySubclass ) );
			propNullables.add( Boolean.valueOf( prop.isOptional() || isDefinedBySubclass ) ); //TODO: is this completely correct?
			types.add( prop.getType() );

			Iterator colIter = prop.getColumnIterator();
			String[] cols = new String[prop.getColumnSpan()];
			String[] readers = new String[prop.getColumnSpan()];
			String[] readerTemplates = new String[prop.getColumnSpan()];
			String[] forms = new String[prop.getColumnSpan()];
			int[] colnos = new int[prop.getColumnSpan()];
			int[] formnos = new int[prop.getColumnSpan()];
			int l = 0;
			Boolean lazy = Boolean.valueOf( prop.isLazy() && lazyAvailable );
			while ( colIter.hasNext() ) {
				Selectable thing = (Selectable) colIter.next();
				if ( thing.isFormula() ) {
					String template = thing.getTemplate( dialect, factory.getSqlFunctionRegistry() );
					formnos[l] = formulaTemplates.size();
					colnos[l] = -1;
					formulaTemplates.add( template );
					forms[l] = template;
					formulas.add( thing.getText( dialect ) );
					formulaAliases.add( thing.getAlias( dialect ) );
					formulasLazy.add( lazy );
				}
				else {
					Column col = (Column) thing;
					String colName = col.getQuotedName( dialect );
					colnos[l] = columns.size(); //beforeQuery add :-)
					formnos[l] = -1;
					columns.add( colName );
					cols[l] = colName;
					aliases.add( thing.getAlias( dialect, prop.getValue().getTable() ) );
					columnsLazy.add( lazy );
					columnSelectables.add( Boolean.valueOf( prop.isSelectable() ) );

					readers[l] = col.getReadExpr( dialect );
					String readerTemplate = col.getTemplate( dialect, factory.getSqlFunctionRegistry() );
					readerTemplates[l] = readerTemplate;
					columnReaderTemplates.add( readerTemplate );
				}
				l++;
			}
			propColumns.add( cols );
			propColumnReaders.add( readers );
			propColumnReaderTemplates.add( readerTemplates );
			templates.add( forms );
			propColumnNumbers.add( colnos );
			propFormulaNumbers.add( formnos );

			joinedFetchesList.add( prop.getValue().getFetchMode() );
			cascades.add( prop.getCascadeStyle() );
		}
		subclassColumnClosure = ArrayHelper.toStringArray( columns );
		subclassColumnAliasClosure = ArrayHelper.toStringArray( aliases );
		subclassColumnLazyClosure = ArrayHelper.toBooleanArray( columnsLazy );
		subclassColumnSelectableClosure = ArrayHelper.toBooleanArray( columnSelectables );
		subclassColumnReaderTemplateClosure = ArrayHelper.toStringArray( columnReaderTemplates );

		subclassFormulaClosure = ArrayHelper.toStringArray( formulas );
		subclassFormulaTemplateClosure = ArrayHelper.toStringArray( formulaTemplates );
		subclassFormulaAliasClosure = ArrayHelper.toStringArray( formulaAliases );
		subclassFormulaLazyClosure = ArrayHelper.toBooleanArray( formulasLazy );

		subclassPropertyNameClosure = ArrayHelper.toStringArray( names );
		subclassPropertySubclassNameClosure = ArrayHelper.toStringArray( classes );
		subclassPropertyTypeClosure = ArrayHelper.toTypeArray( types );
		subclassPropertyNullabilityClosure = ArrayHelper.toBooleanArray( propNullables );
		subclassPropertyFormulaTemplateClosure = ArrayHelper.to2DStringArray( templates );
		subclassPropertyColumnNameClosure = ArrayHelper.to2DStringArray( propColumns );
		subclassPropertyColumnReaderClosure = ArrayHelper.to2DStringArray( propColumnReaders );
		subclassPropertyColumnReaderTemplateClosure = ArrayHelper.to2DStringArray( propColumnReaderTemplates );
		subclassPropertyColumnNumberClosure = ArrayHelper.to2DIntArray( propColumnNumbers );
		subclassPropertyFormulaNumberClosure = ArrayHelper.to2DIntArray( propFormulaNumbers );

		subclassPropertyCascadeStyleClosure = new CascadeStyle[cascades.size()];
		iter = cascades.iterator();
		int j = 0;
		while ( iter.hasNext() ) {
			subclassPropertyCascadeStyleClosure[j++] = (CascadeStyle) iter.next();
		}
		subclassPropertyFetchModeClosure = new FetchMode[joinedFetchesList.size()];
		iter = joinedFetchesList.iterator();
		j = 0;
		while ( iter.hasNext() ) {
			subclassPropertyFetchModeClosure[j++] = (FetchMode) iter.next();
		}

		propertyDefinedOnSubclass = new boolean[definedBySubclass.size()];
		iter = definedBySubclass.iterator();
		j = 0;
		while ( iter.hasNext() ) {
			propertyDefinedOnSubclass[j++] = (Boolean) iter.next();
		}

		// Handle any filters applied to the class level
		filterHelper = new FilterHelper( persistentClass.getFilters(), factory );

		// Check if we can use Reference Cached entities in 2lc
		// todo : should really validate that the cache access type is read-only
		boolean refCacheEntries = true;
		if ( !factory.getSessionFactoryOptions().isDirectReferenceCacheEntriesEnabled() ) {
			refCacheEntries = false;
		}

		// for now, limit this to just entities that:
		// 		1) are immutable
		if ( entityMetamodel.isMutable() ) {
			refCacheEntries = false;
		}

		//		2)  have no associations.  Eventually we want to be a little more lenient with associations.
		for ( Type type : getSubclassPropertyTypeClosure() ) {
			if ( type.isAssociationType() ) {
				refCacheEntries = false;
			}
		}

		useReferenceCacheEntries = refCacheEntries;

		this.cacheEntryHelper = buildCacheEntryHelper();

	}

	protected CacheEntryHelper buildCacheEntryHelper() {
		if ( cacheAccessStrategy == null ) {
			// the entity defined no caching...
			return NoopCacheEntryHelper.INSTANCE;
		}

		if ( canUseReferenceCacheEntries() ) {
			entityMetamodel.setLazy( false );
			// todo : do we also need to unset proxy factory?
			return new ReferenceCacheEntryHelper( this );
		}

		return factory.getSessionFactoryOptions().isStructuredCacheEntriesEnabled()
				? new StructuredCacheEntryHelper( this )
				: new StandardCacheEntryHelper( this );
	}

	public boolean canUseReferenceCacheEntries() {
		return useReferenceCacheEntries;
	}

	protected static String getTemplateFromString(String string, SessionFactoryImplementor factory) {
		return string == null ?
				null :
				Template.renderWhereStringTemplate( string, factory.getDialect(), factory.getSqlFunctionRegistry() );
	}

	protected Map<String,String> generateLazySelectStringsByFetchGroup() {
		final BytecodeEnhancementMetadata enhancementMetadata = entityMetamodel.getBytecodeEnhancementMetadata();
		if ( !enhancementMetadata.isEnhancedForLazyLoading()
				|| !enhancementMetadata.getLazyAttributesMetadata().hasLazyAttributes() ) {
			return Collections.emptyMap();
		}

		Map<String,String> result = new HashMap<>();

		final LazyAttributesMetadata lazyAttributesMetadata = enhancementMetadata.getLazyAttributesMetadata();
		for ( String groupName : lazyAttributesMetadata.getFetchGroupNames() ) {
			HashSet tableNumbers = new HashSet();
			ArrayList columnNumbers = new ArrayList();
			ArrayList formulaNumbers = new ArrayList();

			for ( LazyAttributeDescriptor lazyAttributeDescriptor :
					lazyAttributesMetadata.getFetchGroupAttributeDescriptors( groupName ) ) {
				// all this only really needs to consider properties
				// of this class, not its subclasses, but since we
				// are reusing code used for sequential selects, we
				// use the subclass closure
				int propertyNumber = getSubclassPropertyIndex( lazyAttributeDescriptor.getName() );

				int tableNumber = getSubclassPropertyTableNumber( propertyNumber );
				tableNumbers.add( tableNumber );

				int[] colNumbers = subclassPropertyColumnNumberClosure[propertyNumber];
				for ( int colNumber : colNumbers ) {
					if ( colNumber != -1 ) {
						columnNumbers.add( colNumber );
					}
				}
				int[] formNumbers = subclassPropertyFormulaNumberClosure[propertyNumber];
				for ( int formNumber : formNumbers ) {
					if ( formNumber != -1 ) {
						formulaNumbers.add( formNumber );
					}
				}
			}

			if ( columnNumbers.size() == 0 && formulaNumbers.size() == 0 ) {
				// only one-to-one is lazy fetched
				continue;
			}

			result.put(
					groupName,
					renderSelect(
							ArrayHelper.toIntArray( tableNumbers ),
							ArrayHelper.toIntArray( columnNumbers ),
							ArrayHelper.toIntArray( formulaNumbers )
					)
			);
		}

		return result;
	}

	public Object initializeLazyProperty(String fieldName, Object entity, SharedSessionContractImplementor session) {
		final EntityEntry entry = session.getPersistenceContext().getEntry( entity );
		final InterceptorImplementor interceptor = ( (PersistentAttributeInterceptable) entity ).$$_hibernate_getInterceptor();
		assert interceptor != null : "Expecting bytecode interceptor to be non-null";

		if ( hasCollections() ) {
			final Type type = getPropertyType( fieldName );
			if ( type.isCollectionType() ) {
				// we have a condition where a collection attribute is being access via enhancement:
				// 		we can circumvent all the rest and just return the PersistentCollection
				final CollectionType collectionType = (CollectionType) type;
				final CollectionPersister persister = factory.getMetamodel().collectionPersister( collectionType.getRole() );

				// Get/create the collection, and make sure it is initialized!  This initialized part is
				// different from proxy-based scenarios where we have to create the PersistentCollection
				// reference "ahead of time" to add as a reference to the proxy.  For bytecode solutions
				// we are not creating the PersistentCollection ahead of time, but instead we are creating
				// it on first request through the enhanced entity.

				// see if there is already a collection instance associated with the session
				// 		NOTE : can this ever happen?
				final Serializable key = getCollectionKey( persister, entity, entry, session );
				PersistentCollection collection = session.getPersistenceContext().getCollection( new CollectionKey( persister, key ) );
				if ( collection == null ) {
					collection = collectionType.instantiate( session, persister, key );
					collection.setOwner( entity );
					session.getPersistenceContext().addUninitializedCollection( persister, collection, key );
				}

				// HHH-11161 Initialize, if the collection is not extra lazy
				if ( !persister.isExtraLazy() ) {
					session.initializeCollection( collection, false );
				}
				interceptor.attributeInitialized( fieldName );

				if ( collectionType.isArrayType() ) {
					session.getPersistenceContext().addCollectionHolder( collection );
				}

				// update the "state" of the entity's EntityEntry to over-write UNFETCHED_PROPERTY reference
				// for the collection to the just loaded collection
				final EntityEntry ownerEntry = session.getPersistenceContext().getEntry( entity );
				if ( ownerEntry == null ) {
					// not good
					throw new AssertionFailure(
							"Could not locate EntityEntry for the collection owner in the PersistenceContext"
					);
				}
				ownerEntry.overwriteLoadedStateCollectionValue( fieldName, collection );

				// EARLY EXIT!!!
				return collection;
			}
		}

		final Serializable id = session.getContextEntityIdentifier( entity );
		if ( entry == null ) {
			throw new HibernateException( "entity is not associated with the session: " + id );
		}

		if ( LOG.isTraceEnabled() ) {
			LOG.tracev(
					"Initializing lazy properties of: {0}, field access: {1}", MessageHelper.infoString(
							this,
							id,
							getFactory()
					), fieldName
			);
		}

		if ( session.getCacheMode().isGetEnabled() && hasCache() && isLazyPropertiesCacheable() ) {
			final EntityRegionAccessStrategy cache = getCacheAccessStrategy();
			final Object cacheKey = cache.generateCacheKey(id, this, session.getFactory(), session.getTenantIdentifier() );
			final Object ce = CacheHelper.fromSharedCache( session, cacheKey, cache );
			if ( ce != null ) {
				final CacheEntry cacheEntry = (CacheEntry) getCacheEntryStructure().destructure( ce, factory );
				final Object initializedValue = initializeLazyPropertiesFromCache( fieldName, entity, session, entry, cacheEntry );
				interceptor.attributeInitialized( fieldName );

				// NOTE EARLY EXIT!!!
				return initializedValue;
			}
		}

		return initializeLazyPropertiesFromDatastore( fieldName, entity, session, id, entry );

	}

	protected Serializable getCollectionKey(
			CollectionPersister persister,
			Object owner,
			EntityEntry ownerEntry,
			SharedSessionContractImplementor session) {
		final CollectionType collectionType = persister.getCollectionType();

		if ( ownerEntry != null ) {
			// this call only works when the owner is associated with the Session, which is not always the case
			return collectionType.getKeyOfOwner( owner, session );
		}

		if ( collectionType.getLHSPropertyName() == null ) {
			// collection key is defined by the owning entity identifier
			return persister.getOwnerEntityPersister().getIdentifier( owner, session );
		}
		else {
			return (Serializable) persister.getOwnerEntityPersister().getPropertyValue( owner, collectionType.getLHSPropertyName() );
		}
	}

	private Object initializeLazyPropertiesFromDatastore(
			final String fieldName,
			final Object entity,
			final SharedSessionContractImplementor session,
			final Serializable id,
			final EntityEntry entry) {

		if ( !hasLazyProperties() ) {
			throw new AssertionFailure( "no lazy properties" );
		}

		final InterceptorImplementor interceptor = ( (PersistentAttributeInterceptable) entity ).$$_hibernate_getInterceptor();
		assert interceptor != null : "Expecting bytecode interceptor to be non-null";

		LOG.trace( "Initializing lazy properties from datastore" );

		final String fetchGroup = getEntityMetamodel().getBytecodeEnhancementMetadata()
				.getLazyAttributesMetadata()
				.getFetchGroupName( fieldName );
		final List<LazyAttributeDescriptor> fetchGroupAttributeDescriptors = getEntityMetamodel().getBytecodeEnhancementMetadata()
				.getLazyAttributesMetadata()
				.getFetchGroupAttributeDescriptors( fetchGroup );

		final Set<String> initializedLazyAttributeNames = interceptor.getInitializedLazyAttributeNames();

		final String lazySelect = getSQLLazySelectString( fetchGroup );

		try {
			Object result = null;
			PreparedStatement ps = null;
			try {
				ResultSet rs = null;
				try {
					if ( lazySelect != null ) {
						// null sql means that the only lazy properties
						// are shared PK one-to-one associations which are
						// handled differently in the Type#nullSafeGet code...
						ps = session.getJdbcCoordinator()
								.getStatementPreparer()
								.prepareStatement( lazySelect );
						getIdentifierType().nullSafeSet( ps, id, 1, session );
						rs = session.getJdbcCoordinator().getResultSetReturn().extract( ps );
						rs.next();
					}
					final Object[] snapshot = entry.getLoadedState();
					for ( LazyAttributeDescriptor fetchGroupAttributeDescriptor : fetchGroupAttributeDescriptors ) {
						final boolean previousInitialized = initializedLazyAttributeNames.contains( fetchGroupAttributeDescriptor.getName() );

						if ( previousInitialized ) {
							// todo : one thing we should consider here is potentially un-marking an attribute as dirty based on the selected value
							// 		we know the current value - getPropertyValue( entity, fetchGroupAttributeDescriptor.getAttributeIndex() );
							// 		we know the selected value (see selectedValue below)
							//		we can use the attribute Type to tell us if they are the same
							//
							//		assuming entity is a SelfDirtinessTracker we can also know if the attribute is
							//			currently considered dirty, and if really not dirty we would do the un-marking
							//
							//		of course that would mean a new method on SelfDirtinessTracker to allow un-marking

							// its already been initialized (e.g. by a write) so we don't want to overwrite
							continue;
						}


						final Object selectedValue = fetchGroupAttributeDescriptor.getType().nullSafeGet(
								rs,
								lazyPropertyColumnAliases[fetchGroupAttributeDescriptor.getLazyIndex()],
								session,
								entity
						);

						final boolean set = initializeLazyProperty(
								fieldName,
								entity,
								session,
								snapshot,
								fetchGroupAttributeDescriptor.getLazyIndex(),
								selectedValue
						);
						if ( set ) {
							result = selectedValue;
							interceptor.attributeInitialized( fetchGroupAttributeDescriptor.getName() );
						}

					}
				}
				finally {
					if ( rs != null ) {
						session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( rs, ps );
					}
				}
			}
			finally {
				if ( ps != null ) {
					session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( ps );
					session.getJdbcCoordinator().afterStatementExecution();
				}
			}

			LOG.trace( "Done initializing lazy properties" );

			return result;

		}
		catch (SQLException sqle) {
			throw session.getJdbcServices().getSqlExceptionHelper().convert(
					sqle,
					"could not initialize lazy properties: " + MessageHelper.infoString( this, id, getFactory() ),
					lazySelect
			);
		}
	}

	private Object initializeLazyPropertiesFromCache(
			final String fieldName,
			final Object entity,
			final SharedSessionContractImplementor session,
			final EntityEntry entry,
			final CacheEntry cacheEntry) {

		LOG.trace( "Initializing lazy properties from second-level cache" );

		Object result = null;
		Serializable[] disassembledValues = cacheEntry.getDisassembledState();
		final Object[] snapshot = entry.getLoadedState();
		for ( int j = 0; j < lazyPropertyNames.length; j++ ) {
			final Object propValue = lazyPropertyTypes[j].assemble(
					disassembledValues[lazyPropertyNumbers[j]],
					session,
					entity
			);
			if ( initializeLazyProperty( fieldName, entity, session, snapshot, j, propValue ) ) {
				result = propValue;
			}
		}

		LOG.trace( "Done initializing lazy properties" );

		return result;
	}

	private boolean initializeLazyProperty(
			final String fieldName,
			final Object entity,
			final SharedSessionContractImplementor session,
			final Object[] snapshot,
			final int j,
			final Object propValue) {
		setPropertyValue( entity, lazyPropertyNumbers[j], propValue );
		if ( snapshot != null ) {
			// object have been loaded with setReadOnly(true); HHH-2236
			snapshot[lazyPropertyNumbers[j]] = lazyPropertyTypes[j].deepCopy( propValue, factory );
		}
		return fieldName.equals( lazyPropertyNames[j] );
	}

	public boolean isBatchable() {
		return optimisticLockStyle() == OptimisticLockStyle.NONE
				|| ( !isVersioned() && optimisticLockStyle() == OptimisticLockStyle.VERSION )
				|| getFactory().getSessionFactoryOptions().isJdbcBatchVersionedData();
	}

	public Serializable[] getQuerySpaces() {
		return getPropertySpaces();
	}

	public boolean isBatchLoadable() {
		return batchSize > 1;
	}

	public String[] getIdentifierColumnNames() {
		return rootTableKeyColumnNames;
	}

	public String[] getIdentifierColumnReaders() {
		return rootTableKeyColumnReaders;
	}

	public String[] getIdentifierColumnReaderTemplates() {
		return rootTableKeyColumnReaderTemplates;
	}

	protected int getIdentifierColumnSpan() {
		return identifierColumnSpan;
	}

	protected String[] getIdentifierAliases() {
		return identifierAliases;
	}

	public String getVersionColumnName() {
		return versionColumnName;
	}

	protected String getVersionedTableName() {
		return getTableName( 0 );
	}

	protected boolean[] getSubclassColumnLazyiness() {
		return subclassColumnLazyClosure;
	}

	protected boolean[] getSubclassFormulaLazyiness() {
		return subclassFormulaLazyClosure;
	}

	/**
	 * We can't immediately add to the cache if we have formulas
	 * which must be evaluated, or if we have the possibility of
	 * two concurrent updates to the same item being merged on
	 * the database. This can happen if (a) the item is not
	 * versioned and either (b) we have dynamic update enabled
	 * or (c) we have multiple tables holding the state of the
	 * item.
	 */
	public boolean isCacheInvalidationRequired() {
		return hasFormulaProperties() ||
				( !isVersioned() && ( entityMetamodel.isDynamicUpdate() || getTableSpan() > 1 ) );
	}

	public boolean isLazyPropertiesCacheable() {
		return isLazyPropertiesCacheable;
	}

	public String selectFragment(String alias, String suffix) {
		return identifierSelectFragment( alias, suffix ) +
				propertySelectFragment( alias, suffix, false );
	}

	public String[] getIdentifierAliases(String suffix) {
		// NOTE: this assumes something about how propertySelectFragment is implemented by the subclass!
		// was toUnqotedAliasStrings( getIdentifierColumnNames() ) beforeQuery - now tried
		// to remove that unqoting and missing aliases..
		return new Alias( suffix ).toAliasStrings( getIdentifierAliases() );
	}

	public String[] getPropertyAliases(String suffix, int i) {
		// NOTE: this assumes something about how propertySelectFragment is implemented by the subclass!
		return new Alias( suffix ).toUnquotedAliasStrings( propertyColumnAliases[i] );
	}

	public String getDiscriminatorAlias(String suffix) {
		// NOTE: this assumes something about how propertySelectFragment is implemented by the subclass!
		// was toUnqotedAliasStrings( getdiscriminatorColumnName() ) beforeQuery - now tried
		// to remove that unqoting and missing aliases..
		return entityMetamodel.hasSubclasses() ?
				new Alias( suffix ).toAliasString( getDiscriminatorAlias() ) :
				null;
	}

	public String identifierSelectFragment(String name, String suffix) {
		return new SelectFragment()
				.setSuffix( suffix )
				.addColumns( name, getIdentifierColumnNames(), getIdentifierAliases() )
				.toFragmentString()
				.substring( 2 ); //strip leading ", "
	}


	public String propertySelectFragment(String tableAlias, String suffix, boolean allProperties) {
		return propertySelectFragmentFragment( tableAlias, suffix, allProperties ).toFragmentString();
	}

	public SelectFragment propertySelectFragmentFragment(
			String tableAlias,
			String suffix,
			boolean allProperties) {
		SelectFragment select = new SelectFragment()
				.setSuffix( suffix )
				.setUsedAliases( getIdentifierAliases() );

		int[] columnTableNumbers = getSubclassColumnTableNumberClosure();
		String[] columnAliases = getSubclassColumnAliasClosure();
		String[] columnReaderTemplates = getSubclassColumnReaderTemplateClosure();
		for ( int i = 0; i < getSubclassColumnClosure().length; i++ ) {
			boolean selectable = ( allProperties || !subclassColumnLazyClosure[i] ) &&
					!isSubclassTableSequentialSelect( columnTableNumbers[i] ) &&
					subclassColumnSelectableClosure[i];
			if ( selectable ) {
				String subalias = generateTableAlias( tableAlias, columnTableNumbers[i] );
				select.addColumnTemplate( subalias, columnReaderTemplates[i], columnAliases[i] );
			}
		}

		int[] formulaTableNumbers = getSubclassFormulaTableNumberClosure();
		String[] formulaTemplates = getSubclassFormulaTemplateClosure();
		String[] formulaAliases = getSubclassFormulaAliasClosure();
		for ( int i = 0; i < getSubclassFormulaTemplateClosure().length; i++ ) {
			boolean selectable = ( allProperties || !subclassFormulaLazyClosure[i] )
					&& !isSubclassTableSequentialSelect( formulaTableNumbers[i] );
			if ( selectable ) {
				String subalias = generateTableAlias( tableAlias, formulaTableNumbers[i] );
				select.addFormula( subalias, formulaTemplates[i], formulaAliases[i] );
			}
		}

		if ( entityMetamodel.hasSubclasses() ) {
			addDiscriminatorToSelect( select, tableAlias, suffix );
		}

		if ( hasRowId() ) {
			select.addColumn( tableAlias, rowIdName, ROWID_ALIAS );
		}

		return select;
	}

	public Object[] getDatabaseSnapshot(Serializable id, SharedSessionContractImplementor session)
			throws HibernateException {

		if ( LOG.isTraceEnabled() ) {
			LOG.tracev(
					"Getting current persistent state for: {0}", MessageHelper.infoString(
							this,
							id,
							getFactory()
					)
			);
		}

		try {
			PreparedStatement ps = session
					.getJdbcCoordinator()
					.getStatementPreparer()
					.prepareStatement( getSQLSnapshotSelectString() );
			try {
				getIdentifierType().nullSafeSet( ps, id, 1, session );
				//if ( isVersioned() ) getVersionType().nullSafeSet( ps, version, getIdentifierColumnSpan()+1, session );
				ResultSet rs = session.getJdbcCoordinator().getResultSetReturn().extract( ps );
				try {
					//if there is no resulting row, return null
					if ( !rs.next() ) {
						return null;
					}
					//otherwise return the "hydrated" state (ie. associations are not resolved)
					Type[] types = getPropertyTypes();
					Object[] values = new Object[types.length];
					boolean[] includeProperty = getPropertyUpdateability();
					for ( int i = 0; i < types.length; i++ ) {
						if ( includeProperty[i] ) {
							values[i] = types[i].hydrate(
									rs,
									getPropertyAliases( "", i ),
									session,
									null
							); //null owner ok??
						}
					}
					return values;
				}
				finally {
					session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( rs, ps );
				}
			}
			finally {
				session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( ps );
				session.getJdbcCoordinator().afterStatementExecution();
			}
		}
		catch (SQLException e) {
			throw session.getJdbcServices().getSqlExceptionHelper().convert(
					e,
					"could not retrieve snapshot: " + MessageHelper.infoString( this, id, getFactory() ),
					getSQLSnapshotSelectString()
			);
		}

	}

	@Override
	public Serializable getIdByUniqueKey(Serializable key, String uniquePropertyName, SharedSessionContractImplementor session)
			throws HibernateException {
		if ( LOG.isTraceEnabled() ) {
			LOG.tracef(
					"resolving unique key [%s] to identifier for entity [%s]",
					key,
					getEntityName()
			);
		}

		int propertyIndex = getSubclassPropertyIndex( uniquePropertyName );
		if ( propertyIndex < 0 ) {
			throw new HibernateException(
					"Could not determine Type for property [" + uniquePropertyName + "] on entity [" + getEntityName() + "]"
			);
		}
		Type propertyType = getSubclassPropertyType( propertyIndex );

		try {
			PreparedStatement ps = session
					.getJdbcCoordinator()
					.getStatementPreparer()
					.prepareStatement( generateIdByUniqueKeySelectString( uniquePropertyName ) );
			try {
				propertyType.nullSafeSet( ps, key, 1, session );
				ResultSet rs = session.getJdbcCoordinator().getResultSetReturn().extract( ps );
				try {
					//if there is no resulting row, return null
					if ( !rs.next() ) {
						return null;
					}
					return (Serializable) getIdentifierType().nullSafeGet( rs, getIdentifierAliases(), session, null );
				}
				finally {
					session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( rs, ps );
				}
			}
			finally {
				session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( ps );
				session.getJdbcCoordinator().afterStatementExecution();
			}
		}
		catch (SQLException e) {
			throw session.getJdbcServices().getSqlExceptionHelper().convert(
					e,
					String.format(
							"could not resolve unique property [%s] to identifier for entity [%s]",
							uniquePropertyName,
							getEntityName()
					),
					getSQLSnapshotSelectString()
			);
		}

	}

	protected String generateIdByUniqueKeySelectString(String uniquePropertyName) {
		Select select = new Select( getFactory().getDialect() );

		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			select.setComment( "resolve id by unique property [" + getEntityName() + "." + uniquePropertyName + "]" );
		}

		final String rooAlias = getRootAlias();

		select.setFromClause( fromTableFragment( rooAlias ) + fromJoinFragment( rooAlias, true, false ) );

		SelectFragment selectFragment = new SelectFragment();
		selectFragment.addColumns( rooAlias, getIdentifierColumnNames(), getIdentifierAliases() );
		select.setSelectClause( selectFragment );

		StringBuilder whereClauseBuffer = new StringBuilder();
		final int uniquePropertyIndex = getSubclassPropertyIndex( uniquePropertyName );
		final String uniquePropertyTableAlias = generateTableAlias(
				rooAlias,
				getSubclassPropertyTableNumber( uniquePropertyIndex )
		);
		String sep = "";
		for ( String columnTemplate : getSubclassPropertyColumnReaderTemplateClosure()[uniquePropertyIndex] ) {
			if ( columnTemplate == null ) {
				continue;
			}
			final String columnReference = StringHelper.replace(
					columnTemplate,
					Template.TEMPLATE,
					uniquePropertyTableAlias
			);
			whereClauseBuffer.append( sep ).append( columnReference ).append( "=?" );
			sep = " and ";
		}
		for ( String formulaTemplate : getSubclassPropertyFormulaTemplateClosure()[uniquePropertyIndex] ) {
			if ( formulaTemplate == null ) {
				continue;
			}
			final String formulaReference = StringHelper.replace(
					formulaTemplate,
					Template.TEMPLATE,
					uniquePropertyTableAlias
			);
			whereClauseBuffer.append( sep ).append( formulaReference ).append( "=?" );
			sep = " and ";
		}
		whereClauseBuffer.append( whereJoinFragment( rooAlias, true, false ) );

		select.setWhereClause( whereClauseBuffer.toString() );

		return select.setOuterJoins( "", "" ).toStatementString();
	}


	/**
	 * Generate the SQL that selects the version number by id
	 */
	protected String generateSelectVersionString() {
		SimpleSelect select = new SimpleSelect( getFactory().getDialect() )
				.setTableName( getVersionedTableName() );
		if ( isVersioned() ) {
			select.addColumn( versionColumnName );
		}
		else {
			select.addColumns( rootTableKeyColumnNames );
		}
		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			select.setComment( "get version " + getEntityName() );
		}
		return select.addCondition( rootTableKeyColumnNames, "=?" ).toStatementString();
	}

	public boolean[] getPropertyUniqueness() {
		return propertyUniqueness;
	}

	protected String generateInsertGeneratedValuesSelectString() {
		return generateGeneratedValuesSelectString( GenerationTiming.INSERT );
	}

	protected String generateUpdateGeneratedValuesSelectString() {
		return generateGeneratedValuesSelectString( GenerationTiming.ALWAYS );
	}

	private String generateGeneratedValuesSelectString(final GenerationTiming generationTimingToMatch) {
		Select select = new Select( getFactory().getDialect() );

		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			select.setComment( "get generated state " + getEntityName() );
		}

		String[] aliasedIdColumns = StringHelper.qualify( getRootAlias(), getIdentifierColumnNames() );

		// Here we render the select column list based on the properties defined as being generated.
		// For partial component generation, we currently just re-select the whole component
		// rather than trying to handle the individual generated portions.
		String selectClause = concretePropertySelectFragment(
				getRootAlias(),
				new InclusionChecker() {
					@Override
					public boolean includeProperty(int propertyNumber) {
						final InDatabaseValueGenerationStrategy generationStrategy
								= entityMetamodel.getInDatabaseValueGenerationStrategies()[propertyNumber];
						return generationStrategy != null
								&& timingsMatch( generationStrategy.getGenerationTiming(), generationTimingToMatch );
					}
				}
		);
		selectClause = selectClause.substring( 2 );

		String fromClause = fromTableFragment( getRootAlias() ) +
				fromJoinFragment( getRootAlias(), true, false );

		String whereClause = new StringBuilder()
				.append( StringHelper.join( "=? and ", aliasedIdColumns ) )
				.append( "=?" )
				.append( whereJoinFragment( getRootAlias(), true, false ) )
				.toString();

		return select.setSelectClause( selectClause )
				.setFromClause( fromClause )
				.setOuterJoins( "", "" )
				.setWhereClause( whereClause )
				.toStatementString();
	}

	protected static interface InclusionChecker {
		public boolean includeProperty(int propertyNumber);
	}

	protected String concretePropertySelectFragment(String alias, final boolean[] includeProperty) {
		return concretePropertySelectFragment(
				alias,
				new InclusionChecker() {
					public boolean includeProperty(int propertyNumber) {
						return includeProperty[propertyNumber];
					}
				}
		);
	}

	protected String concretePropertySelectFragment(String alias, InclusionChecker inclusionChecker) {
		int propertyCount = getPropertyNames().length;
		int[] propertyTableNumbers = getPropertyTableNumbersInSelect();
		SelectFragment frag = new SelectFragment();
		for ( int i = 0; i < propertyCount; i++ ) {
			if ( inclusionChecker.includeProperty( i ) ) {
				frag.addColumnTemplates(
						generateTableAlias( alias, propertyTableNumbers[i] ),
						propertyColumnReaderTemplates[i],
						propertyColumnAliases[i]
				);
				frag.addFormulas(
						generateTableAlias( alias, propertyTableNumbers[i] ),
						propertyColumnFormulaTemplates[i],
						propertyColumnAliases[i]
				);
			}
		}
		return frag.toFragmentString();
	}

	protected String generateSnapshotSelectString() {

		//TODO: should we use SELECT .. FOR UPDATE?

		Select select = new Select( getFactory().getDialect() );

		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			select.setComment( "get current state " + getEntityName() );
		}

		String[] aliasedIdColumns = StringHelper.qualify( getRootAlias(), getIdentifierColumnNames() );
		String selectClause = StringHelper.join( ", ", aliasedIdColumns ) +
				concretePropertySelectFragment( getRootAlias(), getPropertyUpdateability() );

		String fromClause = fromTableFragment( getRootAlias() ) +
				fromJoinFragment( getRootAlias(), true, false );

		String whereClause = new StringBuilder()
				.append(
						StringHelper.join(
								"=? and ",
								aliasedIdColumns
						)
				)
				.append( "=?" )
				.append( whereJoinFragment( getRootAlias(), true, false ) )
				.toString();

		/*if ( isVersioned() ) {
			where.append(" and ")
				.append( getVersionColumnName() )
				.append("=?");
		}*/

		return select.setSelectClause( selectClause )
				.setFromClause( fromClause )
				.setOuterJoins( "", "" )
				.setWhereClause( whereClause )
				.toStatementString();
	}

	public Object forceVersionIncrement(Serializable id, Object currentVersion, SharedSessionContractImplementor session) {
		if ( !isVersioned() ) {
			throw new AssertionFailure( "cannot force version increment on non-versioned entity" );
		}

		if ( isVersionPropertyGenerated() ) {
			// the difficulty here is exactly what do we update in order to
			// force the version to be incremented in the db...
			throw new HibernateException( "LockMode.FORCE is currently not supported for generated version properties" );
		}

		Object nextVersion = getVersionType().next( currentVersion, session );
		if ( LOG.isTraceEnabled() ) {
			LOG.trace(
					"Forcing version increment [" + MessageHelper.infoString( this, id, getFactory() ) + "; "
							+ getVersionType().toLoggableString( currentVersion, getFactory() ) + " -> "
							+ getVersionType().toLoggableString( nextVersion, getFactory() ) + "]"
			);
		}

		// todo : cache this sql...
		String versionIncrementString = generateVersionIncrementUpdateString();
		PreparedStatement st = null;
		try {
			st = session
					.getJdbcCoordinator()
					.getStatementPreparer()
					.prepareStatement( versionIncrementString, false );
			try {
				getVersionType().nullSafeSet( st, nextVersion, 1, session );
				getIdentifierType().nullSafeSet( st, id, 2, session );
				getVersionType().nullSafeSet( st, currentVersion, 2 + getIdentifierColumnSpan(), session );
				int rows = session.getJdbcCoordinator().getResultSetReturn().executeUpdate( st );
				if ( rows != 1 ) {
					throw new StaleObjectStateException( getEntityName(), id );
				}
			}
			finally {
				session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( st );
				session.getJdbcCoordinator().afterStatementExecution();
			}
		}
		catch (SQLException sqle) {
			throw session.getJdbcServices().getSqlExceptionHelper().convert(
					sqle,
					"could not retrieve version: " +
							MessageHelper.infoString( this, id, getFactory() ),
					getVersionSelectString()
			);
		}

		return nextVersion;
	}

	private String generateVersionIncrementUpdateString() {
		Update update = new Update( getFactory().getDialect() );
		update.setTableName( getTableName( 0 ) );
		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			update.setComment( "forced version increment" );
		}
		update.addColumn( getVersionColumnName() );
		update.addPrimaryKeyColumns( getIdentifierColumnNames() );
		update.setVersionColumnName( getVersionColumnName() );
		return update.toStatementString();
	}

	/**
	 * Retrieve the version number
	 */
	public Object getCurrentVersion(Serializable id, SharedSessionContractImplementor session) throws HibernateException {

		if ( LOG.isTraceEnabled() ) {
			LOG.tracev( "Getting version: {0}", MessageHelper.infoString( this, id, getFactory() ) );
		}

		try {
			PreparedStatement st = session
					.getJdbcCoordinator()
					.getStatementPreparer()
					.prepareStatement( getVersionSelectString() );
			try {
				getIdentifierType().nullSafeSet( st, id, 1, session );
				ResultSet rs = session.getJdbcCoordinator().getResultSetReturn().extract( st );
				try {
					if ( !rs.next() ) {
						return null;
					}
					if ( !isVersioned() ) {
						return this;
					}
					return getVersionType().nullSafeGet( rs, getVersionColumnName(), session, null );
				}
				finally {
					session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( rs, st );
				}
			}
			finally {
				session.getJdbcCoordinator().getLogicalConnection().getResourceRegistry().release( st );
				session.getJdbcCoordinator().afterStatementExecution();
			}
		}
		catch (SQLException e) {
			throw session.getJdbcServices().getSqlExceptionHelper().convert(
					e,
					"could not retrieve version: " + MessageHelper.infoString( this, id, getFactory() ),
					getVersionSelectString()
			);
		}
	}

	protected void initLockers() {
		lockers.put( LockMode.READ, generateLocker( LockMode.READ ) );
		lockers.put( LockMode.UPGRADE, generateLocker( LockMode.UPGRADE ) );
		lockers.put( LockMode.UPGRADE_NOWAIT, generateLocker( LockMode.UPGRADE_NOWAIT ) );
		lockers.put( LockMode.UPGRADE_SKIPLOCKED, generateLocker( LockMode.UPGRADE_SKIPLOCKED ) );
		lockers.put( LockMode.FORCE, generateLocker( LockMode.FORCE ) );
		lockers.put( LockMode.PESSIMISTIC_READ, generateLocker( LockMode.PESSIMISTIC_READ ) );
		lockers.put( LockMode.PESSIMISTIC_WRITE, generateLocker( LockMode.PESSIMISTIC_WRITE ) );
		lockers.put( LockMode.PESSIMISTIC_FORCE_INCREMENT, generateLocker( LockMode.PESSIMISTIC_FORCE_INCREMENT ) );
		lockers.put( LockMode.OPTIMISTIC, generateLocker( LockMode.OPTIMISTIC ) );
		lockers.put( LockMode.OPTIMISTIC_FORCE_INCREMENT, generateLocker( LockMode.OPTIMISTIC_FORCE_INCREMENT ) );
	}

	protected LockingStrategy generateLocker(LockMode lockMode) {
		return factory.getDialect().getLockingStrategy( this, lockMode );
	}

	private LockingStrategy getLocker(LockMode lockMode) {
		return (LockingStrategy) lockers.get( lockMode );
	}

	public void lock(
			Serializable id,
			Object version,
			Object object,
			LockMode lockMode,
			SharedSessionContractImplementor session) throws HibernateException {
		getLocker( lockMode ).lock( id, version, object, LockOptions.WAIT_FOREVER, session );
	}

	public void lock(
			Serializable id,
			Object version,
			Object object,
			LockOptions lockOptions,
			SharedSessionContractImplementor session) throws HibernateException {
		getLocker( lockOptions.getLockMode() ).lock( id, version, object, lockOptions.getTimeOut(), session );
	}

	public String getRootTableName() {
		return getSubclassTableName( 0 );
	}

	public String getRootTableAlias(String drivingAlias) {
		return drivingAlias;
	}

	public String[] getRootTableIdentifierColumnNames() {
		return getRootTableKeyColumnNames();
	}

	/**
	 * {@inheritDoc}
	 *
	 * Warning:
	 * When there are duplicated property names in the subclasses
	 * then this method may return the wrong results.
	 * To ensure correct results, this method should only be used when
	 * {@literal this} is the concrete EntityPersister (since the
	 * concrete EntityPersister cannot have duplicated property names).
	 */
	@Override
	public String[] toColumns(String alias, String propertyName) throws QueryException {
		return propertyMapping.toColumns( alias, propertyName );
	}

	/**
	 * {@inheritDoc}
	 *
	 * Warning:
	 * When there are duplicated property names in the subclasses
	 * then this method may return the wrong results.
	 * To ensure correct results, this method should only be used when
	 * {@literal this} is the concrete EntityPersister (since the
	 * concrete EntityPersister cannot have duplicated property names).
	 */
	@Override
	public String[] toColumns(String propertyName) throws QueryException {
		return propertyMapping.getColumnNames( propertyName );
	}

	/**
	 * {@inheritDoc}
	 *
	 * Warning:
	 * When there are duplicated property names in the subclasses
	 * then this method may return the wrong results.
	 * To ensure correct results, this method should only be used when
	 * {@literal this} is the concrete EntityPersister (since the
	 * concrete EntityPersister cannot have duplicated property names).
	 */
	@Override
	public Type toType(String propertyName) throws QueryException {
		return propertyMapping.toType( propertyName );
	}

	/**
	 * {@inheritDoc}
	 *
	 * Warning:
	 * When there are duplicated property names in the subclasses
	 * then this method may return the wrong results.
	 * To ensure correct results, this method should only be used when
	 * {@literal this} is the concrete EntityPersister (since the
	 * concrete EntityPersister cannot have duplicated property names).
	 */
	@Override
	public String[] getPropertyColumnNames(String propertyName) {
		return propertyMapping.getColumnNames( propertyName );
	}

	/**
	 * Warning:
	 * When there are duplicated property names in the subclasses
	 * of the class, this method may return the wrong table
	 * number for the duplicated subclass property (note that
	 * SingleTableEntityPersister defines an overloaded form
	 * which takes the entity name.
	 */
	public int getSubclassPropertyTableNumber(String propertyPath) {
		String rootPropertyName = StringHelper.root( propertyPath );
		Type type = propertyMapping.toType( rootPropertyName );
		if ( type.isAssociationType() ) {
			AssociationType assocType = (AssociationType) type;
			if ( assocType.useLHSPrimaryKey() ) {
				// performance op to avoid the array search
				return 0;
			}
			else if ( type.isCollectionType() ) {
				// properly handle property-ref-based associations
				rootPropertyName = assocType.getLHSPropertyName();
			}
		}
		//Enable for HHH-440, which we don't like:
		/*if ( type.isComponentType() && !propertyName.equals(rootPropertyName) ) {
			String unrooted = StringHelper.unroot(propertyName);
			int idx = ArrayHelper.indexOf( getSubclassColumnClosure(), unrooted );
			if ( idx != -1 ) {
				return getSubclassColumnTableNumberClosure()[idx];
			}
		}*/
		int index = ArrayHelper.indexOf(
				getSubclassPropertyNameClosure(),
				rootPropertyName
		); //TODO: optimize this better!
		return index == -1 ? 0 : getSubclassPropertyTableNumber( index );
	}

	public Declarer getSubclassPropertyDeclarer(String propertyPath) {
		int tableIndex = getSubclassPropertyTableNumber( propertyPath );
		if ( tableIndex == 0 ) {
			return Declarer.CLASS;
		}
		else if ( isClassOrSuperclassTable( tableIndex ) ) {
			return Declarer.SUPERCLASS;
		}
		else {
			return Declarer.SUBCLASS;
		}
	}

	private DiscriminatorMetadata discriminatorMetadata;

	public DiscriminatorMetadata getTypeDiscriminatorMetadata() {
		if ( discriminatorMetadata == null ) {
			discriminatorMetadata = buildTypeDiscriminatorMetadata();
		}
		return discriminatorMetadata;
	}

	private DiscriminatorMetadata buildTypeDiscriminatorMetadata() {
		return new DiscriminatorMetadata() {
			public String getSqlFragment(String sqlQualificationAlias) {
				return toColumns( sqlQualificationAlias, ENTITY_CLASS )[0];
			}

			public Type getResolutionType() {
				return new DiscriminatorType( getDiscriminatorType(), AbstractEntityPersister.this );
			}
		};
	}

	public static String generateTableAlias(String rootAlias, int tableNumber) {
		if ( tableNumber == 0 ) {
			return rootAlias;
		}
		StringBuilder buf = new StringBuilder().append( rootAlias );
		if ( !rootAlias.endsWith( "_" ) ) {
			buf.append( '_' );
		}
		return buf.append( tableNumber ).append( '_' ).toString();
	}

	public String[] toColumns(String name, final int i) {
		final String alias = generateTableAlias( name, getSubclassPropertyTableNumber( i ) );
		String[] cols = getSubclassPropertyColumnNames( i );
		String[] templates = getSubclassPropertyFormulaTemplateClosure()[i];
		String[] result = new String[cols.length];
		for ( int j = 0; j < cols.length; j++ ) {
			if ( cols[j] == null ) {
				result[j] = StringHelper.replace( templates[j], Template.TEMPLATE, alias );
			}
			else {
				result[j] = StringHelper.qualify( alias, cols[j] );
			}
		}
		return result;
	}

	private int getSubclassPropertyIndex(String propertyName) {
		return ArrayHelper.indexOf( subclassPropertyNameClosure, propertyName );
	}

	protected String[] getPropertySubclassNames() {
		return propertySubclassNames;
	}

	public String[] getPropertyColumnNames(int i) {
		return propertyColumnNames[i];
	}

	public String[] getPropertyColumnWriters(int i) {
		return propertyColumnWriters[i];
	}

	protected int getPropertyColumnSpan(int i) {
		return propertyColumnSpans[i];
	}

	protected boolean hasFormulaProperties() {
		return hasFormulaProperties;
	}

	public FetchMode getFetchMode(int i) {
		return subclassPropertyFetchModeClosure[i];
	}

	public CascadeStyle getCascadeStyle(int i) {
		return subclassPropertyCascadeStyleClosure[i];
	}

	public Type getSubclassPropertyType(int i) {
		return subclassPropertyTypeClosure[i];
	}

	public String getSubclassPropertyName(int i) {
		return subclassPropertyNameClosure[i];
	}

	public int countSubclassProperties() {
		return subclassPropertyTypeClosure.length;
	}

	public String[] getSubclassPropertyColumnNames(int i) {
		return subclassPropertyColumnNameClosure[i];
	}

	public boolean isDefinedOnSubclass(int i) {
		return propertyDefinedOnSubclass[i];
	}

	@Override
	public String[][] getSubclassPropertyFormulaTemplateClosure() {
		return subclassPropertyFormulaTemplateClosure;
	}

	protected Type[] getSubclassPropertyTypeClosure() {
		return subclassPropertyTypeClosure;
	}

	protected String[][] getSubclassPropertyColumnNameClosure() {
		return subclassPropertyColumnNameClosure;
	}

	public String[][] getSubclassPropertyColumnReaderClosure() {
		return subclassPropertyColumnReaderClosure;
	}

	public String[][] getSubclassPropertyColumnReaderTemplateClosure() {
		return subclassPropertyColumnReaderTemplateClosure;
	}

	protected String[] getSubclassPropertyNameClosure() {
		return subclassPropertyNameClosure;
	}

	@Override
	public int[] resolveAttributeIndexes(String[] attributeNames) {
		if ( attributeNames == null || attributeNames.length == 0 ) {
			return new int[0];
		}
		int[] fields = new int[attributeNames.length];
		int counter = 0;

		// We sort to get rid of duplicates
		Arrays.sort( attributeNames );

		Integer index0 = entityMetamodel.getPropertyIndexOrNull( attributeNames[0] );
		if ( index0 != null ) {
			fields[counter++] = index0;
		}

		for ( int i = 0, j = 1; j < attributeNames.length; ++i, ++j ) {
			if ( !attributeNames[i].equals( attributeNames[j] ) ) {
				Integer index = entityMetamodel.getPropertyIndexOrNull( attributeNames[j] );
				if ( index != null ) {
					fields[counter++] = index;
				}
			}
		}

		return Arrays.copyOf( fields, counter );
	}

	protected String[] getSubclassPropertySubclassNameClosure() {
		return subclassPropertySubclassNameClosure;
	}

	protected String[] getSubclassColumnClosure() {
		return subclassColumnClosure;
	}

	protected String[] getSubclassColumnAliasClosure() {
		return subclassColumnAliasClosure;
	}

	public String[] getSubclassColumnReaderTemplateClosure() {
		return subclassColumnReaderTemplateClosure;
	}

	protected String[] getSubclassFormulaClosure() {
		return subclassFormulaClosure;
	}

	protected String[] getSubclassFormulaTemplateClosure() {
		return subclassFormulaTemplateClosure;
	}

	protected String[] getSubclassFormulaAliasClosure() {
		return subclassFormulaAliasClosure;
	}

	public String[] getSubclassPropertyColumnAliases(String propertyName, String suffix) {
		String[] rawAliases = (String[]) subclassPropertyAliases.get( propertyName );

		if ( rawAliases == null ) {
			return null;
		}

		String[] result = new String[rawAliases.length];
		for ( int i = 0; i < rawAliases.length; i++ ) {
			result[i] = new Alias( suffix ).toUnquotedAliasString( rawAliases[i] );
		}
		return result;
	}

	public String[] getSubclassPropertyColumnNames(String propertyName) {
		//TODO: should we allow suffixes on these ?
		return (String[]) subclassPropertyColumnNames.get( propertyName );
	}


	//This is really ugly, but necessary:

	/**
	 * Must be called by subclasses, at the end of their constructors
	 */
	protected void initSubclassPropertyAliasesMap(PersistentClass model) throws MappingException {

		// ALIASES
		internalInitSubclassPropertyAliasesMap( null, model.getSubclassPropertyClosureIterator() );

		// aliases for identifier ( alias.id ); skip if the entity defines a non-id property named 'id'
		if ( !entityMetamodel.hasNonIdentifierPropertyNamedId() ) {
			subclassPropertyAliases.put( ENTITY_ID, getIdentifierAliases() );
			subclassPropertyColumnNames.put( ENTITY_ID, getIdentifierColumnNames() );
		}

		// aliases named identifier ( alias.idname )
		if ( hasIdentifierProperty() ) {
			subclassPropertyAliases.put( getIdentifierPropertyName(), getIdentifierAliases() );
			subclassPropertyColumnNames.put( getIdentifierPropertyName(), getIdentifierColumnNames() );
		}

		// aliases for composite-id's
		if ( getIdentifierType().isComponentType() ) {
			// Fetch embedded identifiers propertynames from the "virtual" identifier component
			CompositeType componentId = (CompositeType) getIdentifierType();
			String[] idPropertyNames = componentId.getPropertyNames();
			String[] idAliases = getIdentifierAliases();
			String[] idColumnNames = getIdentifierColumnNames();

			for ( int i = 0; i < idPropertyNames.length; i++ ) {
				if ( entityMetamodel.hasNonIdentifierPropertyNamedId() ) {
					subclassPropertyAliases.put(
							ENTITY_ID + "." + idPropertyNames[i],
							new String[] {idAliases[i]}
					);
					subclassPropertyColumnNames.put(
							ENTITY_ID + "." + getIdentifierPropertyName() + "." + idPropertyNames[i],
							new String[] {idColumnNames[i]}
					);
				}
//				if (hasIdentifierProperty() && !ENTITY_ID.equals( getIdentifierPropertyName() ) ) {
				if ( hasIdentifierProperty() ) {
					subclassPropertyAliases.put(
							getIdentifierPropertyName() + "." + idPropertyNames[i],
							new String[] {idAliases[i]}
					);
					subclassPropertyColumnNames.put(
							getIdentifierPropertyName() + "." + idPropertyNames[i],
							new String[] {idColumnNames[i]}
					);
				}
				else {
					// embedded composite ids ( alias.idname1, alias.idname2 )
					subclassPropertyAliases.put( idPropertyNames[i], new String[] {idAliases[i]} );
					subclassPropertyColumnNames.put( idPropertyNames[i], new String[] {idColumnNames[i]} );
				}
			}
		}

		if ( entityMetamodel.isPolymorphic() ) {
			subclassPropertyAliases.put( ENTITY_CLASS, new String[] {getDiscriminatorAlias()} );
			subclassPropertyColumnNames.put( ENTITY_CLASS, new String[] {getDiscriminatorColumnName()} );
		}

	}

	private void internalInitSubclassPropertyAliasesMap(String path, Iterator propertyIterator) {
		while ( propertyIterator.hasNext() ) {

			Property prop = (Property) propertyIterator.next();
			String propname = path == null ? prop.getName() : path + "." + prop.getName();
			if ( prop.isComposite() ) {
				Component component = (Component) prop.getValue();
				Iterator compProps = component.getPropertyIterator();
				internalInitSubclassPropertyAliasesMap( propname, compProps );
			}
			else {
				String[] aliases = new String[prop.getColumnSpan()];
				String[] cols = new String[prop.getColumnSpan()];
				Iterator colIter = prop.getColumnIterator();
				int l = 0;
				while ( colIter.hasNext() ) {
					Selectable thing = (Selectable) colIter.next();
					aliases[l] = thing.getAlias( getFactory().getDialect(), prop.getValue().getTable() );
					cols[l] = thing.getText( getFactory().getDialect() ); // TODO: skip formulas?
					l++;
				}

				subclassPropertyAliases.put( propname, aliases );
				subclassPropertyColumnNames.put( propname, cols );
			}
		}

	}

	public Object loadByUniqueKey(
			String propertyName,
			Object uniqueKey,
			SharedSessionContractImplementor session) throws HibernateException {
		return getAppropriateUniqueKeyLoader( propertyName, session ).loadByUniqueKey( session, uniqueKey );
	}

	private EntityLoader getAppropriateUniqueKeyLoader(String propertyName, SharedSessionContractImplementor session) {
		final boolean useStaticLoader = !session.getLoadQueryInfluencers().hasEnabledFilters()
				&& !session.getLoadQueryInfluencers().hasEnabledFetchProfiles()
				&& propertyName.indexOf( '.' ) < 0; //ugly little workaround for fact that createUniqueKeyLoaders() does not handle component properties

		if ( useStaticLoader ) {
			return (EntityLoader) uniqueKeyLoaders.get( propertyName );
		}
		else {
			return createUniqueKeyLoader(
					propertyMapping.toType( propertyName ),
					propertyMapping.toColumns( propertyName ),
					session.getLoadQueryInfluencers()
			);
		}
	}

	public int getPropertyIndex(String propertyName) {
		return entityMetamodel.getPropertyIndex( propertyName );
	}

	protected void createUniqueKeyLoaders() throws MappingException {
		Type[] propertyTypes = getPropertyTypes();
		String[] propertyNames = getPropertyNames();
		for ( int i = 0; i < entityMetamodel.getPropertySpan(); i++ ) {
			if ( propertyUniqueness[i] ) {
				//don't need filters for the static loaders
				uniqueKeyLoaders.put(
						propertyNames[i],
						createUniqueKeyLoader(
								propertyTypes[i],
								getPropertyColumnNames( i ),
								LoadQueryInfluencers.NONE
						)
				);
				//TODO: create uk loaders for component properties
			}
		}
	}

	private EntityLoader createUniqueKeyLoader(
			Type uniqueKeyType,
			String[] columns,
			LoadQueryInfluencers loadQueryInfluencers) {
		if ( uniqueKeyType.isEntityType() ) {
			String className = ( (EntityType) uniqueKeyType ).getAssociatedEntityName();
			uniqueKeyType = getFactory().getMetamodel().entityPersister( className ).getIdentifierType();
		}
		return new EntityLoader(
				this,
				columns,
				uniqueKeyType,
				1,
				LockMode.NONE,
				getFactory(),
				loadQueryInfluencers
		);
	}

	protected String getSQLWhereString(String alias) {
		return StringHelper.replace( sqlWhereStringTemplate, Template.TEMPLATE, alias );
	}

	protected boolean hasWhere() {
		return sqlWhereString != null;
	}

	private void initOrdinaryPropertyPaths(Mapping mapping) throws MappingException {
		for ( int i = 0; i < getSubclassPropertyNameClosure().length; i++ ) {
			propertyMapping.initPropertyPaths(
					getSubclassPropertyNameClosure()[i],
					getSubclassPropertyTypeClosure()[i],
					getSubclassPropertyColumnNameClosure()[i],
					getSubclassPropertyColumnReaderClosure()[i],
					getSubclassPropertyColumnReaderTemplateClosure()[i],
					getSubclassPropertyFormulaTemplateClosure()[i],
					mapping
			);
		}
	}

	private void initIdentifierPropertyPaths(Mapping mapping) throws MappingException {
		String idProp = getIdentifierPropertyName();
		if ( idProp != null ) {
			propertyMapping.initPropertyPaths(
					idProp, getIdentifierType(), getIdentifierColumnNames(),
					getIdentifierColumnReaders(), getIdentifierColumnReaderTemplates(), null, mapping
			);
		}
		if ( entityMetamodel.getIdentifierProperty().isEmbedded() ) {
			propertyMapping.initPropertyPaths(
					null, getIdentifierType(), getIdentifierColumnNames(),
					getIdentifierColumnReaders(), getIdentifierColumnReaderTemplates(), null, mapping
			);
		}
		if ( !entityMetamodel.hasNonIdentifierPropertyNamedId() ) {
			propertyMapping.initPropertyPaths(
					ENTITY_ID, getIdentifierType(), getIdentifierColumnNames(),
					getIdentifierColumnReaders(), getIdentifierColumnReaderTemplates(), null, mapping
			);
		}
	}

	private void initDiscriminatorPropertyPath(Mapping mapping) throws MappingException {
		propertyMapping.initPropertyPaths(
				ENTITY_CLASS,
				getDiscriminatorType(),
				new String[] {getDiscriminatorColumnName()},
				new String[] {getDiscriminatorColumnReaders()},
				new String[] {getDiscriminatorColumnReaderTemplate()},
				new String[] {getDiscriminatorFormulaTemplate()},
				getFactory()
		);
	}

	protected void initPropertyPaths(Mapping mapping) throws MappingException {
		initOrdinaryPropertyPaths( mapping );
		initOrdinaryPropertyPaths( mapping ); //do two passes, for collection property-ref!
		initIdentifierPropertyPaths( mapping );
		if ( entityMetamodel.isPolymorphic() ) {
			initDiscriminatorPropertyPath( mapping );
		}
	}

	protected UniqueEntityLoader createEntityLoader(
			LockMode lockMode,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		//TODO: disable batch loading if lockMode > READ?
		return BatchingEntityLoaderBuilder.getBuilder( getFactory() )
				.buildLoader( this, batchSize, lockMode, getFactory(), loadQueryInfluencers );
	}

	protected UniqueEntityLoader createEntityLoader(
			LockOptions lockOptions,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		//TODO: disable batch loading if lockMode > READ?
		return BatchingEntityLoaderBuilder.getBuilder( getFactory() )
				.buildLoader( this, batchSize, lockOptions, getFactory(), loadQueryInfluencers );
	}

	/**
	 * Used internally to create static loaders.  These are the default set of loaders used to handle get()/load()
	 * processing.  lock() handling is done by the LockingStrategy instances (see {@link #getLocker})
	 *
	 * @param lockMode The lock mode to apply to the thing being loaded.
	 *
	 * @return
	 *
	 * @throws MappingException
	 */
	protected UniqueEntityLoader createEntityLoader(LockMode lockMode) throws MappingException {
		return createEntityLoader( lockMode, LoadQueryInfluencers.NONE );
	}

	protected boolean check(
			int rows,
			Serializable id,
			int tableNumber,
			Expectation expectation,
			PreparedStatement statement) throws HibernateException {
		try {
			expectation.verifyOutcome( rows, statement, -1 );
		}
		catch (StaleStateException e) {
			if ( !isNullableTable( tableNumber ) ) {
				if ( getFactory().getStatistics().isStatisticsEnabled() ) {
					getFactory().getStatisticsImplementor()
							.optimisticFailure( getEntityName() );
				}
				throw new StaleObjectStateException( getEntityName(), id );
			}
			return false;
		}
		catch (TooManyRowsAffectedException e) {
			throw new HibernateException(
					"Duplicate identifier in table for: " +
							MessageHelper.infoString( this, id, getFactory() )
			);
		}
		catch (Throwable t) {
			return false;
		}
		return true;
	}

	protected String generateUpdateString(boolean[] includeProperty, int j, boolean useRowId) {
		return generateUpdateString( includeProperty, j, null, useRowId );
	}

	/**
	 * Generate the SQL that updates a row by id (and version)
	 */
	protected String generateUpdateString(
			final boolean[] includeProperty,
			final int j,
			final Object[] oldFields,
			final boolean useRowId) {

		Update update = new Update( getFactory().getDialect() ).setTableName( getTableName( j ) );

		// select the correct row by either pk or rowid
		if ( useRowId ) {
			update.addPrimaryKeyColumns( new String[] {rowIdName} ); //TODO: eventually, rowIdName[j]
		}
		else {
			update.addPrimaryKeyColumns( getKeyColumns( j ) );
		}

		boolean hasColumns = false;
		for ( int i = 0; i < entityMetamodel.getPropertySpan(); i++ ) {
			if ( includeProperty[i] && isPropertyOfTable( i, j )
					&& !lobProperties.contains( i ) ) {
				// this is a property of the table, which we are updating
				update.addColumns(
						getPropertyColumnNames( i ),
						propertyColumnUpdateable[i], propertyColumnWriters[i]
				);
				hasColumns = hasColumns || getPropertyColumnSpan( i ) > 0;
			}
		}

		// HHH-4635
		// Oracle expects all Lob properties to be last in inserts
		// and updates.  Insert them at the end.
		for ( int i : lobProperties ) {
			if ( includeProperty[i] && isPropertyOfTable( i, j ) ) {
				// this property belongs on the table and is to be inserted
				update.addColumns(
						getPropertyColumnNames( i ),
						propertyColumnUpdateable[i], propertyColumnWriters[i]
				);
				hasColumns = true;
			}
		}

		if ( j == 0 && isVersioned() && entityMetamodel.getOptimisticLockStyle() == OptimisticLockStyle.VERSION ) {
			// this is the root (versioned) table, and we are using version-based
			// optimistic locking;  if we are not updating the version, also don't
			// check it (unless this is a "generated" version column)!
			if ( checkVersion( includeProperty ) ) {
				update.setVersionColumnName( getVersionColumnName() );
				hasColumns = true;
			}
		}
		else if ( isAllOrDirtyOptLocking() && oldFields != null ) {
			// we are using "all" or "dirty" property-based optimistic locking

			boolean[] includeInWhere = entityMetamodel.getOptimisticLockStyle() == OptimisticLockStyle.ALL
					?
					getPropertyUpdateability()
					//optimistic-lock="all", include all updatable properties
					:
					includeProperty;             //optimistic-lock="dirty", include all properties we are updating this time

			boolean[] versionability = getPropertyVersionability();
			Type[] types = getPropertyTypes();
			for ( int i = 0; i < entityMetamodel.getPropertySpan(); i++ ) {
				boolean include = includeInWhere[i] &&
						isPropertyOfTable( i, j ) &&
						versionability[i];
				if ( include ) {
					// this property belongs to the table, and it is not specifically
					// excluded from optimistic locking by optimistic-lock="false"
					String[] propertyColumnNames = getPropertyColumnNames( i );
					String[] propertyColumnWriters = getPropertyColumnWriters( i );
					boolean[] propertyNullness = types[i].toColumnNullness( oldFields[i], getFactory() );
					for ( int k = 0; k < propertyNullness.length; k++ ) {
						if ( propertyNullness[k] ) {
							update.addWhereColumn( propertyColumnNames[k], "=" + propertyColumnWriters[k] );
						}
						else {
							update.addWhereColumn( propertyColumnNames[k], " is null" );
						}
					}
				}
			}

		}

		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			update.setComment( "update " + getEntityName() );
		}

		return hasColumns ? update.toStatementString() : null;
	}

	private boolean checkVersion(final boolean[] includeProperty) {
		return includeProperty[getVersionProperty()]
				|| entityMetamodel.isVersionGenerated();
	}

	protected String generateInsertString(boolean[] includeProperty, int j) {
		return generateInsertString( false, includeProperty, j );
	}

	protected String generateInsertString(boolean identityInsert, boolean[] includeProperty) {
		return generateInsertString( identityInsert, includeProperty, 0 );
	}

	/**
	 * Generate the SQL that inserts a row
	 */
	protected String generateInsertString(boolean identityInsert, boolean[] includeProperty, int j) {

		// todo : remove the identityInsert param and variations;
		//   identity-insert strings are now generated from generateIdentityInsertString()

		Insert insert = new Insert( getFactory().getDialect() )
				.setTableName( getTableName( j ) );

		// add normal properties
		for ( int i = 0; i < entityMetamodel.getPropertySpan(); i++ ) {
			// the incoming 'includeProperty' array only accounts for insertable defined at the root level, it
			// does not account for partially generated composites etc.  We also need to account for generation
			// values
			if ( isPropertyOfTable( i, j ) ) {
				if ( !lobProperties.contains( i ) ) {
					final InDatabaseValueGenerationStrategy generationStrategy = entityMetamodel.getInDatabaseValueGenerationStrategies()[i];
					if ( generationStrategy != null && generationStrategy.getGenerationTiming().includesInsert() ) {
						if ( generationStrategy.referenceColumnsInSql() ) {
							final String[] values;
							if ( generationStrategy.getReferencedColumnValues() == null ) {
								values = propertyColumnWriters[i];
							}
							else {
								final int numberOfColumns = propertyColumnWriters[i].length;
								values = new String[numberOfColumns];
								for ( int x = 0; x < numberOfColumns; x++ ) {
									if ( generationStrategy.getReferencedColumnValues()[x] != null ) {
										values[x] = generationStrategy.getReferencedColumnValues()[x];
									}
									else {
										values[x] = propertyColumnWriters[i][x];
									}
								}
							}
							insert.addColumns( getPropertyColumnNames( i ), propertyColumnInsertable[i], values );
						}
					}
					else if ( includeProperty[i] ) {
						insert.addColumns(
								getPropertyColumnNames( i ),
								propertyColumnInsertable[i],
								propertyColumnWriters[i]
						);
					}
				}
			}
		}

		// add the discriminator
		if ( j == 0 ) {
			addDiscriminatorToInsert( insert );
		}

		// add the primary key
		if ( j == 0 && identityInsert ) {
			insert.addIdentityColumn( getKeyColumns( 0 )[0] );
		}
		else {
			insert.addColumns( getKeyColumns( j ) );
		}

		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			insert.setComment( "insert " + getEntityName() );
		}

		// HHH-4635
		// Oracle expects all Lob properties to be last in inserts
		// and updates.  Insert them at the end.
		for ( int i : lobProperties ) {
			if ( includeProperty[i] && isPropertyOfTable( i, j ) ) {
				// this property belongs on the table and is to be inserted
				insert.addColumns(
						getPropertyColumnNames( i ),
						propertyColumnInsertable[i],
						propertyColumnWriters[i]
				);
			}
		}

		String result = insert.toStatementString();

		// append the SQL to return the generated identifier
		if ( j == 0 && identityInsert && useInsertSelectIdentity() ) { //TODO: suck into Insert
			result = getFactory().getDialect().getIdentityColumnSupport().appendIdentitySelectToInsert( result );
		}

		return result;
	}

	/**
	 * Used to generate an insery statement against the root table in the
	 * case of identifier generation strategies where the insert statement
	 * executions actually generates the identifier value.
	 *
	 * @param includeProperty indices of the properties to include in the
	 * insert statement.
	 *
	 * @return The insert SQL statement string
	 */
	protected String generateIdentityInsertString(boolean[] includeProperty) {
		Insert insert = identityDelegate.prepareIdentifierGeneratingInsert();
		insert.setTableName( getTableName( 0 ) );

		// add normal properties except lobs
		for ( int i = 0; i < entityMetamodel.getPropertySpan(); i++ ) {
			if ( includeProperty[i] && isPropertyOfTable( i, 0 ) && !lobProperties.contains( i ) ) {
				// this property belongs on the table and is to be inserted
				insert.addColumns( getPropertyColumnNames( i ), propertyColumnInsertable[i], propertyColumnWriters[i] );
			}
		}

		// HHH-4635 & HHH-8103
		// Oracle expects all Lob properties to be last in inserts
		// and updates.  Insert them at the end.
		for ( int i : lobProperties ) {
			if ( includeProperty[i] && isPropertyOfTable( i, 0 ) ) {
				insert.addColumns( getPropertyColumnNames( i ), propertyColumnInsertable[i], propertyColumnWriters[i] );
			}
		}

		// add the discriminator
		addDiscriminatorToInsert( insert );

		// delegate already handles PK columns

		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			insert.setComment( "insert " + getEntityName() );
		}

		return insert.toStatementString();
	}

	/**
	 * Generate the SQL that deletes a row by id (and version)
	 */
	protected String generateDeleteString(int j) {
		final Delete delete = new Delete()
				.setTableName( getTableName( j ) )
				.addPrimaryKeyColumns( getKeyColumns( j ) );
		if ( j == 0 ) {
			delete.setVersionColumnName( getVersionColumnName() );
		}
		if ( getFactory().getSessionFactoryOptions().isCommentsEnabled() ) {
			delete.setComment( "delete " + getEntityName() );
		}
		return delete.toStatementString();
	}

	protected int dehydrate(
			Serializable id,
			Object[] fields,
			boolean[] includeProperty,
			boolean[][] includeColumns,
			int j,
			PreparedStatement st,
			SharedSessionContractImplementor session,
			boolean isUpdate) throws HibernateException, SQLException {
		return dehydrate( id, fields, null, includeProperty, includeColumns, j, st, session, 1, isUpdate );
	}

	/**
	 * Marshall the fields of a persistent instance to a prepared statement
	 */
	protected int dehydrate(
			final Serializable id,
			final Object[] fields,
			final Object rowId,
			final boolean[] includeProperty,
			final boolean[][] includeColumns,
			final int j,
			final PreparedStatement ps,
			final SharedSessionContractImplementor session,
			int index,
			boolean isUpdate) throws SQLException, HibernateException {

		if ( LOG.isTraceEnabled() ) {
			LOG.tracev( "Dehydrating entity: {0}", MessageHelper.infoString( this, id, getFactory() ) );
		}

		for ( int i = 0; i < entityMetamodel.getPropertySpan(); i++ ) {
			if ( includeProperty[i] && isPropertyOfTable( i, j )
					&& !lobProperties.contains( i ) ) {
				getPropertyTypes()[i].nullSafeSet( ps, fields[i], index, includeColumns[i], session );
				index += ArrayHelper.countTrue( includeColumns[i] ); //TODO:  this is kinda slow...
			}
		}

		if ( !isUpdate ) {
			index += dehydrateId( id, rowId, ps, session, index );
		}

		// HHH-4635
		// Oracle expects all Lob properties to be last in inserts
		// and updates.  Insert them at the end.
		for ( int i : lobProperties ) {
			if ( includeProperty[i] && isPropertyOfTable( i, j ) ) {
				getPropertyTypes()[i].nullSafeSet( ps, fields[i], index, includeColumns[i], session );
				index += ArrayHelper.countTrue( includeColumns[i] ); //TODO:  this is kinda slow...
			}
		}

		if ( isUpdate ) {
			index += dehydrateId( id, rowId, ps, session, index );
		}

		return index;

	}

	private int dehydrateId(
			final Serializable id,
			final Object rowId,
			final PreparedStatement ps,
			final SharedSessionContractImplementor session,
			int index) throws SQLException {
		if ( rowId != null ) {
			ps.setObject( index, rowId );
			return 1;
		}
		else if ( id != null ) {
			getIdentifierType().nullSafeSet( ps, id, index, session );
			return getIdentifierColumnSpan();
		}
		return 0;
	}

	/**
	 * Unmarshall the fields of a persistent instance from a result set,
	 * without resolving associations or collections. Question: should
	 * this really be here, or should it be sent back to Loader?
	 */
	public Object[] hydrate(
			final ResultSet rs,
			final Serializable id,
			final Object object,
			final Loadable rootLoadable,
			final String[][] suffixedPropertyColumns,
			final boolean allProperties,
			final SharedSessionContractImplementor session) throws SQLException, HibernateException {

		if ( LOG.isTraceEnabled() ) {
			LOG.tracev( "Hydrating entity: {0}", MessageHelper.infoString( this, id, getFactory() ) );
		}

		final AbstractEntityPersister rootPersister = (AbstractEntityPersister) rootLoadable;

		final boolean hasDeferred = rootPersister.hasSequentialSelect();
		PreparedStatement sequentialSelect = null;
		ResultSet sequentialResultSet = null;
		boolean sequentialSelectEmpty = false;
		try {

			if ( hasDeferred ) {
				final String sql = rootPersister.getSequentialSelect( getEntityName() );
				if ( sql != null ) {
					//TODO: I am not so sure about the exception handling in this bit!
					sequentialSelect = session
							.getJdbcCoordinator()
							.getStatementPreparer()
							.prepareStatement( sql );
					rootPersister.getIdentifierType().nullSafeSet( sequentialSelect, id, 1, session );
					sequentialResultSet = session.getJdbcCoordinator().getResultSetReturn().extract( sequentialSelect );
					if ( !sequentialResultSet.next() ) {
						// TODO: Deal with the "optional" attribute in the <join> mapping;
						// this code assumes that optional defaults to "true" because it
						// doesn't actually seem to work in the fetch="join" code
						//
						// Note that actual proper handling of optional-ality here is actually
						// more involved than this patch assumes.  Remember that we might have
						// multiple <join/> mappings associated with a single entity.  Really
						// a couple of things need to happen to properly handle optional here:
						//  1) First and foremost, when handling multiple <join/>s, we really
						//      should be using the entity root table as the driving table;
						//      another option here would be to choose some non-optional joined
						//      table to use as the driving table.  In all likelihood, just using
						//      the root table is much simplier
						//  2) Need to add the FK columns corresponding to each joined table
						//      to the generated select list; these would then be used when
						//      iterating the result set to determine whether all non-optional
						//      data is present
						// My initial thoughts on the best way to deal with this would be
						// to introduce a new SequentialSelect abstraction that actually gets
						// generated in the persisters (ok, SingleTable...) and utilized here.
						// It would encapsulated all this required optional-ality checking...
						sequentialSelectEmpty = true;
					}
				}
			}

			final String[] propNames = getPropertyNames();
			final Type[] types = getPropertyTypes();
			final Object[] values = new Object[types.length];
			final boolean[] laziness = getPropertyLaziness();
			final String[] propSubclassNames = getSubclassPropertySubclassNameClosure();

			for ( int i = 0; i < types.length; i++ ) {
				if ( !propertySelectable[i] ) {
					values[i] = PropertyAccessStrategyBackRefImpl.UNKNOWN;
				}
				else if ( allProperties || !laziness[i] ) {
					//decide which ResultSet to get the property value from:
					final boolean propertyIsDeferred = hasDeferred &&
							rootPersister.isSubclassPropertyDeferred( propNames[i], propSubclassNames[i] );
					if ( propertyIsDeferred && sequentialSelectEmpty ) {
						values[i] = null;
					}
					else {
						final ResultSet propertyResultSet = propertyIsDeferred ? sequentialResultSet : rs;
						final String[] cols = propertyIsDeferred ?
								propertyColumnAliases[i] :
								suffixedPropertyColumns[i];
						values[i] = types[i].hydrate( propertyResultSet, cols, session, object );
					}
				}
				else {
					values[i] = LazyPropertyInitializer.UNFETCHED_PROPERTY;
				}
			}

			if ( sequentialResultSet != null ) {
				session.getJdbcCoordinator().getResourceRegistry().release( sequentialResultSet, sequentialSelect );
			}

			return values;

		}
		finally {
			if ( sequentialSelect != null ) {
				session.getJdbcCoordinator().getResourceRegistry().release( sequentialSelect );
				session.getJdbcCoordinator().afterStatementExecution();
			}
		}
	}

	protected boolean useInsertSelectIdentity() {
		return !useGetGeneratedKeys() && getFactory().getDialect().getIdentityColumnSupport().supportsInsertSelectIdentity();
	}

	protected boolean useGetGeneratedKeys() {
		return getFactory().getSessionFactoryOptions().isGetGeneratedKeysEnabled();
	}

	protected String getSequentialSelect(String entityName) {
		throw new UnsupportedOperationException( "no sequential selects" );
	}

	/**
	 * Perform an SQL INSERT, and then retrieve a generated identifier.
	 * <p/>
	 * This form is used for PostInsertIdentifierGenerator-style ids (IDENTITY,
	 * select, etc).
	 */
	protected Serializable insert(
			final Object[] fields,
			final boolean[] notNull,
			String sql,
			final Object object,
			final SharedSessionContractImplementor session) throws HibernateException {

		if ( LOG.isTraceEnabled() ) {
			LOG.tracev( "Inserting entity: {0} (native id)", getEntityName() );
			if ( isVersioned() ) {
				LOG.tracev( "Version: {0}", Versioning.getVersion( fields, this ) );
			}
		}

		Binder binder = new Binder() {
			public void bindValues(PreparedStatement ps) throws SQLException {
				dehydrate( null, fields, notNull, propertyColumnInsertable, 0, ps, session, false );
			}

			public Object getEntity() {
				return object;
			}
		};

		return identityDelegate.performInsert( sql, session, binder );
	}


	public String[] getKeyColumnNames() {
		return getIdentifierColumnNames();
	}

	public String getName() {
		return getEntityName();
	}

	public boolean isCollection() {
		return false;
	}

	public boolean consumesEntityAlias() {
		return true;
	}

	public boolean consumesCollectionAlias() {
		return false;
	}





}
