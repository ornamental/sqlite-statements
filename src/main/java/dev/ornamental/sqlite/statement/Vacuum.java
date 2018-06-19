package dev.ornamental.sqlite.statement;

/**
 * Executes the vacuuming process on a specific database or on all the attached databases.
 * It has the form<br/>
 * <code><strong>VACUUM [<em>schemaName</em>]</strong></code>.<br/>
 * This is a complete SQL statement.
 */
public final class Vacuum implements ExplicableStatement {

	static final Vacuum ALL = new Vacuum(null);

	private final CharSequence schemaName; // nullable

	Vacuum(CharSequence schemaName) {
		this.schemaName = schemaName;
	}

	@Override
	public Vacuum copy() throws IllegalStateException {
		CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();

		return schemaNameCopy == schemaName ? this : new Vacuum(schemaNameCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("VACUUM");
		if (schemaName != null) {
			receptacle.append(' ');
			SqliteUtilities.appendQuotedName(receptacle, schemaName);
		}
	}
}
