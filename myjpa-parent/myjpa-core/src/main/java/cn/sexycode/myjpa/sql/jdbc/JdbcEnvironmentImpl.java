package cn.sexycode.myjpa.sql.jdbc;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.jdbc.JdbcEnvironment;
import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.util.core.service.Service;
import cn.sexycode.util.core.service.ServiceRegistry;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Steve Ebersole
 */
public class JdbcEnvironmentImpl implements JdbcEnvironment, Service {

    private final Dialect dialect;

    //	private final SqlExceptionHelper sqlExceptionHelper;
    //	private final ExtractedDatabaseMetaData extractedMetaDataSupport;
    private final Identifier currentCatalog = null;

    private final Identifier currentSchema = null;
    //	private final IdentifierHelper identifierHelper;
    //	private final QualifiedObjectNameFormatter qualifiedObjectNameFormatter;
    //	private final LobCreatorBuilderImpl lobCreatorBuilder;

    //	private final LinkedHashSet<TypeInfo> typeInfoSet = new LinkedHashSet<TypeInfo>();
    //	private final NameQualifierSupport nameQualifierSupport;

    /**
     * Constructor form used when the JDBC {@link DatabaseMetaData} is not available.
     *
     * @param serviceRegistry The service registry
     * @param dialect         The resolved dialect.
     */
    public JdbcEnvironmentImpl(final ServiceRegistry serviceRegistry, Dialect dialect) {
        this.dialect = dialect;

		/*final ConfigurationService cfgService = serviceRegistry.getService( ConfigurationService.class );

		NameQualifierSupport nameQualifierSupport = dialect.getNameQualifierSupport();
		if ( nameQualifierSupport == null ) {
			// assume both catalogs and schemas are supported
			nameQualifierSupport = NameQualifierSupport.BOTH;
		}
		this.nameQualifierSupport = nameQualifierSupport;

		this.sqlExceptionHelper = buildSqlExceptionHelper( dialect, logWarnings( cfgService, dialect ) );

		final IdentifierHelperBuilder identifierHelperBuilder = IdentifierHelperBuilder.from( this );
		identifierHelperBuilder.setGloballyQuoteIdentifiers( globalQuoting( cfgService ) );
		identifierHelperBuilder.setSkipGlobalQuotingForColumnDefinitions( globalQuotingSkippedForColumnDefinitions(
				cfgService ) );
		identifierHelperBuilder.setAutoQuoteKeywords( autoKeywordQuoting( cfgService ) );
		identifierHelperBuilder.setNameQualifierSupport( nameQualifierSupport );

		IdentifierHelper identifierHelper = null;
		ExtractedDatabaseMetaDataImpl.Builder dbMetaDataBuilder = new ExtractedDatabaseMetaDataImpl.Builder( this );
		try {
			identifierHelper = dialect.buildIdentifierHelper( identifierHelperBuilder, null );
			dbMetaDataBuilder.setSupportsNamedParameters( dialect.supportsNamedParameters( null ) );
		}
		catch (SQLException sqle) {
			// should never ever happen
			log.debug( "There was a problem accessing DatabaseMetaData in building the JdbcEnvironment", sqle );
		}
		if ( identifierHelper == null ) {
			identifierHelper = identifierHelperBuilder.build();
		}
		this.identifierHelper = identifierHelper;

		this.extractedMetaDataSupport = dbMetaDataBuilder.build();

		this.currentCatalog = identifierHelper.toIdentifier(
				cfgService.getSetting( AvailableSettings.DEFAULT_CATALOG, StandardConverters.STRING )
		);
		this.currentSchema = Identifier.toIdentifier(
				cfgService.getSetting( AvailableSettings.DEFAULT_SCHEMA, StandardConverters.STRING )
		);

		this.qualifiedObjectNameFormatter = new QualifiedObjectNameFormatterStandardImpl( nameQualifierSupport );

		this.lobCreatorBuilder = LobCreatorBuilderImpl.makeLobCreatorBuilder();*/
    }

/*	private static boolean globalQuoting(ConfigurationService cfgService) {
		return cfgService.getSetting(
				AvailableSettings.GLOBALLY_QUOTED_IDENTIFIERS,
				StandardConverters.BOOLEAN,
				false
		);
	}

	private boolean globalQuotingSkippedForColumnDefinitions(ConfigurationService cfgService) {
		return cfgService.getSetting(
				AvailableSettings.GLOBALLY_QUOTED_IDENTIFIERS_SKIP_COLUMN_DEFINITIONS,
				StandardConverters.BOOLEAN,
				false
		);
	}

	private static boolean autoKeywordQuoting(ConfigurationService cfgService) {
		return cfgService.getSetting(
				AvailableSettings.KEYWORD_AUTO_QUOTING_ENABLED,
				StandardConverters.BOOLEAN,
				false
		);
	}*/

