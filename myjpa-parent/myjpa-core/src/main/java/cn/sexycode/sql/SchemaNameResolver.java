/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql;


import cn.sexycode.sql.dialect.Dialect;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Contract for resolving the schema of a {@link Connection}.
 *
 * @author Steve Ebersole
 */
public interface SchemaNameResolver {
    /**
     * Given a JDBC {@link Connection}, resolve the name of the schema (if one) to which it connects.
     *
     * @param connection The JDBC connection
     * @param dialect    The Dialect
     * @return The name of the schema (may be null).
     */
    public String resolveSchemaName(Connection connection, Dialect dialect) throws SQLException;
}
