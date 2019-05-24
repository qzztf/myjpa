/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql;

import cn.sexycode.mybatis.jpa.mapping.Identifier;
import cn.sexycode.sql.dialect.Dialect;

/**
 * Initial look at this concept we keep talking about with merging information from {@link java.sql.DatabaseMetaData}
 * and {@link org.hibernate.dialect.Dialect}
 *
 * @author Steve Ebersole
 */
public interface JdbcEnvironment {
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
    ExtractedDatabaseMetaData getExtractedDatabaseMetaData();

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
     * Obtain the helper for dealing with JDBC {@link java.sql.SQLException} faults.
     *
     * @return This environment's helper.
     */
    SqlExceptionHelper getSqlExceptionHelper();


}
