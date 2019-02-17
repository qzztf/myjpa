/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.dialect;


import cn.sexycode.sql.JDBCException;

import java.sql.SQLException;

/**
 * A helper to centralize conversion of {@link SQLException}s to {@link org.hibernate.JDBCException}s.
 * <p/>
 * Used while querying JDBC metadata during bootstrapping
 *
 * @author Steve Ebersole
 */
public class BasicSQLExceptionConverter {

    /**
     * Singleton access
     */
    public static final BasicSQLExceptionConverter INSTANCE = new BasicSQLExceptionConverter();

    /**
     * Message
     */


    /**
     * Perform a conversion.
     *
     * @param sqlException The exception to convert.
     * @return The converted exception.
     */
    public JDBCException convert(SQLException sqlException) {
        return CONVERTER.convert(sqlException, MSG, null);
    }


}
