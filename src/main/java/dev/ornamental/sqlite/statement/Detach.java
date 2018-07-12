package dev.ornamental.sqlite.statement;

/**
 * Represents a <code>DETACH</code> statement removing a database (schema) from the
 * current connection:<br>
 * <code><strong>DETACH DATABASE <em>schemaName</em></strong></code>
 */
public final class Detach implements ExplicableStatement {

	private final CharSequence schemaName;

	Detach(CharSequence schemaName) {
		this.schemaName = schemaName;
	}

	@Override
	public Detach copy() throws IllegalStateException {
		CharSequence schemaNameCopy = schemaName.toString();

		return schemaNameCopy == schemaName ? this : new Detach(schemaNameCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("DETACH DATABASE ");
		SqliteUtilities.appendQuotedName(receptacle, schemaName);
	}
}
