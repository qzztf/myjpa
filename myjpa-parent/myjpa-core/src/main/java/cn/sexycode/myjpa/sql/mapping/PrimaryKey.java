package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.mapping.Column;
import cn.sexycode.myjpa.sql.mapping.Constraint;
import cn.sexycode.myjpa.sql.mapping.Table;
import cn.sexycode.util.core.str.StringUtils;

import java.util.Iterator;

/**
 * A primary key constraint
 *
 */
public class PrimaryKey extends Constraint {

    public PrimaryKey(Table table) {
        setTable(table);
    }

    @Override
    public void addColumn(cn.sexycode.myjpa.sql.mapping.Column column) {
        final Iterator<cn.sexycode.myjpa.sql.mapping.Column> columnIterator = getTable().getColumnIterator();
        while (columnIterator.hasNext()) {
            final cn.sexycode.myjpa.sql.mapping.Column next = columnIterator.next();
            if (next.getCanonicalName().equals(column.getCanonicalName())) {
                next.setNullable(false);
                /*log.debug(
                        "Forcing column [%s] to be non-null as it is part of the primary key for table [%s]",
                        column.getCanonicalName(),
                        getTableNameForLogging(column)
                );*/
            }
        }
        super.addColumn(column);
    }

    protected String getTableNameForLogging(cn.sexycode.myjpa.sql.mapping.Column column) {
        if (getTable() != null) {
            if (getTable().getNameIdentifier() != null) {
                return getTable().getNameIdentifier().getCanonicalName();
            } else {
                return "<unknown>";
            }
        } else if (column.getValue() != null && column.getValue().getTable() != null) {
            return column.getValue().getTable().getNameIdentifier().getCanonicalName();
        }
        return "<unknown>";
    }

    public String sqlConstraintString(Dialect dialect) {
        StringBuilder buf = new StringBuilder("primary key (");
        Iterator iter = getColumnIterator();
        while (iter.hasNext()) {
            buf.append(((cn.sexycode.myjpa.sql.mapping.Column) iter.next()).getQuotedName(dialect));
            if (iter.hasNext()) {
                buf.append(", ");
            }
        }
        return buf.append(')').toString();
    }

    @Override
    public String sqlConstraintString(Dialect dialect, String constraintName, String defaultCatalog,
            String defaultSchema) {
        StringBuilder buf = new StringBuilder(dialect.getAddPrimaryKeyConstraintString(constraintName)).append('(');
        Iterator iter = getColumnIterator();
        while (iter.hasNext()) {
            buf.append(((Column) iter.next()).getQuotedName(dialect));
            if (iter.hasNext()) {
                buf.append(", ");
            }
        }
        return buf.append(')').toString();
    }

    @Override
    public String generatedConstraintNamePrefix() {
        return "PK_";
    }

    public String getExportIdentifier() {
        return StringUtils.qualify(getTable().getName(), "PK-" + getName());
    }
}
