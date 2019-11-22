package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.MysqlException;
import cn.sexycode.myjpa.sql.mapping.Exportable;
import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.myjpa.sql.model.QualifiedNameParser;
import cn.sexycode.myjpa.sql.model.QualifiedSequenceName;

/**
 * Models a database {@code SEQUENCE}.
 *
 */
public class Sequence implements Exportable {
	public static class Name extends QualifiedNameParser.NameParts {
		public Name(
				Identifier catalogIdentifier,
				Identifier schemaIdentifier,
				Identifier nameIdentifier) {
			super( catalogIdentifier, schemaIdentifier, nameIdentifier );
		}
	}

	private final QualifiedSequenceName name;
	private final String exportIdentifier;
	private int initialValue = 1;
	private int incrementSize = 1;

	public Sequence(Identifier catalogName, Identifier schemaName, Identifier sequenceName) {
		this.name = new QualifiedSequenceName( catalogName, schemaName, sequenceName );
		this.exportIdentifier = name.render();
	}

	public Sequence(
			Identifier catalogName,
			Identifier schemaName,
			Identifier sequenceName,
			int initialValue,
			int incrementSize) {
		this( catalogName, schemaName, sequenceName );
		this.initialValue = initialValue;
		this.incrementSize = incrementSize;
	}

	public QualifiedSequenceName getName() {
		return name;
	}

	@Override
	public String getExportIdentifier() {
		return exportIdentifier;
	}

	public int getInitialValue() {
		return initialValue;
	}

	public int getIncrementSize() {
		return incrementSize;
	}

	public void validate(int initialValue, int incrementSize) {
		if ( this.initialValue != initialValue ) {
            throw new MysqlException(
					String.format(
							"Multiple references to database sequence [%s] were encountered attempting to " +
									"set conflicting values for 'initial value'.  Found [%s] and [%s]",
							exportIdentifier,
							this.initialValue,
							initialValue
					)
			);
		}
		if ( this.incrementSize != incrementSize ) {
            throw new MysqlException(
					String.format(
							"Multiple references to database sequence [%s] were encountered attempting to " +
									"set conflicting values for 'increment size'.  Found [%s] and [%s]",
							exportIdentifier,
							this.incrementSize,
							incrementSize
					)
			);
		}
	}
}
