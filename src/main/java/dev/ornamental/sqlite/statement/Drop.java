package dev.ornamental.sqlite.statement;

/**
 * This is the common ancestor class for the classes representing <code>DROP</code> statements.
 * The specific implementations are the nested subclasses {@link Index}, {@link Table},
 * {@link Trigger}, and {@link View}.<br/>
 * Each implementation is a complete SQL statement.
 */
public abstract class Drop implements ExplicableStatement {

	/**
	 * Represents a <code>DROP INDEX</code> statement having the form<br/>
	 * <code><strong>DROP INDEX [IF EXISTS] [<em>schemaName</em>.]<em>indexName</em></strong></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class Index extends Drop {

		Index(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
			super(ifExists, schemaName, objectName);
		}

		@Override
		public Index copy() throws IllegalStateException {
			return (Index)super.copy();
		}

		@Override
		protected String objectType() {
			return "INDEX";
		}

		@Override
		protected Index create(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
			return new Index(ifExists, schemaName, objectName);
		}
	}

	/**
	 * Represents a <code>DROP TABLE</code> statement having the form<br/>
	 * <code><strong>DROP TABLE [IF EXISTS] [<em>schemaName</em>.]<em>tableName</em></strong></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class Table extends Drop {

		Table(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
			super(ifExists, schemaName, objectName);
		}

		@Override
		public Table copy() throws IllegalStateException {
			return (Table)super.copy();
		}

		@Override
		protected String objectType() {
			return "TABLE";
		}

		@Override
		protected Table create(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
			return new Table(ifExists, schemaName, objectName);
		}
	}

	/**
	 * Represents a <code>DROP TRIGGER</code> statement having the form<br/>
	 * <code><strong>DROP TRIGGER [IF EXISTS] [<em>schemaName</em>.]<em>triggerName</em></strong></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class Trigger extends Drop {

		Trigger(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
			super(ifExists, schemaName, objectName);
		}

		@Override
		public Trigger copy() throws IllegalStateException {
			return (Trigger)super.copy();
		}

		@Override
		protected String objectType() {
			return "TRIGGER";
		}

		@Override
		protected Trigger create(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
			return new Trigger(ifExists, schemaName, objectName);
		}
	}

	/**
	 * Represents a <code>DROP VIEW</code> statement having the form<br/>
	 * <code><strong>DROP VIEW [IF EXISTS] [<em>schemaName</em>.]<em>viewName</em></strong></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class View extends Drop {

		View(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
			super(ifExists, schemaName, objectName);
		}

		@Override
		public View copy() throws IllegalStateException {
			return (View)super.copy();
		}

		@Override
		protected String objectType() {
			return "VIEW";
		}

		@Override
		protected Drop create(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
			return new Trigger(ifExists, schemaName, objectName);
		}
	}

	private final boolean ifExists;

	private final CharSequence schemaName; // nullable

	private final CharSequence objectName;

	Drop(boolean ifExists, CharSequence schemaName, CharSequence objectName) {
		this.ifExists = ifExists;
		this.schemaName = schemaName;
		this.objectName = objectName;
	}

	protected abstract String objectType();

	protected abstract Drop create(boolean ifExists, CharSequence schemaName, CharSequence objectName);

	@Override
	public Drop copy() throws IllegalStateException {
		CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
		CharSequence objectNameCopy = objectName.toString();

		return schemaNameCopy == schemaName && objectNameCopy == objectName
			? this : create(ifExists, schemaNameCopy, objectNameCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("DROP ").append(objectType()).append(' ');
		if (ifExists) {
			receptacle.append("IF EXISTS ");
		}
		SqliteUtilities.appendQuotedName(receptacle, schemaName, objectName);
	}
}
