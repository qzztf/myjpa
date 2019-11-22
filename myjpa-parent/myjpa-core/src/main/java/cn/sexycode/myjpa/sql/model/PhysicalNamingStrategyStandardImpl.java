package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.Environment;
import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.myjpa.sql.model.PhysicalNamingStrategy;

import java.io.Serializable;

/**
 * Standard implementation of the PhysicalNamingStrategy contract.
 *
 */
public class PhysicalNamingStrategyStandardImpl implements PhysicalNamingStrategy, Serializable {
	/**
	 * Singleton access
	 */
	public static final PhysicalNamingStrategyStandardImpl INSTANCE = new PhysicalNamingStrategyStandardImpl();

	@Override
	public cn.sexycode.myjpa.sql.model.Identifier toPhysicalCatalogName(cn.sexycode.myjpa.sql.model.Identifier name, Environment context) {
		return name;
	}

	@Override
	public cn.sexycode.myjpa.sql.model.Identifier toPhysicalSchemaName(cn.sexycode.myjpa.sql.model.Identifier name, Environment context) {
		return name;
	}

	@Override
	public cn.sexycode.myjpa.sql.model.Identifier toPhysicalTableName(cn.sexycode.myjpa.sql.model.Identifier name, Environment context) {
		return name;
	}

	@Override
	public cn.sexycode.myjpa.sql.model.Identifier toPhysicalSequenceName(cn.sexycode.myjpa.sql.model.Identifier name, Environment context) {
		return name;
	}

	@Override
	public cn.sexycode.myjpa.sql.model.Identifier toPhysicalColumnName(Identifier name, Environment context) {
		return name;
	}
}
