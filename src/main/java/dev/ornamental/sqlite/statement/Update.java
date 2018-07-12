package dev.ornamental.sqlite.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class has no functionality of its own. It is destined for grouping the classes pertaining
 * to <code>UPDATE</code> statements.
 */
public final class Update {

	/**
	 * Represents the initial stage of construction of an <code>UPDATE</code> statement,
	 * namely the part<br>
	 * <code><strong>[WITH <em>cte</em>] UPDATE [OR REPLACE|ROLLBACK|ABORT|FAIL|IGNORE]</strong></code>.
	 */
	public static final class Verb {

		private final CommonTableExpression cte; // nullable

		private final UpdateVerb verb;

		Verb(CommonTableExpression cte, UpdateVerb verb) {
			this.cte = cte;
			this.verb = verb;
		}

		/**
		 * Appends the target table name to the statement being built.
		 * @param tableName the target table name
		 * @return the initial part of an <code>UPDATE</code> statement having the form<br>
		 * <code>[WITH <em>cte</em>] UPDATE [OR REPLACE|ROLLBACK|ABORT|FAIL|IGNORE]
		 * <strong>INTO <em>tableName</em></strong></code>
		 */
		public Stub into(CharSequence tableName) {
			return new Stub(cte, verb, null, tableName);
		}

		/**
		 * Appends the target table name to the statement being built.
		 * @param schemaName the name of the schema containing the target table
		 * @param tableName the target table name
		 * @return the initial part of an <code>UPDATE</code> statement having the form<br>
		 * <code>[WITH <em>cte</em>] UPDATE [OR REPLACE|ROLLBACK|ABORT|FAIL|IGNORE]
		 * <strong>INTO <em>schemaName</em>.<em>tableName</em></strong></code>
		 */
		public Stub into(CharSequence schemaName, CharSequence tableName) {
			return new Stub(cte, verb, schemaName, tableName);
		}
	}

	/**
	 * This interface may be used to add <code>ORDER BY</code> sorting keys
	 * in a manner uniform both for an <code>UPDATE</code> statement not yet having
	 * an <code>ORDER BY</code> clause and a limited <code>UPDATE</code> statement stub
	 * already having at least one sorting key specified.
	 */
	public interface Sortable {

		/**
		 * Adds a sorting key expression with the default sorting order to the <code>ORDER BY</code>
		 * clause (in case this is the first sorting key, adds the clause itself).
		 * @param sortingKey the expression by whose value the sorting will be performed
		 * @return the incomplete <code>UPDATE</code> statement ending with an <code>ORDER BY</code>
		 * clause whose last sorting key is the specified expression
		 */
		Ordered orderBy(SqlExpression sortingKey);

		/**
		 * Adds a sorting key expression with the specified sorting order to the <code>ORDER BY</code>
		 * clause (in case this is the first sorting key, adds the clause itself).
		 * @param sortingKey the expression by whose value the sorting will be performed
		 * @param order the sorting order for the key
		 * @return the incomplete <code>UPDATE</code> statement ending with an <code>ORDER BY</code>
		 * clause whose last sorting key is the specified expression
		 */
		Ordered orderBy(SqlExpression sortingKey, SortingOrder order);

		/**
		 * Adds a <code>LIMIT</code> clause.<br>
		 * The result is a complete SQL statement.
		 * @param limitExpression the expression determining the maximum number of rows to update
		 * @return the <code>UPDATE</code> statement with a <code>LIMIT</code> clause
		 */
		LimitedNoOffset limit(SqlExpression limitExpression);

		/**
		 * Adds a <code>LIMIT</code> clause.<br>
		 * The result is a complete SQL statement.
		 * @param maxRows the maximum number of rows to update
		 * @return the <code>UPDATE</code> statement with a <code>LIMIT</code> clause
		 */
		LimitedNoOffset limit(long maxRows);
	}

	/**
	 * The interface implemented by the classes representing the <code>UPDATE</code>
	 * statements having no <code>LIMIT</code> clause.<br>
	 * All the implementations are complete SQL statements.
	 */
	public interface NotLimited extends Sortable, TriggerStatement {

		@Override
		default Ordered orderBy(SqlExpression sortingKey) {
			return new Ordered(this, new Sort(sortingKey, null));
		}

