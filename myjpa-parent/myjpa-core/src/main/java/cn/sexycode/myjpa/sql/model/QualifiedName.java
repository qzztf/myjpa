package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.model.Identifier;

/**
 * Models the qualified name of a database object.  Some things to keep in
 * mind wrt catalog/schema:<ol>
 *     <li>{@link java.sql.DatabaseMetaData#isCatalogAtStart}</li>
 *     <li>{@link java.sql.DatabaseMetaData#getCatalogSeparator()}</li>
 * </ol>
 * <p/>
 * Also, be careful about the usage of {@link #render}.  If the intention is get get the name
 * as used in the database, the {@link org.hibernate.engine.jdbc.env.spi.JdbcEnvironment} ->
 * {@link QualifiedObjectNameFormatter#format} should be
 * used instead.
 *
 */
public interface QualifiedName {
	cn.sexycode.myjpa.sql.model.Identifier getCatalogName();
	cn.sexycode.myjpa.sql.model.Identifier getSchemaName();
	Identifier getObjectName();

	/**
	 * Returns a String-form of the qualified name.
	 * <p/>
	 * Depending on intention, may not be appropriate.  May want
	 * {@link org.hibernate.engine.jdbc.env.spi.QualifiedObjectNameFormatter#format}
	 * instead.  See {@link org.hibernate.engine.jdbc.env.spi.JdbcEnvironment#getQualifiedObjectNameFormatter}
	 *
	 * @return The string form
	 */
	String render();
}
