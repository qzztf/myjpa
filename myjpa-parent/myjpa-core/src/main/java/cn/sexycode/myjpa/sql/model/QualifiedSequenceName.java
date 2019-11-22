package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.myjpa.sql.model.Namespace;
import cn.sexycode.myjpa.sql.model.QualifiedNameImpl;

/**
 */
public class QualifiedSequenceName extends QualifiedNameImpl {
	public QualifiedSequenceName(
            cn.sexycode.myjpa.sql.model.Identifier catalogName, cn.sexycode.myjpa.sql.model.Identifier schemaName, cn.sexycode.myjpa.sql.model.Identifier sequenceName) {
		super( catalogName, schemaName, sequenceName );
	}

	public QualifiedSequenceName(Namespace.Name schemaName, cn.sexycode.myjpa.sql.model.Identifier sequenceName) {
		super( schemaName, sequenceName );
	}

	public Identifier getSequenceName() {
		return getObjectName();
	}
}