		@Override
		default Ordered orderBy(SqlExpression sortingKey, SortingOrder order) {
			return new Ordered(this, new Sort(sortingKey, order));
		}

		@Override
		default LimitedNoOffset limit(SqlExpression limitExpression) {
			return new LimitedNoOffset(this, new IntegralValueClause.Limit(limitExpression));
		}

		@Override
		default LimitedNoOffset limit(long maxRows) {
			return new LimitedNoOffset(this, new IntegralValueClause.Limit(maxRows));
		}

		@Override
		NotLimited copy();
	}

	/**
	 * This interface may be used to add <code>SET</code> clause column assignments
	 * in a manner uniform both for an <code>UPDATE</code> statement stub not yet
	 * having a <code>SET</code> clause and an <code>UPDATE</code> statement already
	 * having at least one column assignment.
	 */
	public interface AssignmentList {

		/**
		 * Adds a <code>SET</code> column assignment to the <code>UPDATE</code> statement.
		 * @param columnName the name of the column to assign the value to
		 * @param value the expression calculating the value to assign to the column
		 * @return the <code>UPDATE</code> statement ending with the assignment
		 * <code><strong><em>columnName</em> = <em>value</em></strong></code>
		 */
		NotFiltered set(CharSequence columnName, SqlExpression value);

		/**
		 * Adds a <code>SET</code> group column assignment to the <code>UPDATE</code> statement.
		 * @param columnNames the names of the columns to assign the values to
		 * @param tuple the row expression containing the values to assign to the columns
		 * @return the <code>UPDATE</code> statement ending with the assignment
		 * <code><strong>(<em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>}) =
		 * (<em>tuple<sub>0</sub></em>{, <em>tuple<sub>i</sub></em>})</strong></code>
		 */
		NotFiltered set(Iterable<? extends CharSequence> columnNames, SqlExpression tuple);

		/**
		 * Adds a <code>SET</code> group column assignment to the <code>UPDATE</code> statement.
		 * @param columnNames the names of the columns to assign the values to
		 * @param tuple the row expression containing the values to assign to the columns
		 * @return the <code>UPDATE</code> statement ending with the assignment
		 * <code><strong>(<em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>}) =
		 * (<em>tuple<sub>0</sub></em>{, <em>tuple<sub>i</sub></em>})</strong></code>
		 */
		default NotFiltered set(CharSequence[] columnNames, SqlExpression tuple) {
			return set(Arrays.asList(columnNames), tuple);
		}
	}

	private abstract static class InitialStage implements AssignmentList {

		@Override
		public NotFiltered set(CharSequence columnName, SqlExpression value) {
			return new NotFiltered(this, new SingleAssignment(columnName, value));
		}

		@Override
		public NotFiltered set(Iterable<? extends CharSequence> columnNames, SqlExpression tuple) {
			return new NotFiltered(this, new TupleAssignment(columnNames, tuple));
		}

		abstract void appendTo(StringBuilder receptacle);

		abstract InitialStage copy();
	}

	/**
	 * Represents a prefix of an <code>UPDATE</code> statement having the form<br>
	 * <code><strong>[WITH <em>cte</em>] UPDATE [OR REPLACE|ROLLBACK|ABORT|FAIL|IGNORE]
	 * INTO [<em>schemaName</em>.]<em>tableName</em></strong></code>.
	 */
	public static final class Stub extends InitialStage {

		private final CommonTableExpression cte;

		private final UpdateVerb verb;

		private final CharSequence schemaName; // nullable

		private final CharSequence tableName;

		Stub(CommonTableExpression cte, UpdateVerb verb, CharSequence schemaName, CharSequence tableName) {
			this.cte = cte;
			this.verb = verb;
			this.schemaName = schemaName;
			this.tableName = tableName;
		}

		/**
		 * Adds an <code>INDEXED BY</code> clause forcing the use of a the specified index.
		 * @param indexName the name of the index to force the use of
		 * @return the prefix of an <code>UPDATE</code> statement having the form<br>
		 * <code>[WITH <em>cte</em>] UPDATE [OR REPLACE|ROLLBACK|ABORT|FAIL|IGNORE]
		 * INTO [<em>schemaName</em>.]<em>tableName</em> <strong>INDEXED BY <em>indexName</em></strong></code>
		 */
		public WithIndexDirective indexedBy(CharSequence indexName) {
			return new WithIndexDirective(this, indexName);
		}

