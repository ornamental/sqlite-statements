package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL statement having the form<br>
 * <code><strong>SAVEPOINT <em>savepointName</em></strong></code><br>
 * creating a new savepoint with the designated name.<br>
 * This is a complete SQL statement.
 */
public final class Savepoint implements ExplicableStatement {

	private final CharSequence savepointName;

	Savepoint(CharSequence savepointName) {
		this.savepointName = savepointName;
	}

	@Override
	public Savepoint copy() throws IllegalStateException {
		CharSequence savepointNameCopy = savepointName.toString();

		return savepointNameCopy == savepointName
			? this : new Savepoint(savepointNameCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("SAVEPOINT ");
		SqliteUtilities.quoteNameIfNecessary(receptacle, savepointName);
	}
}
