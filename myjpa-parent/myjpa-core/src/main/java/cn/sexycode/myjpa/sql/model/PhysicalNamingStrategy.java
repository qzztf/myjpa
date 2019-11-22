package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.Environment;
import cn.sexycode.myjpa.sql.model.Identifier;

/**
 * Pluggable strategy contract for applying physical naming rules for database object names.
 *
 * NOTE: Ideally we'd pass "extra" things in here like Dialect, etc to better handle identifier
 * length constraints or auto quoting of identifiers.  However, the pre-metamodel model does not
 * necessarily know this information at the time the strategy is called.
 *
 */
public interface PhysicalNamingStrategy {
	 cn.sexycode.myjpa.sql.model.Identifier toPhysicalCatalogName(cn.sexycode.myjpa.sql.model.Identifier name,
             Environment Environment);

	 cn.sexycode.myjpa.sql.model.Identifier toPhysicalSchemaName(cn.sexycode.myjpa.sql.model.Identifier name,
             Environment Environment);

	 cn.sexycode.myjpa.sql.model.Identifier toPhysicalTableName(cn.sexycode.myjpa.sql.model.Identifier name,
             Environment Environment);

	 cn.sexycode.myjpa.sql.model.Identifier toPhysicalSequenceName(cn.sexycode.myjpa.sql.model.Identifier name,
             Environment Environment);

	 cn.sexycode.myjpa.sql.model.Identifier toPhysicalColumnName(Identifier name, Environment Environment);
}