		/**
		 * Adds a <code>NOT INDEXED</code> clause forbidding index use.
		 * @return the prefix of an <code>UPDATE</code> statement having the form<br>
		 * <code>[WITH <em>cte</em>] UPDATE [OR REPLACE|ROLLBACK|ABORT|FAIL|IGNORE]
		 * INTO [<em>schemaName</em>.]<em>tableName</em> <strong>NOT INDEXED</strong></code>
		 */
		public WithIndexDirective notIndexed() {
			return new WithIndexDirective(this, null);
		}

		Stub copy() {
			CommonTableExpression cteCopy = cte == null ? null : cte.copy();
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();

			return cteCopy == cte && schemaNameCopy == schemaName && tableNameCopy == tableName
				? this : new Stub(cteCopy, verb, schemaNameCopy, tableNameCopy);
		}

		void appendTo(StringBuilder receptacle) {
			if (cte != null) {
				cte.appendTo(receptacle);
				receptacle.append(' ');
			}
			receptacle.append(verb.toString());
			receptacle.append(' ');
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
		}
	}

	/**
	 * Represents a prefix of an <code>UPDATE</code> statement ending with an <code>INDEXED BY</code>
	 * or a <code>NOT INDEXED</code> directive.
	 */
	public static final class WithIndexDirective extends InitialStage {

		private final Stub stub;

		private final CharSequence indexName; // may be null

		WithIndexDirective(Stub stub, CharSequence indexName) {
			this.stub = stub;
			this.indexName = indexName;
		}

		WithIndexDirective copy() {
			Stub stubCopy = stub.copy();
			CharSequence indexNameCopy = indexName == null ? null : indexName.toString();

			return stubCopy == stub && indexNameCopy == indexName
				? this : new WithIndexDirective(stubCopy, indexNameCopy);
		}

		void appendTo(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			TableWithIndex.appendIndexDirective(receptacle, indexName);
		}
	}

	/**
	 * Represents an <code>UPDATE</code> statement without a <code>WHERE</code> clause
	 * nor a row count limitation clause.<br>
	 * This is a complete SQL statement.
	 */
	public static final class NotFiltered implements NotLimited, AssignmentList {

		private final InitialStage initial;

		private final NotFiltered previous;

		private final Assignment assignment;

		NotFiltered(InitialStage initial, Assignment assignment) {
			this(initial, null, assignment);
		}

		NotFiltered(NotFiltered previous, Assignment assignment) {
			this(null, previous, assignment);
		}

		private NotFiltered(InitialStage initial, NotFiltered previous, Assignment assignment) {
			this.initial = initial;
			this.previous = previous;
			this.assignment = assignment;
		}

		@Override
		public NotFiltered set(CharSequence columnName, SqlExpression value) {
			return new NotFiltered(this, new SingleAssignment(columnName, value));
		}

		@Override
		public NotFiltered set(Iterable<? extends CharSequence> columnNames, SqlExpression tuple) {
			return new NotFiltered(this, new TupleAssignment(columnNames, tuple));
		}

		/**
		 * Adds a filtering condition (a <code>WHERE</code> clause) to the <code>UPDATE</code> statement.<br>
		 * The result is a complete <code>UPDATE</code> statement.
		 * @param condition the filtering condition expression
		 * @return the <code>UPDATE</code> statement ending with a <code>WHERE</code> clause
		 */
		public Filtered where(SqlExpression condition) {
			return new Filtered(this, condition);
		}

		@Override
		public NotFiltered copy() {
			InitialStage initialCopy = initial == null ? null : initial.copy();
			NotFiltered previousCopy = previous == null ? null : previous.copy();
			Assignment assignmentCopy = assignment.copy();

			return initialCopy == initial && previousCopy == previous && assignmentCopy == assignment
				? this : new NotFiltered(initialCopy, previousCopy, assignmentCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			List<NotFiltered> chain = new ArrayList<>();
			NotFiltered current = this;
			while (current != null) {
				chain.add(current);
				current = current.previous;
			}

			chain.get(chain.size() - 1).initial.appendTo(receptacle);
			receptacle.append(" SET ");
			for (int i = chain.size() - 1; i >= 0; i--) {
				chain.get(i).assignment.appendTo(receptacle);
				if (i != 0) {
					receptacle.append(", ");
				}
			}
		}
	}

