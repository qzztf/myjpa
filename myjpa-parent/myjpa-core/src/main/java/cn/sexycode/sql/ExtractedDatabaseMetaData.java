/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql;

import java.util.Set;

/**
 * Information extracted from {@link java.sql.DatabaseMetaData} regarding what the JDBC driver reports as
 * being supported or not.  Obviously {@link java.sql.DatabaseMetaData} reports many things, these are a few in
 * which we have particular interest.
 *
 * @author Steve Ebersole
 */
@SuppressWarnings({"UnusedDeclaration"})
public interface ExtractedDatabaseMetaData {
    /**
     * Obtain the JDBC Environment from which this metadata came.
     *
     * @return The JDBC environment
     */
    JdbcEnvironment getJdbcEnvironment();

    /**
     * Retrieve the name of the catalog in effect when we connected to the database.
     *
     * @return The catalog name
     */
    String getConnectionCatalogName();

    /**
     * Retrieve the name of the schema in effect when we connected to the database.
     *
     * @return The schema name
     */
    String getConnectionSchemaName();


    /**
     * Get the list of extra keywords (beyond standard SQL92 keywords) reported by the driver.
     *
     * @return The extra keywords used by this database.
     * @see java.sql.DatabaseMetaData#getSQLKeywords()
     */
    Set<String> getExtraKeywords();

    /**
     * Does the driver report supporting named parameters?
     *
     * @return {@code true} indicates the driver reported true; {@code false} indicates the driver reported false
     * or that the driver could not be asked.
     */
    boolean supportsNamedParameters();

    /**
     * Does the driver report supporting REF_CURSORs?
     *
     * @return {@code true} indicates the driver reported true; {@code false} indicates the driver reported false
     * or that the driver could not be asked.
     */
    boolean supportsRefCursors();

    /**
     * Did the driver report to supporting scrollable result sets?
     *
     * @return True if the driver reported to support {@link java.sql.ResultSet#TYPE_SCROLL_INSENSITIVE}.
     * @see java.sql.DatabaseMetaData#supportsResultSetType
     */
    boolean supportsScrollableResults();

    /**
     * Did the driver report to supporting retrieval of generated keys?
     *
     * @return True if the if the driver reported to support calls to {@link java.sql.Statement#getGeneratedKeys}
     * @see java.sql.DatabaseMetaData#supportsGetGeneratedKeys
     */
    boolean supportsGetGeneratedKeys();

    /**
     * Did the driver report to supporting batched updates?
     *
     * @return True if the driver supports batched updates
     * @see java.sql.DatabaseMetaData#supportsBatchUpdates
     */
    boolean supportsBatchUpdates();

    /**
     * Did the driver report to support performing DDL within transactions?
     *
     * @return True if the drivers supports DDL statements within transactions.
     * @see java.sql.DatabaseMetaData#dataDefinitionIgnoredInTransactions
     */
    boolean supportsDataDefinitionInTransaction();

    /**
     * Did the driver report to DDL statements performed within a transaction performing an implicit commit of the
     * transaction.
     *
     * @return True if the driver/database performs an implicit commit of transaction when DDL statement is
     * performed
     * @see java.sql.DatabaseMetaData#dataDefinitionCausesTransactionCommit()
     */
    boolean doesDataDefinitionCauseTransactionCommit();


    /**
     * Did the driver report that updates to a LOB locator affect a copy of the LOB?
     *
     * @return True if updates to the state of a LOB locator update only a copy.
     * @see java.sql.DatabaseMetaData#locatorsUpdateCopy()
     */
    boolean doesLobLocatorUpdateCopy();
}
