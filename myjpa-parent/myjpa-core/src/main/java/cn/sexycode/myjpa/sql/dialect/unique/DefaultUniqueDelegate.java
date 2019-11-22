package cn.sexycode.myjpa.sql.dialect.unique;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.dialect.unique.UniqueDelegate;
import cn.sexycode.myjpa.sql.mapping.Column;
import cn.sexycode.myjpa.sql.mapping.Table;
import cn.sexycode.myjpa.sql.mapping.UniqueKey;

import java.util.Iterator;

/**
 * The default UniqueDelegate implementation for most dialects.  Uses
 * separate create/alter statements to apply uniqueness to a column.
 *
 */
public class DefaultUniqueDelegate implements UniqueDelegate {
    protected final Dialect dialect;

    /**
     * Constructs DefaultUniqueDelegate
     *
     * @param dialect The dialect for which we are handling unique constraints
     */
    public DefaultUniqueDelegate(Dialect dialect) {
        this.dialect = dialect;
    }

    // legacy model ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String getColumnDefinitionUniquenessFragment(Column column) {
        return "";
    }

    @Override
    public String getTableCreationUniqueConstraintsFragment(Table table) {
        return "";
    }


    protected String uniqueConstraintSql(UniqueKey uniqueKey) {
        final StringBuilder sb = new StringBuilder();
        sb.append("unique (");
        final Iterator<Column> columnIterator = uniqueKey.columnIterator();
        while (columnIterator.hasNext()) {
            final Column column = columnIterator.next();
            sb.append(column.getQuotedName(dialect));
            if (uniqueKey.getColumnOrderMap().containsKey(column)) {
                sb.append(" ").append(uniqueKey.getColumnOrderMap().get(column));
            }
            if (columnIterator.hasNext()) {
                sb.append(", ");
            }
        }

        return sb.append(')').toString();
    }


    protected String getDropUnique() {
        return " drop constraint ";
    }

}