    /**
     * Constructor form used from testing
     *
     * @param dialect The dialect
     */
    public JdbcEnvironmentImpl(DatabaseMetaData databaseMetaData, Dialect dialect) throws SQLException {
        this.dialect = dialect;

		/*this.sqlExceptionHelper = buildSqlExceptionHelper( dialect, false );

		NameQualifierSupport nameQualifierSupport = dialect.getNameQualifierSupport();
		if ( nameQualifierSupport == null ) {
			nameQualifierSupport = determineNameQualifierSupport( databaseMetaData );
		}
		this.nameQualifierSupport = nameQualifierSupport;

		final IdentifierHelperBuilder identifierHelperBuilder = IdentifierHelperBuilder.from( this );
		identifierHelperBuilder.setNameQualifierSupport( nameQualifierSupport );
		IdentifierHelper identifierHelper = null;
		try {
			identifierHelper = dialect.buildIdentifierHelper( identifierHelperBuilder, databaseMetaData );
		}
		catch (SQLException sqle) {
			// should never ever happen
			log.debug( "There was a problem accessing DatabaseMetaData in building the JdbcEnvironment", sqle );
		}
		if ( identifierHelper == null ) {
			identifierHelper = identifierHelperBuilder.build();
		}
		this.identifierHelper = identifierHelper;

		this.extractedMetaDataSupport = new ExtractedDatabaseMetaDataImpl.Builder( this )
				.apply( databaseMetaData )
				.setSupportsNamedParameters( databaseMetaData.supportsNamedParameters() )
				.setSequenceInformationList( sequenceInformationList( databaseMetaData.getConnection() ) )
				.build();

		this.currentCatalog = null;
		this.currentSchema = null;

		this.qualifiedObjectNameFormatter = new QualifiedObjectNameFormatterStandardImpl(
				nameQualifierSupport,
				databaseMetaData
		);

		this.lobCreatorBuilder = LobCreatorBuilderImpl.makeLobCreatorBuilder();*/
    }

/*
	private NameQualifierSupport determineNameQualifierSupport(DatabaseMetaData databaseMetaData) throws SQLException {
		final boolean supportsCatalogs = databaseMetaData.supportsCatalogsInTableDefinitions();
		final boolean supportsSchemas = databaseMetaData.supportsSchemasInTableDefinitions();

		if ( supportsCatalogs && supportsSchemas ) {
			return NameQualifierSupport.BOTH;
		}
		else if ( supportsCatalogs ) {
			return NameQualifierSupport.CATALOG;
		}
		else if ( supportsSchemas ) {
			return NameQualifierSupport.SCHEMA;
		}
		else {
			return NameQualifierSupport.NONE;
		}
	}
*/

    /**
     * The main constructor form.  Builds a JdbcEnvironment using the available DatabaseMetaData
     *
     * @param serviceRegistry  The service registry
     * @param dialect          The resolved dialect
     * @param databaseMetaData The available DatabaseMetaData
     * @throws SQLException
     */
    public JdbcEnvironmentImpl(ServiceRegistry serviceRegistry, Dialect dialect, DatabaseMetaData databaseMetaData)
            throws SQLException {
        this.dialect = dialect;

		/*final ConfigurationService cfgService = serviceRegistry.getService( ConfigurationService.class );

		this.sqlExceptionHelper = buildSqlExceptionHelper( dialect, logWarnings( cfgService, dialect ) );

		NameQualifierSupport nameQualifierSupport = dialect.getNameQualifierSupport();
		if ( nameQualifierSupport == null ) {
			nameQualifierSupport = determineNameQualifierSupport( databaseMetaData );
		}
		this.nameQualifierSupport = nameQualifierSupport;

		final IdentifierHelperBuilder identifierHelperBuilder = IdentifierHelperBuilder.from( this );
		identifierHelperBuilder.setGloballyQuoteIdentifiers( globalQuoting( cfgService ) );
		identifierHelperBuilder.setSkipGlobalQuotingForColumnDefinitions( globalQuotingSkippedForColumnDefinitions(
				cfgService ) );
		identifierHelperBuilder.setAutoQuoteKeywords( autoKeywordQuoting( cfgService ) );
		identifierHelperBuilder.setNameQualifierSupport( nameQualifierSupport );
		IdentifierHelper identifierHelper = null;
		try {
			identifierHelper = dialect.buildIdentifierHelper( identifierHelperBuilder, databaseMetaData );
		}
		catch (SQLException sqle) {
			// should never ever happen
			log.debug( "There was a problem accessing DatabaseMetaData in building the JdbcEnvironment", sqle );
		}
		if ( identifierHelper == null ) {
			identifierHelper = identifierHelperBuilder.build();
		}
		this.identifierHelper = identifierHelper;

		this.extractedMetaDataSupport = new ExtractedDatabaseMetaDataImpl.Builder( this )
				.apply( databaseMetaData )
				.setConnectionSchemaName( determineCurrentSchemaName( databaseMetaData, serviceRegistry, dialect ) )
				.setSupportsNamedParameters( dialect.supportsNamedParameters( databaseMetaData ) )
				.setSequenceInformationList( sequenceInformationList( databaseMetaData.getConnection() ) )
				.build();

		// and that current-catalog and current-schema happen after it
		this.currentCatalog = identifierHelper.toIdentifier( extractedMetaDataSupport.getConnectionCatalogName() );
		this.currentSchema = identifierHelper.toIdentifier( extractedMetaDataSupport.getConnectionSchemaName() );

		this.qualifiedObjectNameFormatter = new QualifiedObjectNameFormatterStandardImpl(
				nameQualifierSupport,
				databaseMetaData
		);

		this.typeInfoSet.addAll( TypeInfo.extractTypeInfo( databaseMetaData ) );

		this.lobCreatorBuilder = LobCreatorBuilderImpl.makeLobCreatorBuilder(
				dialect,
				cfgService.getSettings(),
				databaseMetaData.getConnection()
		);*/
    }

