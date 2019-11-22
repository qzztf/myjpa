package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.mapping.Column;
import cn.sexycode.myjpa.sql.mapping.Constraint;
import cn.sexycode.myjpa.sql.type.Mapping;
import cn.sexycode.util.core.str.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * A relational unique key constraint
 *
 */
public class UniqueKey extends Constraint {
    private Map<cn.sexycode.myjpa.sql.mapping.Column, String> columnOrderMap = new HashMap<cn.sexycode.myjpa.sql.mapping.Column, String>();

    @Override
    public String sqlConstraintString(Dialect dialect, String constraintName, String defaultCatalog,
            String defaultSchema) {
        //		return dialect.getUniqueDelegate().uniqueConstraintSql( this );
        // Not used.
        return "";
    }

    @Override
    public String sqlCreateString(Dialect dialect, Mapping p, String defaultCatalog, String defaultSchema) {
        return null;
        //		return dialect.getUniqueDelegate().getAlterTableToAddUniqueKeyCommand(
        //				this, defaultCatalog, defaultSchema
        //		);
    }

    @Override
    public String sqlDropString(Dialect dialect, String defaultCatalog, String defaultSchema) {
        return null;
        //		return dialect.getUniqueDelegate().getAlterTableToDropUniqueKeyCommand(
        //				this, defaultCatalog, defaultSchema
        //		);
    }

    public void addColumn(cn.sexycode.myjpa.sql.mapping.Column column, String order) {
        addColumn(column);
        if (StringUtils.isNotEmpty(order)) {
            columnOrderMap.put(column, order);
        }
    }

    public Map<Column, String> getColumnOrderMap() {
        return columnOrderMap;
    }

    @Override
    public String generatedConstraintNamePrefix() {
        return "UK_";
    }

}
