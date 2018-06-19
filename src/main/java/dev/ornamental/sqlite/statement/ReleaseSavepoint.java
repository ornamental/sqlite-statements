package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL statement having the form<br/>
 * <code><strong>RELEASE <em>savepointName</em></strong></code><br/>
 * releasing the specified savepoint.<br/>
 * This is a complete SQL statement.
 */
public final class ReleaseSavepoint implements ExplicableStatement {

	private final CharSequence savepointName;

	ReleaseSavepoint(CharSequence savepointName) {
		this.savepointName = savepointName;
	}

	@Override
	public ReleaseSavepoint copy() throws IllegalStateException {
		CharSequence savepointNameCopy = savepointName.toString();

		return savepointNameCopy == savepointName
			? this : new ReleaseSavepoint(savepointNameCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("RELEASE SAVEPOINT ");
		SqliteUtilities.quoteNameIfNecessary(receptacle, savepointName);
	}
}
