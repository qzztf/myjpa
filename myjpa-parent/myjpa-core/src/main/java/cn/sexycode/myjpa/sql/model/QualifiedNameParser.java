package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.MysqlException;
import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.myjpa.sql.model.IllegalIdentifierException;
import cn.sexycode.myjpa.sql.model.QualifiedName;

import java.util.Objects;

/**
 * Parses a qualified name.
 *
 */
public class QualifiedNameParser {
	/**
	 * Singleton access
	 */
	public static final QualifiedNameParser INSTANCE = new QualifiedNameParser();

	public static class NameParts implements QualifiedName {
		private final cn.sexycode.myjpa.sql.model.Identifier catalogName;
		private final cn.sexycode.myjpa.sql.model.Identifier schemaName;
		private final cn.sexycode.myjpa.sql.model.Identifier objectName;

		private final String qualifiedText;

		public NameParts(
                cn.sexycode.myjpa.sql.model.Identifier catalogName, cn.sexycode.myjpa.sql.model.Identifier schemaName, cn.sexycode.myjpa.sql.model.Identifier objectName) {
			if ( objectName == null ) {
				throw new IllegalArgumentException( "Name cannot be null" );
			}

			this.catalogName = catalogName;
			this.schemaName = schemaName;
			this.objectName = objectName;

			StringBuilder buff = new StringBuilder();
			if ( catalogName != null ) {
				buff.append( catalogName.toString() ).append( '.' );
			}
			if ( schemaName != null ) {
				buff.append( schemaName.toString() ).append( '.' );
			}
			buff.append( objectName.toString() );
			qualifiedText = buff.toString();
		}

		@Override
		public cn.sexycode.myjpa.sql.model.Identifier getCatalogName() {
			return catalogName;
		}

		@Override
		public cn.sexycode.myjpa.sql.model.Identifier getSchemaName() {
			return schemaName;
		}

		@Override
		public cn.sexycode.myjpa.sql.model.Identifier getObjectName() {
			return objectName;
		}

		@Override
		public String render() {
			return qualifiedText;
		}

		@Override
		public String toString() {
			return qualifiedText;
		}

		@Override
		@SuppressWarnings("SimplifiableIfStatement")
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}

			NameParts that = (NameParts) o;

			return Objects.equals( this.getCatalogName(), that.getCatalogName() )
					&& Objects.equals( this.getSchemaName(), that.getSchemaName() )
					&& Objects.equals( this.getObjectName(), that.getObjectName() );
		}

		@Override
		public int hashCode() {
			int result = getCatalogName() != null ? getCatalogName().hashCode() : 0;
			result = 31 * result + ( getSchemaName() != null ? getSchemaName().hashCode() : 0);
			result = 31 * result + getObjectName().hashCode();
			return result;
		}
	}

	/**
	 * Parses a textual representation of a qualified name into a NameParts
	 * representation.  Explicitly looks for the form {@code catalog.schema.name}.
	 *
	 * @param text The simple text representation of the qualified name.
	 *
	 * @return The wrapped QualifiedName
	 */
	public NameParts parse(String text, cn.sexycode.myjpa.sql.model.Identifier defaultCatalog, cn.sexycode.myjpa.sql.model.Identifier defaultSchema) {
		if ( text == null ) {
			throw new IllegalIdentifierException( "Object name to parse must be specified, but found null" );
		}

		String catalogName = null;
		String schemaName = null;
		String name;

		boolean catalogWasQuoted = false;
		boolean schemaWasQuoted = false;
		boolean nameWasQuoted;

		// Note that we try to handle both forms of quoting,
		//		1) where the entire string was quoted
		//		2) where  one or more individual parts were quoted

		boolean wasQuotedInEntirety = text.startsWith( "`" ) && text.endsWith( "`" );
		if ( wasQuotedInEntirety ) {
			text = unquote( text );
		}

		final String[] tokens = text.split( "\\." );
		if ( tokens.length == 0 || tokens.length == 1 ) {
			// we have just a local name...
			name = text;
		}
		else if ( tokens.length == 2 ) {
			schemaName = tokens[0];
			name = tokens[1];
		}
		else if ( tokens.length == 3 ) {
			schemaName = tokens[0];
			catalogName = tokens[1];
			name = tokens[2];
		}
		else {
            throw new MysqlException("Unable to parse object name: " + text);
		}

		nameWasQuoted = cn.sexycode.myjpa.sql.model.Identifier.isQuoted( name );
		if ( nameWasQuoted ) {
			name = unquote( name );
		}

		if ( schemaName != null ) {
			schemaWasQuoted = cn.sexycode.myjpa.sql.model.Identifier.isQuoted( schemaName );
			if ( schemaWasQuoted ) {
				schemaName = unquote( schemaName );
			}
		}
		else if ( defaultSchema != null ) {
			schemaName = defaultSchema.getText();
			schemaWasQuoted = defaultSchema.isQuoted();
		}

		if ( catalogName != null ) {
			catalogWasQuoted = cn.sexycode.myjpa.sql.model.Identifier.isQuoted( catalogName );
			if ( catalogWasQuoted ) {
				catalogName = unquote( catalogName );
			}
		}
		else if ( defaultCatalog != null ) {
			catalogName = defaultCatalog.getText();
			catalogWasQuoted = defaultCatalog.isQuoted();
		}

		return new NameParts(
				cn.sexycode.myjpa.sql.model.Identifier.toIdentifier( catalogName, wasQuotedInEntirety||catalogWasQuoted ),
				cn.sexycode.myjpa.sql.model.Identifier.toIdentifier( schemaName, wasQuotedInEntirety||schemaWasQuoted ),
				Identifier.toIdentifier( name, wasQuotedInEntirety||nameWasQuoted )
		);
	}

	private static String unquote(String text) {
		return text.substring( 1, text.length() - 1 );
	}

	/**
	 * Parses a textual representation of a qualified name into a NameParts
	 * representation.  Explicitly looks for the form {@code catalog.schema.name}.
	 *
	 * @param text The simple text representation of the qualified name.
	 *
	 * @return The wrapped QualifiedName
	 */
	public NameParts parse(String text) {
		return parse( text, null, null );
	}
}
