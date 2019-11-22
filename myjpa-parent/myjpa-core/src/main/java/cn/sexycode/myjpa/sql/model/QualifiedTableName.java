package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.myjpa.sql.model.Namespace;
import cn.sexycode.myjpa.sql.model.QualifiedNameImpl;

/**
 */
public class QualifiedTableName extends QualifiedNameImpl {
	public QualifiedTableName(cn.sexycode.myjpa.sql.model.Identifier catalogName, cn.sexycode.myjpa.sql.model.Identifier schemaName, cn.sexycode.myjpa.sql.model.Identifier tableName) {
		super( catalogName, schemaName, tableName );
	}

	public QualifiedTableName(Namespace.Name schemaName, cn.sexycode.myjpa.sql.model.Identifier tableName) {
		super( schemaName, tableName );
	}

	public Identifier getTableName() {
		return getObjectName();
	}
}
