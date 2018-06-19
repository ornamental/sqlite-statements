package dev.ornamental.sqlite.statement;

/**
 * Represents an <code>ATTACH</code> statement having the form<br/>
 * <code><strong>ATTACH <em>databaseFileNameExpression</em> AS <em>schemaName</em></strong></code><br/>
 * attaching a database to the connection.
 */
public final class Attach implements ExplicableStatement {

	public static final class Stub {

		private final SqlExpression database;

		Stub(SqlExpression database) {
			this.database = database;
		}

		public ExplicableStatement as(CharSequence schemaName) {
			return new Attach(database, schemaName);
		}
	}

	private final SqlExpression database;

	private final CharSequence schemaName;

	Attach(SqlExpression database, CharSequence schemaName) {
		this.database = database;
		this.schemaName = schemaName;
	}

	@Override
	public Attach copy() throws IllegalStateException {
		SqlExpression databaseCopy = database.copy();
		CharSequence schemaNameCopy = schemaName.toString();

		return databaseCopy == database && schemaNameCopy == schemaName
			? this : new Attach(databaseCopy, schemaNameCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("ATTACH DATABASE ");
		database.appendTo(receptacle);
		receptacle.append(" AS ");
		SqliteUtilities.appendQuotedName(receptacle, schemaName);
	}
}
