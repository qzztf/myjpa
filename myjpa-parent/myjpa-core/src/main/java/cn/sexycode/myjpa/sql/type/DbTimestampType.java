package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.TimestampType;

/**
 * <tt>dbtimestamp</tt>: An extension of {@link cn.sexycode.myjpa.sql.type.TimestampType} which
 * maps to the database's current timestamp, rather than the jvm's
 * current timestamp.
 * <p/>
 * Note: May/may-not cause issues on dialects which do not properly support
 * a true notion of timestamp (Oracle < 8, for example, where only its DATE
 * datatype is supported).  Depends on the frequency of DML operations...
 *
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
