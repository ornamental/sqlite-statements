package dev.ornamental.sqlite.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class has no functionality of its own. It is destined for grouping the classes
 * pertaining to <code>INSERT</code> statements.
 */
public final class Insert {

	/**
	 * Represents the initial stage of construction of an <code>INSERT</code> statement, namely the part<br/>
	 * <code><strong>[WITH <em>cte</em>] INSERT [OR REPLACE|ABORT|FAIL|ROLLBACK|IGNORE]</strong></code><br/>
	 * (or <code><strong>[WITH <em>cte</em>] REPLACE</strong></code>
	 * which is the same as <code>[WITH <em>cte</em>] INSERT OR REPLACE</code>).
	 */
	public static final class Stub {

		private final CommonTableExpression cte; // nullable

		private final InsertVerb verb;

		Stub(CommonTableExpression cte, InsertVerb verb) {
			this.cte = cte;
			this.verb = verb;
		}

		/**
		 * Adds the name of the target table to the incomplete <code>INSERT</code> statement.
		 * @param tableName the name of the target table
		 * @return the initial part of the <code>INSERT</code> statement with the table name specified
		 */
		public Into into(CharSequence tableName) {
			return new Into(cte, verb, null, tableName);
		}

		/**
		 * Adds the name of the target table to the incomplete <code>INSERT</code> statement.
		 * @param schemaName the name of the schema the target table belongs to
		 * @param tableName the name of the target table
		 * @return the initial part of the <code>INSERT</code> statement with the table name specified
		 */
		public Into into(CharSequence schemaName, CharSequence tableName) {
			return new Into(cte, verb, schemaName, tableName);
		}
	}

	private abstract static class Incomplete {

		/**
		 * Complements the <code>INSERT</code> statement stub with a
		 * <code>DEFAULT VALUES</code> clause: the inserted values will be the corresponding
		 * columns' defaults.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>INSERT</code> statement for inserting the default values into the target table
		 */
		public Defaults defaultValues() {
			return new Defaults(this);
		}

		/**
		 * Complements the <code>INSERT</code> statement stub with a
		 * <code>SELECT</code> or <code>VALUES</code> clause: the inserted values will be taken
		 * from the result of a <code>SELECT</code> (or a <code>VALUES</code>) statement.<br/>
		 * The result is a complete SQL statement.
		 * @param selectStatement the <code>SELECT</code> (or <code>VALUES</code>) statement
		 * being the source of the rows to insert
		 * @return the <code>INSERT</code> statement for inserting the results of a <code>SELECT</code>
		 * statement into the table
		 */
		public FromSelect from(SelectStatement selectStatement) {
			return new FromSelect(this, selectStatement);
		}

		/**
		 * Complements the <code>INSERT</code> statement stub with a <code>VALUES</code> clause:
		 * the inserted rows are explicitly specified on the next stage of statement construction.
		 * @return the <code>INSERT .. VALUES</code> statement stub
		 */
		public ValuesStub values() {
			return new ValuesStub(this);
		}

		/**
		 * Complements the <code>INSERT</code> statement stub with a <code>VALUES</code> clause
		 * with explicitly specified rows to insert.<br/>
		 * The result is a complete SQL statement
		 * @param rows the rows to insert into the table
		 * @return the <code>INSERT .. VALUES</code> statement
		 */
		public AllValues values(RowExpression... rows) {
			return values(Arrays.asList(rows));
		}

		/**
		 * Complements the <code>INSERT</code> statement stub with a <code>VALUES</code> clause
		 * with explicitly specified rows to insert.<br/>
		 * The result is a complete SQL statement
		 * @param rows the rows to insert into the table
		 * @return the <code>INSERT .. VALUES</code> statement
		 */
		public AllValues values(Iterable<? extends RowExpression> rows) {
			return new AllValues(this, rows);
		}

		abstract void appendTo(StringBuilder receptacle);

		abstract Incomplete copy();
	}

	/**
	 * Represents an <code>INSERT</code> statement stub with the target table name specified.
	 */
	public static final class Into extends Incomplete {

		private final CommonTableExpression cte; // nullable

		private final InsertVerb verb;

		private final CharSequence schemaName; // may be null

		private final CharSequence tableName;

		Into(CommonTableExpression cte, InsertVerb verb, CharSequence schemaName, CharSequence tableName) {
			this.cte = cte;
			this.verb = verb;
			this.schemaName = schemaName;
			this.tableName = tableName;
		}

