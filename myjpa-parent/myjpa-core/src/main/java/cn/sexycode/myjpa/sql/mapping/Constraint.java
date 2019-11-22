package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.mapping.Column;
import cn.sexycode.myjpa.sql.mapping.MappingException;
import cn.sexycode.myjpa.sql.mapping.Selectable;
import cn.sexycode.myjpa.sql.mapping.Table;
import cn.sexycode.myjpa.sql.type.Mapping;
import cn.sexycode.util.core.str.StringUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * A relational constraint.
 *
 */
public abstract class Constraint implements Serializable {

    private String name;
    private final ArrayList<cn.sexycode.myjpa.sql.mapping.Column> columns = new ArrayList<cn.sexycode.myjpa.sql.mapping.Column>();
    private cn.sexycode.myjpa.sql.mapping.Table table;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * If a constraint is not explicitly named, this is called to generate
     * a unique hash using the table and column names.
     * Static so the name can be generated prior to creating the Constraint.
     * They're cached, keyed by name, in multiple locations.
     *
     * @return String The generated name
     */
    public static String generateName(String prefix, cn.sexycode.myjpa.sql.mapping.Table table, cn.sexycode.myjpa.sql.mapping.Column... columns) {
        // Use a concatenation that guarantees uniqueness, even if identical names
        // exist between all table and column identifiers.

        StringBuilder sb = new StringBuilder("table`" + table.getName() + "`");

        // Ensure a consistent ordering of columns, regardless of the order
        // they were bound.
        // Clone the list, as sometimes a set of order-dependent Column
        // bindings are given.
        cn.sexycode.myjpa.sql.mapping.Column[] alphabeticalColumns = columns.clone();
        Arrays.sort(alphabeticalColumns, ColumnComparator.INSTANCE);
        for (cn.sexycode.myjpa.sql.mapping.Column column : alphabeticalColumns) {
            String columnName = column == null ? "" : column.getName();
            sb.append("column`").append(columnName).append("`");
        }
        return prefix + hashedName(sb.toString());
    }

    /**
     * Helper method for {@link #generateName(String, cn.sexycode.myjpa.sql.mapping.Table, cn.sexycode.myjpa.sql.mapping.Column...)}.
     *
     * @return String The generated name
     */
    public static String generateName(String prefix, cn.sexycode.myjpa.sql.mapping.Table table, List<cn.sexycode.myjpa.sql.mapping.Column> columns) {
        return generateName(prefix, table, columns.toArray(new cn.sexycode.myjpa.sql.mapping.Column[columns.size()]));
    }

    /**
     * Hash a constraint name using MD5. Convert the MD5 digest to base 35
     * (full alphanumeric), guaranteeing
     * that the length of the name will always be smaller than the 30
     * character identifier restriction enforced by a few dialects.
     *
     * @param s The name to be hashed.
     * @return String The hased name.
     */
    public static String hashedName(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(s.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            // By converting to base 35 (full alphanumeric), we guarantee
            // that the length of the name will always be smaller than the 30
            // character identifier restriction enforced by a few dialects.
            return bigInt.toString(35);
        } catch (NoSuchAlgorithmException e) {
            throw new MappingException("Unable to generate a hashed Constraint name!", e);
        }
    }

    private static class ColumnComparator implements Comparator<cn.sexycode.myjpa.sql.mapping.Column> {
        public static ColumnComparator INSTANCE = new ColumnComparator();

        @Override
        public int compare(cn.sexycode.myjpa.sql.mapping.Column col1, cn.sexycode.myjpa.sql.mapping.Column col2) {
            return col1.getName().compareTo(col2.getName());
        }
    }

    public void addColumn(cn.sexycode.myjpa.sql.mapping.Column column) {
        if (!columns.contains(column)) {
            columns.add(column);
        }
    }

    public void addColumns(Iterator columnIterator) {
        while (columnIterator.hasNext()) {
            Selectable col = (Selectable) columnIterator.next();
            if (!col.isFormula()) {
                addColumn((cn.sexycode.myjpa.sql.mapping.Column) col);
            }
        }
    }

    /**
     * @return true if this constraint already contains a column with same name.
     */
    public boolean containsColumn(cn.sexycode.myjpa.sql.mapping.Column column) {
        return columns.contains(column);
    }

    public int getColumnSpan() {
        return columns.size();
    }

    public cn.sexycode.myjpa.sql.mapping.Column getColumn(int i) {
        return columns.get(i);
    }

    //todo duplicated method, remove one
    public Iterator<cn.sexycode.myjpa.sql.mapping.Column> getColumnIterator() {
        return columns.iterator();
    }

    public Iterator<cn.sexycode.myjpa.sql.mapping.Column> columnIterator() {
        return columns.iterator();
    }

    public cn.sexycode.myjpa.sql.mapping.Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public boolean isGenerated(Dialect dialect) {
        return true;
    }

    public String sqlDropString(Dialect dialect, String defaultCatalog, String defaultSchema) {
        if (isGenerated(dialect)) {
            return String.format(
                    Locale.ROOT,
                    "alter table %s drop constraint %s",
                    getTable().getQualifiedName(dialect, defaultCatalog, defaultSchema),
                    dialect.quote(getName())
            );
        } else {
            return null;
        }
    }

    public String sqlCreateString(Dialect dialect, Mapping p, String defaultCatalog, String defaultSchema) {
        if (isGenerated(dialect)) {
            // Certain dialects (ex: HANA) don't support FKs as expected, but other constraints can still be created.
            // If that's the case, hasAlterTable() will be true, but getAddForeignKeyConstraintString will return
            // empty string.  Prevent blank "alter table" statements.
            String constraintString = sqlConstraintString(dialect, getName(), defaultCatalog, defaultSchema);
            if (!StringUtils.isEmpty(constraintString)) {
                return "alter table " + getTable().getQualifiedName(dialect, defaultCatalog, defaultSchema)
                        + constraintString;
            }
        }
        return null;
    }

    public List<Column> getColumns() {
        return columns;
    }

    /**
     * @param d
     * @param constraintName
     * @param defaultCatalog
     * @param defaultSchema
     * @return
     */
    public abstract String sqlConstraintString(
            Dialect d,
            String constraintName,
            String defaultCatalog,
            String defaultSchema);

    @Override
    public String toString() {
        return getClass().getName() + '(' + getTable().getName() + getColumns() + ") as " + name;
    }

    /**
     * @return String The prefix to use in generated constraint names.  Examples:
     * "UK_", "FK_", and "PK_".
     */
    public abstract String generatedConstraintNamePrefix();
}