	/**
	 * Represents an <code>UPDATE</code> statement ending with a <code>WHERE</code> clause
	 * but without a row count limitation clause.<br>
	 * This is a complete SQL statement.
	 */
	public static final class Filtered implements NotLimited {

		private final NotFiltered previous;

		private final SqlExpression condition;

		Filtered(NotFiltered previous, SqlExpression condition) {
			this.previous = previous;
			this.condition = condition;
		}

		@Override
		public Filtered copy() {
			NotFiltered previousCopy = previous.copy();
			SqlExpression conditionCopy = condition.copy();

			return previousCopy == previous && conditionCopy == condition
				? this : new Filtered(previousCopy, conditionCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			receptacle.append(" WHERE ");
			condition.appendTo(receptacle);
		}
	}

	/**
	 * Represents a partial <code>UPDATE</code> statement ending with an <code>ORDER BY</code>
	 * clause. It must be supplemented with a <code>LIMIT</code> clause to get a complete SQL statement.
	 */
	public static final class Ordered implements Sortable {

		private final NotLimited notLimited;

		private final Ordered previous;

		private final Sort sort;

		Ordered(NotLimited notLimited, Sort sort) {
			this(notLimited, null, sort);
		}

		Ordered(Ordered previous, Sort sort) {
			this(null, previous, sort);
		}

		private Ordered(NotLimited notLimited, Ordered previous, Sort sort) {
			this.notLimited = notLimited;
			this.previous = previous;
			this.sort = sort;
		}

		@Override
		public Ordered orderBy(SqlExpression sortingKey) {
			return new Ordered(this, new Sort(sortingKey, null));
		}

		@Override
		public Ordered orderBy(SqlExpression sortingKey, SortingOrder order) {
			return new Ordered(this, new Sort(sortingKey, order));
		}

		@Override
		public LimitedNoOffset limit(SqlExpression limitExpression) {
			return new LimitedNoOffset(this, new IntegralValueClause.Limit(limitExpression));
		}

		@Override
		public LimitedNoOffset limit(long maxRows) {
			return new LimitedNoOffset(this, new IntegralValueClause.Limit(maxRows));
		}

		Ordered copy() {
			NotLimited notLimitedCopy = notLimited == null ? null : notLimited.copy();
			Ordered previousCopy = previous == null ? null : previous.copy();
			Sort sortCopy = sort.copy();

			return notLimitedCopy == notLimited && previousCopy == previous && sortCopy == sort
				? this : new Ordered(notLimitedCopy, previousCopy, sortCopy);
		}

		void appendTo(StringBuilder receptacle) {
			List<Ordered> chain = new ArrayList<>();
			Ordered current = this;
			while (current != null) {
				chain.add(current);
				current = current.previous;
			}

			chain.get(chain.size() - 1).notLimited.build(receptacle);
			receptacle.append(" ORDER BY ");
			for (int i = chain.size() - 1; i >= 0; i--) {
				chain.get(i).sort.appendTo(receptacle);
				if (i != 0) {
					receptacle.append(", ");
				}
			}
		}
	}

	/**
	 * Represents an <code>UPDATE</code> statement ending with a <code>LIMIT</code> clause
	 * without an <code>OFFSET</code> part.
	 */
	public static final class LimitedNoOffset implements TriggerStatement {

		private final NotLimited previousNotLimited;

		private final Ordered previousOrdered;

		private final IntegralValueClause.Limit limit;

		LimitedNoOffset(NotLimited previous, IntegralValueClause.Limit limit) {
			this(previous, null, limit);
		}

		LimitedNoOffset(Ordered previous, IntegralValueClause.Limit limit) {
			this(null, previous, limit);
		}

		private LimitedNoOffset(
			NotLimited previousNotLimited, Ordered previousOrdered, IntegralValueClause.Limit limit) {

			this.previousNotLimited = previousNotLimited;
			this.previousOrdered = previousOrdered;
			this.limit = limit;
		}

