/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;


/**
 * <tt>dbtimestamp</tt>: An extension of {@link TimestampType} which
 * maps to the database's current timestamp, rather than the jvm's
 * current timestamp.
 * <p/>
 * Note: May/may-not cause issues on dialects which do not properly support
 * a true notion of timestamp (Oracle < 8, for example, where only its DATE
 * datatype is supported).  Depends on the frequency of DML operations...
 *
 * @author Steve Ebersole
 */
public class DbTimestampType extends TimestampType {
    public static final DbTimestampType INSTANCE = new DbTimestampType();

    @Override
    public String getName() {
        return "dbtimestamp";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[]{getName()};
    }


}
