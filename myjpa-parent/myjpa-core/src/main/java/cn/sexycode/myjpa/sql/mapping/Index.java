package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.mapping.Column;
import cn.sexycode.myjpa.sql.mapping.Exportable;
import cn.sexycode.myjpa.sql.mapping.Table;
import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.myjpa.sql.type.Mapping;
import cn.sexycode.util.core.str.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A relational table index
 *
 */
public class Index implements Exportable, Serializable {
	private Table table;
	private java.util.List<Column> columns = new ArrayList<Column>();
	private java.util.Map<Column, String> columnOrderMap = new HashMap<Column, String>(  );
	private Identifier name;

	public String sqlCreateString(Dialect dialect, Mapping mapping, String defaultCatalog, String defaultSchema) {
		return buildSqlCreateIndexString(
				dialect,
				getQuotedName( dialect ),
				getTable(),
				getColumnIterator(),
				columnOrderMap,
				false,
				defaultCatalog,
				defaultSchema
		);
	}

	public static String buildSqlDropIndexString(
			Dialect dialect,
			Table table,
			String name,
			String defaultCatalog,
			String defaultSchema) {
		return buildSqlDropIndexString( name, table.getQualifiedName( dialect, defaultCatalog, defaultSchema ) );
	}

	public static String buildSqlDropIndexString(
			String name,
			String tableName) {
		return "drop index " + StringUtils.qualify( tableName, name );
	}

	public static String buildSqlCreateIndexString(
			Dialect dialect,
			String name,
			Table table,
			Iterator<Column> columns,
			java.util.Map<Column, String> columnOrderMap,
			boolean unique,
			String defaultCatalog,
			String defaultSchema) {
		return buildSqlCreateIndexString(
				dialect,
				name,
				table.getQualifiedName( dialect, defaultCatalog, defaultSchema ),
				columns,
				columnOrderMap,
				unique
		);
	}

	public static String buildSqlCreateIndexString(
			Dialect dialect,
			String name,
			String tableName,
			Iterator<Column> columns,
			java.util.Map<Column, String> columnOrderMap,
			boolean unique) {
		StringBuilder buf = new StringBuilder( "create" )
				.append( unique ? " unique" : "" )
				.append( " index " )
				.append( dialect.qualifyIndexName() ? name : StringUtils.unqualify( name ) )
				.append( " on " )
				.append( tableName )
				.append( " (" );
		while ( columns.hasNext() ) {
			Column column = columns.next();
			buf.append( column.getQuotedName( dialect ) );
			if ( columnOrderMap.containsKey( column ) ) {
				buf.append( " " ).append( columnOrderMap.get( column ) );
			}
			if ( columns.hasNext() ) {
				buf.append( ", " );
			}
		}
		buf.append( ")" );
		return buf.toString();
	}

	public static String buildSqlCreateIndexString(
			Dialect dialect,
			String name,
			Table table,
			Iterator<Column> columns,
			boolean unique,
			String defaultCatalog,
			String defaultSchema) {
		return buildSqlCreateIndexString(
				dialect,
				name,
				table,
				columns,
				Collections.EMPTY_MAP,
				unique,
				defaultCatalog,
				defaultSchema
		);
	}



	// Used only in Table for sqlCreateString (but commented out at the moment)
	public String sqlConstraintString(Dialect dialect) {
		StringBuilder buf = new StringBuilder( " index (" );
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			buf.append( ( (Column) iter.next() ).getQuotedName( dialect ) );
			if ( iter.hasNext() ) {
				buf.append( ", " );
			}
		}
		return buf.append( ')' ).toString();
	}


	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public int getColumnSpan() {
		return columns.size();
	}

	public Iterator<Column> getColumnIterator() {
		return columns.iterator();
	}

	public java.util.Map<Column, String> getColumnOrderMap() {
		return Collections.unmodifiableMap( columnOrderMap );
	}

	public void addColumn(Column column) {
		if ( !columns.contains( column ) ) {
			columns.add( column );
		}
	}

	public void addColumn(Column column, String order) {
		addColumn( column );
		if ( StringUtils.isNotEmpty( order ) ) {
			columnOrderMap.put( column, order );
		}
	}

	public void addColumns(Iterator extraColumns) {
		while ( extraColumns.hasNext() ) {
			addColumn( (Column) extraColumns.next() );
		}
	}

	public boolean containsColumn(Column column) {
		return columns.contains( column );
	}

	public String getName() {
		return name == null ? null : name.getText();
	}

	public void setName(String name) {
		this.name = Identifier.toIdentifier( name );
	}

	public String getQuotedName(Dialect dialect) {
		return name == null ? null : name.render( dialect );
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + getName() + ")";
	}

	@Override
	public String getExportIdentifier() {
		return StringUtils.qualify( getTable().getName(), "IDX-" + getName() );
	}
}
