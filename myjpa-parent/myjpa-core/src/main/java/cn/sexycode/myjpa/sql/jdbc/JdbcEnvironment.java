package cn.sexycode.myjpa.sql.jdbc;

import cn.sexycode.myjpa.sql.Environment;
import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.model.Identifier;

/**
 * Initial look at this concept we keep talking about with merging information from {@link java.sql.DatabaseMetaData}
 * and {@link Dialect}
 *
 * @author Steve Ebersole
 */
public interface JdbcEnvironment extends Environment {
    /**
     * Get the dialect for this environment.
     *
     * @return The dialect.
     */
    Dialect getDialect();

    /**
     * Access to the bits of information we pulled off the JDBC {@link java.sql.DatabaseMetaData} (that did not get
     * "interpreted" into the helpers/delegates available here).
     *
     * @return The values extracted from JDBC DatabaseMetaData
     */
    //	ExtractedDatabaseMetaData getExtractedDatabaseMetaData();

    /**
     * Get the current database catalog.  Typically will come from either {@link java.sql.Connection#getCatalog()}
     * or {@link org.hibernate.cfg.AvailableSettings#DEFAULT_CATALOG}.
     *
     * @return The current catalog.
     */
    Identifier getCurrentCatalog();

    /**
     * Get the current database catalog.  Typically will come from either
     * {@link SchemaNameResolver#resolveSchemaName(java.sql.Connection, org.hibernate.dialect.Dialect)} or
     * {@link org.hibernate.cfg.AvailableSettings#DEFAULT_CATALOG}.
     *
     * @return The current schema
     */
    Identifier getCurrentSchema();

    /**
     * Obtain support for formatting qualified object names.
     *
     * @return Qualified name support.
     */
    //	QualifiedObjectNameFormatter getQualifiedObjectNameFormatter();

    /**
     * Obtain the helper for dealing with identifiers in this environment.
     * <p/>
     * Note that the Identifiers returned from this IdentifierHelper already account for
     * auto-quoting :) yaay!
     *
     * @return The identifier helper.
     */
    //	IdentifierHelper getIdentifierHelper();

    /**
     * Obtain the level of support for qualified names.
     *
     * @return
     */
    //	NameQualifierSupport getNameQualifierSupport();

    /**
     * Obtain the helper for dealing with JDBC {@link java.sql.SQLException} faults.
     *
     * @return This environment's helper.
     */
    //	SqlExceptionHelper getSqlExceptionHelper();

    /**
     * Retrieve the delegate for building {@link org.hibernate.engine.jdbc.LobCreator} instances.
     *
     * @return The LobCreator builder.
     */
    //	LobCreatorBuilder getLobCreatorBuilder();

    /**
     * Find type information for the type identified by the given "JDBC type code".
     *
     * @param jdbcTypeCode The JDBC type code.
     *
     * @return The corresponding type info.
     */
    //	TypeInfo getTypeInfoForJdbcCode(int jdbcTypeCode);
}