		/**
		 * Adds an <code>OFFSET</code> clause defining the limited range of rows to be updated.<br>
		 * The result is a complete SQL statement.
		 * @param offsetExpression the expression determining the offset
		 * @return the <code>UPDATE</code> statement finalized with a <code>LIMIT .. OFFSET</code> clause
		 */
		public LimitedWithOffset offset(SqlExpression offsetExpression) {
			return new LimitedWithOffset(this, new IntegralValueClause.Offset(offsetExpression));
		}

		/**
		 * Adds an <code>OFFSET</code> clause defining the limited range of rows to be updated.<br>
		 * The result is a complete SQL statement.
		 * @param offset the number determining the offset
		 * @return the <code>UPDATE</code> statement finalized with a <code>LIMIT .. OFFSET</code> clause
		 */
		public LimitedWithOffset offset(long offset) {
			return new LimitedWithOffset(this, new IntegralValueClause.Offset(offset));
		}

		@Override
		public LimitedNoOffset copy() {
			NotLimited previousNotLimitedCopy = previousNotLimited == null ? null : previousNotLimited.copy();
			Ordered previousOrderedCopy = previousOrdered == null ? null : previousOrdered.copy();
			IntegralValueClause.Limit limitCopy = limit.copy();

			return previousNotLimitedCopy == previousNotLimited
				&& previousOrderedCopy == previousOrdered && limitCopy == limit
					? this : new LimitedNoOffset(previousNotLimitedCopy, previousOrderedCopy, limitCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			if (previousNotLimited != null) {
				previousNotLimited.build(receptacle);
			} else {
				previousOrdered.appendTo(receptacle);
			}
			limit.appendTo(receptacle);
		}
	}

	/**
	 * Represents an <code>UPDATE</code> statement having a <code>LIMIT .. OFFSET</code> clause.<br>
	 * This is a complete SQL statement.
	 */
	public static final class LimitedWithOffset implements TriggerStatement {

		private final LimitedNoOffset previous;

		private final IntegralValueClause.Offset offset;

		LimitedWithOffset(LimitedNoOffset previous, IntegralValueClause.Offset offset) {
			this.previous = previous;
			this.offset = offset;
		}

		@Override
		public LimitedWithOffset copy() {
			LimitedNoOffset previousCopy = previous.copy();
			IntegralValueClause.Offset offsetCopy = offset.copy();

			return previousCopy == previous && offsetCopy == offset
				? this : new LimitedWithOffset(previousCopy, offsetCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			offset.appendTo(receptacle);
		}
	}

	private abstract static class Assignment {

		abstract void appendTo(StringBuilder receptacle);

		abstract Assignment copy();
	}

	private static final class SingleAssignment extends Assignment {

		private final CharSequence columnName;

		private final SqlExpression value;

		SingleAssignment(CharSequence columnName, SqlExpression value) {
			this.columnName = columnName;
			this.value = value;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			SqliteUtilities.appendQuotedName(receptacle, columnName);
			receptacle.append(" = ");
			value.appendTo(receptacle);
		}

		@Override
		public SingleAssignment copy() {
			CharSequence columnNameCopy = columnName.toString();
			SqlExpression valueCopy = value.copy();

			return columnNameCopy == columnName && valueCopy == value
				? this : new SingleAssignment(columnNameCopy, valueCopy);
		}
	}

	private static final class TupleAssignment extends Assignment {

		private final Iterable<? extends CharSequence> columnNames;

		private final SqlExpression tuple;

		TupleAssignment(Iterable<? extends CharSequence> columnNames, SqlExpression tuple) {
			this.columnNames = columnNames;
			this.tuple = tuple;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append('(');
			SqliteUtilities.appendQuotedDelimited(receptacle, columnNames);
			receptacle.append(") = ");
			tuple.appendTo(receptacle);
		}

		@Override
		public TupleAssignment copy() {
			Iterable<? extends CharSequence> columnNamesCopy =
				ReadonlyIterable.of(columnNames, CharSequence::toString);
			SqlExpression tupleCopy = tuple.copy();

			return columnNamesCopy == columnNames && tupleCopy == tuple
				? this : new TupleAssignment(columnNamesCopy, tupleCopy);
		}
	}

	private Update() { }
}