    public static final String SCHEMA_NAME_RESOLVER = "hibernate.schema_name_resolver";

/*
	private String determineCurrentSchemaName(
			DatabaseMetaData databaseMetaData,
			ServiceRegistry serviceRegistry,
			Dialect dialect) throws SQLException {
		final SchemaNameResolver schemaNameResolver;

		final Object setting = serviceRegistry.getService( ConfigurationService.class ).getSettings().get(
				SCHEMA_NAME_RESOLVER );
		if ( setting == null ) {
			schemaNameResolver = dialect.getSchemaNameResolver();
		}
		else {
			schemaNameResolver = serviceRegistry.getService( StrategySelector.class ).resolveDefaultableStrategy(
					SchemaNameResolver.class,
					setting,
					dialect.getSchemaNameResolver()
			);
		}

		try {
			return schemaNameResolver.resolveSchemaName( databaseMetaData.getConnection(), dialect );
		}
		catch (Exception e) {
			log.debug( "Unable to resolve connection default schema", e );
			return null;
		}
	}
*/

    @SuppressWarnings("deprecation")
/*	private SqlExceptionHelper buildSqlExceptionHelper(Dialect dialect, boolean logWarnings) {
		final StandardSQLExceptionConverter sqlExceptionConverter = new StandardSQLExceptionConverter();
		sqlExceptionConverter.addDelegate( dialect.buildSQLExceptionConversionDelegate() );
		sqlExceptionConverter.addDelegate( new SQLExceptionTypeDelegate( dialect ) );
		// todo : vary this based on extractedMetaDataSupport.getSqlStateType()
		sqlExceptionConverter.addDelegate( new SQLStateConversionDelegate( dialect ) );
		return new SqlExceptionHelper( sqlExceptionConverter, logWarnings );
	}

	private Set<String> buildMergedReservedWords(Dialect dialect, DatabaseMetaData dbmd) throws SQLException {
		Set<String> reservedWords = new HashSet<String>();
		reservedWords.addAll( dialect.getKeywords() );
		// todo : do we need to explicitly handle SQL:2003 keywords?
		reservedWords.addAll( Arrays.asList( dbmd.getSQLKeywords().split( "," ) ) );
		return reservedWords;
	}*/

    @Override
    public Dialect getDialect() {
        return dialect;
    }


/*

	@Override
	public ExtractedDatabaseMetaData getExtractedDatabaseMetaData() {
		return extractedMetaDataSupport;
	}
*/

    @Override
    public Identifier getCurrentCatalog() {
        return currentCatalog;
    }

    @Override
    public Identifier getCurrentSchema() {
        return currentSchema;
    }

    /**
     * @return
     */
    @Override
    public Properties getProperties() {
        return null;
    }

/*
	@Override
	public QualifiedObjectNameFormatter getQualifiedObjectNameFormatter() {
		return qualifiedObjectNameFormatter;
	}

	@Override
	public IdentifierHelper getIdentifierHelper() {
		return identifierHelper;
	}

	@Override
	public NameQualifierSupport getNameQualifierSupport() {
		return nameQualifierSupport;
	}

	@Override
	public SqlExceptionHelper getSqlExceptionHelper() {
		return sqlExceptionHelper;
	}

	@Override
	public LobCreatorBuilder getLobCreatorBuilder() {
		return lobCreatorBuilder;
	}

	@Override
	public TypeInfo getTypeInfoForJdbcCode(int jdbcTypeCode) {
		for ( TypeInfo typeInfo : typeInfoSet ) {
			if ( typeInfo.getJdbcTypeCode() == jdbcTypeCode ) {
				return typeInfo;
			}
		}
		return null;
	}
*/

    /**
     * Get the sequence information List from the database.
     *
     * @param connection database connection
     * @return sequence information List
     */
	/*private List<SequenceInformation> sequenceInformationList(final Connection connection) {
		try {

			Iterable<SequenceInformation> sequenceInformationIterable = dialect
				.getSequenceInformationExtractor()
				.extractMetadata( new ExtractionContext.EmptyExtractionContext() {
					@Override
					public Connection getJdbcConnection() {
						return connection;
					}

					@Override
					public JdbcEnvironment getJdbcEnvironment() {
						return JdbcEnvironmentImpl.this;
					}
				}
			);

			return StreamSupport.stream( sequenceInformationIterable.spliterator(), false )
					.collect( Collectors.toList() );
		}
		catch (SQLException e) {
			log.error( "Could not fetch the SequenceInformation from the database", e );
		}

		return Collections.emptyList();
	}*/
}
