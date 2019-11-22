package cn.sexycode.myjpa.sql.dialect.unique;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.dialect.unique.DefaultUniqueDelegate;

/**
 */
public class MySQLUniqueDelegate extends DefaultUniqueDelegate {

    /**
     * Constructs MySQLUniqueDelegate
     *
     * @param dialect The dialect for which we are handling unique constraints
     */
    public MySQLUniqueDelegate(Dialect dialect) {
        super(dialect);
    }

    @Override
    protected String getDropUnique() {
        return " drop index ";
    }
}
