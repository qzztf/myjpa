package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.myjpa.sql.model.Namespace;
import cn.sexycode.myjpa.sql.model.QualifiedName;
import cn.sexycode.myjpa.sql.model.QualifiedNameParser;

/**
 */
public class QualifiedNameImpl extends QualifiedNameParser.NameParts implements QualifiedName {
	public QualifiedNameImpl(Namespace.Name schemaName, cn.sexycode.myjpa.sql.model.Identifier objectName) {
		this(
				schemaName.getCatalog(),
				schemaName.getSchema(),
				objectName
		);
	}

	public QualifiedNameImpl(cn.sexycode.myjpa.sql.model.Identifier catalogName, cn.sexycode.myjpa.sql.model.Identifier schemaName, Identifier objectName) {
		super( catalogName, schemaName, objectName );
	}
}