		/**
		 * Explicitly specifies the target column names whose values will be determined on the next stage
		 * of the statement construction.
		 * @param columnNames the names of the target table columns
		 * @return the <code>INSERT</code> statement stub with explicitly specified target columns
		 */
		public WithColumnNames columns(CharSequence... columnNames) {
			return columns(Arrays.asList(columnNames));
		}

		/**
		 * Explicitly specifies the target column names whose values will be determined on the next stage
		 * of the statement construction.
		 * @param columnNames the names of the target table columns
		 * @return the <code>INSERT</code> statement stub with explicitly specified target columns
		 */
		public WithColumnNames columns(Iterable<? extends CharSequence> columnNames) {
			return new WithColumnNames(this, columnNames);
		}

		void appendTo(StringBuilder receptacle) {
			if (cte != null) {
				cte.appendTo(receptacle);
				receptacle.append(' ');
			}
			receptacle.append(verb.toString()).append(" INTO ");
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
		}

		Into copy() {
			CommonTableExpression cteCopy = cte == null ? null : cte.copy();
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();

			return cteCopy == cte && schemaNameCopy == schemaName && tableNameCopy == tableName
				? this : new Into(cteCopy, verb, schemaNameCopy, tableNameCopy);
		}
	}

	/**
	 * Represents an <code>INSERT</code> statement stub with explicitly specified target columns.
	 */
	public static final class WithColumnNames extends Incomplete {

		private final Into stub;

		private final Iterable<? extends CharSequence> columnNames;

		WithColumnNames(Into stub, Iterable<? extends CharSequence> columnNames) {
			this.stub = stub;
			this.columnNames = columnNames;
		}

		void appendTo(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			receptacle.append('(');
			SqliteUtilities.appendQuotedDelimited(receptacle, columnNames);
			receptacle.append(')');
		}

		WithColumnNames copy() {
			Into stubCopy = stub.copy();
			Iterable<? extends CharSequence> columnNamesCopy =
				ReadonlyIterable.of(columnNames, CharSequence::toString);

			return stubCopy == stub && columnNamesCopy == columnNames
				? this : new WithColumnNames(stubCopy, columnNamesCopy);
		}
	}

	/**
	 * Represents an <code>INSERT</code> statement using column default values
	 * instead of explicitly specified tuples or a source <code>SELECT</code> statement.
	 */
	public static final class Defaults implements TriggerStatement {

		private final Incomplete incomplete;

		Defaults(Incomplete incomplete) {
			this.incomplete = incomplete;
		}

		@Override
		public Defaults copy() {
			Incomplete incompleteCopy = incomplete.copy();

			return incompleteCopy == incomplete ? this : new Defaults(incompleteCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			incomplete.appendTo(receptacle);
			receptacle.append(" DEFAULT VALUES");
		}
	}

	/**
	 * Represents an <code>INSERT</code> statement using a <code>SELECT</code> (or <code>VALUES</code>)
	 * statement as the source of the rows to insert.
	 */
	public static final class FromSelect implements TriggerStatement {

		private final Incomplete incomplete;

		private final SelectStatement source;

		FromSelect(Incomplete incomplete, SelectStatement source) {
			this.incomplete = incomplete;
			this.source = source;
		}

		@Override
		public FromSelect copy() {
			Incomplete incompleteCopy = incomplete.copy();
			SelectStatement sourceCopy = source.copy();

			return incompleteCopy == incomplete && sourceCopy == source
				? this : new FromSelect(incompleteCopy, sourceCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			incomplete.appendTo(receptacle);
			receptacle.append(' ');
			source.build(receptacle);
		}
	}

	/**
	 * Represents an <code>INSERT</code> statement using an explicit list of rows
	 * to insert into the target  table.
	 */
	public static final class AllValues implements TriggerStatement {

		private final Incomplete stub;

		private final Iterable<? extends RowExpression> rows;

		AllValues(Incomplete stub, Iterable<? extends RowExpression> rows) {
			this.stub = stub;
			this.rows = rows;
		}

