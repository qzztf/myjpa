package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.mapping.Table;

/**
 * Models the commonality between a column and a formula (computed value).
 */
public interface Selectable {
    public String getAlias(Dialect dialect);

    public String getAlias(Dialect dialect, Table table);

    public boolean isFormula();

    public String getText(Dialect dialect);

    public String getText();
}
