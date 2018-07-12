package dev.ornamental.sqlite.statement;

import java.util.Arrays;

/**
 * Represents a complete <code>CREATE VIEW</code> statement:<br>
 * <code><strong>CREATE [TEMPORARY] VIEW [IF NOT EXISTS]
 * [<em>schemaName</em>.]<em>viewName</em>[(<em>columnName<sub>0</sub></em>{,
 * <em>columnName<sub>i</sub></em>})] AS <em>selectStatement</em></strong></code>.
 */
public final class CreateView implements ExplicableStatement {

	/**
	 * Represents the initial part of a <code>CREATE VIEW</code> statement, namely<br>
	 * <code><strong>CREATE [TEMPORARY] VIEW [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>viewName</em></strong></code>.
	 */
	public static final class Stub {

		private final boolean temporary;

		private final boolean ifNotExists;

		private final CharSequence schemaName; // may be null

		private final CharSequence viewName;

		Stub(boolean temporary, boolean ifNotExists, CharSequence schemaName, CharSequence viewName) {
			this.temporary = temporary;
			this.ifNotExists = ifNotExists;
			this.schemaName = schemaName;
			this.viewName = viewName;
		}

		/**
		 * Continues the construction of a <code>CREATE VIEW</code> statement by
		 * explicitly specifying the names of the view columns.
		 * @param columnNames the names to be assigned to the view columns
		 * @return a partial <code>CREATE VIEW</code> statement from which only the <code>AS</code>
		 * close is missing:<br>
		 * <code>CREATE [TEMPORARY] VIEW [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>viewName</em><strong>(<em>columnName<sub>0</sub></em>{,
		 * <em>columnName<sub>i</sub></em>})</strong></code>
		 */
		public WithColumnNames withColumnNames(CharSequence... columnNames) {
			return withColumnNames(Arrays.asList(columnNames));
		}

		/**
		 * Continues the construction of a <code>CREATE VIEW</code> statement by
		 * explicitly specifying the names of the view columns.
		 * @param columnNames the names to be assigned to the view columns
		 * @return a partial <code>CREATE VIEW</code> statement from which only the <code>AS</code>
		 * close is missing:<br>
		 * <code>CREATE [TEMPORARY] VIEW [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>viewName</em><strong>(<em>columnName<sub>0</sub></em>{,
		 * <em>columnName<sub>i</sub></em>})</strong></code>
		 */
		public WithColumnNames withColumnNames(Iterable<? extends CharSequence> columnNames) {
			return new WithColumnNames(this, columnNames);
		}

		/**
		 * Specifies the view contents by supplying a <code>SELECT</code> (or <code>VALUES</code>) statement.<br>
		 * The result is a complete SQL statement.
		 * @param selectStatement the statement defining the contents of the view
		 * @return the complete <code>CREATE VIEW</code> statement using the specified <code>SELECT</code>
		 * (or <code>VALUES</code>) statement to define the view contents
		 */
		public CreateView as(SelectStatement selectStatement) {
			return new CreateView(this, selectStatement);
		}

		Stub copy() {
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence viewNameCopy = viewName.toString();

			return schemaNameCopy == schemaName && viewNameCopy == viewName
				? this : new Stub(temporary, ifNotExists, schemaNameCopy, viewNameCopy);
		}

		void appendTo(StringBuilder receptacle) {
			receptacle.append("CREATE ");
			if (temporary) {
				receptacle.append("TEMPORARY ");
			}
			receptacle.append("VIEW ");
			if (ifNotExists) {
				receptacle.append("IF NOT EXISTS ");
			}
			SqliteUtilities.appendQuotedName(receptacle, schemaName, viewName);
		}
	}

	/**
	 * Represents a partial <code>CREATE VIEW</code> statement with explicitly named view columns:<br>
	 * <code><strong>CREATE [TEMPORARY] VIEW [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>tableName</em>(<em>columnName<sub>0</sub></em>{,
	 * <em>columnName<sub>i</sub></em>})</strong></code>.
	 */
	public static final class WithColumnNames {

		private final Stub previous;

		private final Iterable<? extends CharSequence> columnNames;

		WithColumnNames(Stub previous, Iterable<? extends CharSequence> columnNames) {
			this.previous = previous;
			this.columnNames = columnNames;
		}

		/**
		 * Specifies the view contents by supplying a <code>SELECT</code> (or <code>VALUES</code>) statement.<br>
		 * The result is a complete SQL statement.
		 * @param selectStatement the statement defining the contents of the view
		 * @return the complete <code>CREATE VIEW</code> statement using the specified <code>SELECT</code>
		 * (or <code>VALUES</code>) statement to define the view contents
		 */
		public CreateView as(SelectStatement selectStatement) {
			return new CreateView(this, selectStatement);
		}

		WithColumnNames copy() {
			Stub previousCopy = previous.copy();
			Iterable<? extends CharSequence> columnNamesCopy =
				ReadonlyIterable.of(columnNames, CharSequence::toString);

			return previousCopy == previous && columnNamesCopy == columnNames
				? this : new WithColumnNames(previousCopy, columnNamesCopy);
		}

		void appendTo(StringBuilder receptacle) {
			previous.appendTo(receptacle);
			receptacle.append('(');
			SqliteUtilities.appendQuotedDelimited(receptacle, columnNames);
			receptacle.append(')');
		}
	}

	private final Stub previousNoColumnNames;

	private final WithColumnNames previousWithColumnNames;

	private final SelectStatement selectStatement;

	CreateView(Stub previousNoColumnNames, SelectStatement selectStatement) {
		this(previousNoColumnNames, null, selectStatement);
	}

	CreateView(WithColumnNames previousWithColumnNames, SelectStatement selectStatement) {
		this(null, previousWithColumnNames, selectStatement);
	}

	private CreateView(
		Stub previousNoColumnNames, WithColumnNames previousWithColumnNames, SelectStatement selectStatement) {

		this.previousNoColumnNames = previousNoColumnNames;
		this.previousWithColumnNames = previousWithColumnNames;
		this.selectStatement = selectStatement;
	}

	@Override
	public CreateView copy() throws IllegalStateException {
		Stub previousNoColumnNamesCopy =
			previousNoColumnNames == null ? null : previousNoColumnNames.copy();
		WithColumnNames previousWithColumnNamesCopy =
			previousWithColumnNames == null ? null : previousWithColumnNames.copy();
		SelectStatement selectStatementCopy = selectStatement.copy();

		return
			previousNoColumnNamesCopy == previousNoColumnNames
			&& previousWithColumnNamesCopy == previousWithColumnNames
			&& selectStatementCopy == selectStatement
				? this
				: new CreateView(previousNoColumnNamesCopy, previousWithColumnNamesCopy, selectStatementCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		if (previousWithColumnNames != null) {
			previousWithColumnNames.appendTo(receptacle);
		} else {
			previousNoColumnNames.appendTo(receptacle);
		}

		receptacle.append(" AS ");
		selectStatement.build(receptacle);
	}
}