		@Override
		public AllValues copy() {
			Incomplete stubCopy = stub.copy();
			Iterable<? extends RowExpression> rowsCopy = ReadonlyIterable.of(rows, RowExpression::copy);

			return stubCopy == stub && rowsCopy == rows
				? this : new AllValues(stubCopy, rowsCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			receptacle.append(" VALUES ");
			Iterator<? extends RowExpression> rowIterator = rows.iterator();
			if (!rowIterator.hasNext()) {
				throw new IllegalArgumentException();
			}
			RowExpression last = rowIterator.next();
			while (rowIterator.hasNext()) {
				last.appendTo(receptacle);
				receptacle.append(", ");
				last = rowIterator.next();
			}
			last.appendTo(receptacle);
		}
	}

	/**
	 * This interface allows adding value tuples in a manner uniform
	 * for both {@link ValuesStub} and {@link Values} classes, the first containing
	 * yet no value tuples (and thus not being a valid SQL statement) and the second
	 * containing at least one.
	 */
	public interface ValuesList {

		/**
		 * Adds a new value tuple to the <code>VALUES</code> clause of the <code>INSERT</code> statement.<br/>
		 * The result is a complete SQL statement.
		 * @param row the expressions used to evaluate the values for insertion
		 * @return the <code>INSERT .. VALUES</code> statement having the specified
		 * tuple of expressions as the last one
		 */
		default Values add(SqlExpression... row) {
			return add(Arrays.asList(row));
		}

		/**
		 * Adds a new value tuple to the <code>VALUES</code> clause of the <code>INSERT</code> statement.<br/>
		 * The result is a complete SQL statement.
		 * @param row the expressions used to evaluate the values for insertion
		 * @return the <code>INSERT .. VALUES</code> statement having the specified
		 * tuple of expressions as the last one
		 */
		Values add(Iterable<? extends SqlExpression> row);
	}

	/**
	 * Represents a stub of <code>INSERT .. VALUES</code> statement to which no value tuples
	 * have yet been added.
	 */
	public static final class ValuesStub implements ValuesList {

		private final Incomplete stub;

		ValuesStub(Incomplete stub) {
			this.stub = stub;
		}

		@Override
		public Values add(Iterable<? extends SqlExpression> row) {
			return new Values(stub, new Values.Row(null, row));
		}
	}

	/**
	 * Represents an <code>INSERT</code> statement using an explicit list of rows
	 * to insert into the target table.
	 */
	public static final class Values implements TriggerStatement, ValuesList {

		static final class Row {

			private final Row previous;

			private final Iterable<? extends SqlExpression> row;

			Row(Row previous, Iterable<? extends SqlExpression> row) {
				this.previous = previous;
				this.row = row;
			}

			Row copy() {
				Row previousCopy = previous == null ? null : previous.copy();
				Iterable<SqlExpression> rowCopy = ReadonlyIterable.of(row, SqlExpression::copy);

				return previousCopy == previous && rowCopy == row ? this : new Row(previousCopy, rowCopy);
			}
		}

		private final Incomplete stub;

		private final Row tail;

		Values(Values previous, Iterable<? extends SqlExpression> row) {
			this.stub = previous.stub;
			this.tail = new Row(previous.tail, row);
		}

		Values(Incomplete stub, Row tail) {
			this.stub = stub;
			this.tail = tail;
		}

		@Override
		public Values add(Iterable<? extends SqlExpression> row) {
			return new Values(this, row);
		}

		@Override
		public Values copy() {
			Incomplete stubCopy = stub.copy();
			Row tailCopy = tail == null ? null : tail.copy();

			return stubCopy == stub && tailCopy == tail ? this : new Values(stubCopy, tailCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			receptacle.append(" VALUES ");
			List<Iterable<? extends SqlExpression>> list = new ArrayList<>();
			Row current = tail;
			while (current != null) {
				list.add(current.row);
				current = current.previous;
			}
			for (int i = list.size() - 1; i >= 0; i--) {
				Iterable<? extends SqlExpression> item = list.get(i);
				receptacle.append('(');
				appendRow(receptacle, item);
				receptacle.append(')');
				if (i != 0) {
					receptacle.append(", ");
				}
			}
		}

		private static void appendRow(StringBuilder receptacle, Iterable<? extends SqlExpression> item) {
			Iterator<? extends SqlExpression> iterator = item.iterator();
			if (!iterator.hasNext()) {
				throw new IllegalStateException("Each row must contain at least one value.");
			}
			SqlExpression last = iterator.next();
			while (iterator.hasNext()) {
				last.appendTo(receptacle);
				receptacle.append(", ");
				last = iterator.next();
			}
			last.appendTo(receptacle);
		}
	}

	private Insert() { }
}
