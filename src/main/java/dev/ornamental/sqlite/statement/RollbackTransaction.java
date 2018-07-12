package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL statement having the form<br>
 * <code><strong>ROLLBACK [<em>savepointName</em>]</strong></code><br>
 * rolling back either the outer transaction (when no savepoint name is specified)
 * or to the previously created and not yet released savepoint with the designated name.<br>
 * This is a complete SQL statement.
 */
public final class RollbackTransaction implements ExplicableStatement {

	static final RollbackTransaction OUTER = new RollbackTransaction(null);

	private final CharSequence savepointName;

	RollbackTransaction(CharSequence savepointName) {
		this.savepointName = savepointName;
	}

	@Override
	public RollbackTransaction copy() throws IllegalStateException {
		CharSequence savepointNameCopy = savepointName == null ? null : savepointName.toString();

		return savepointNameCopy == savepointName
			? this : new RollbackTransaction(savepointNameCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("ROLLBACK TRANSACTION");
		if (savepointName != null) {
			receptacle.append(" TO SAVEPOINT ");
			SqliteUtilities.quoteNameIfNecessary(receptacle, savepointName);
		}
	}
}
