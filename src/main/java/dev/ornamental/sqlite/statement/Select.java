package dev.ornamental.sqlite.statement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * This class has no functionality of its own. It is destined for grouping the classes pertaining
 * to <code>SELECT</code> statements.
 */
public final class Select {

	/**
	 * This interface is implemented by the classes representing <code>SELECT</code>
	 * statements having no <code>LIMIT</code> clause.<br>
	 * All the implementations are complete SQL statements and expressions.
	 */
	public interface NotLimited extends SelectStatement {

		@Override
		NotLimited copy();

		/**
		 * Adds a <code>LIMIT</code> clause to the statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param maxRows the maximum number of rows to be fetched
		 * @return the <code>SELECT</code> statement with a <code>LIMIT</code> clause
		 * without the <code>OFFSET</code> part
		 */
		default LimitedNoOffset limit(long maxRows) {
			return new LimitedNoOffset(this, new IntegralValueClause.Limit(maxRows));
		}

		/**
		 * Adds a <code>LIMIT</code> clause to the statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param limitExpression the expression determining the maximum number of rows to be fetched
		 * @return the <code>SELECT</code> statement with a <code>LIMIT</code> clause
		 * without the <code>OFFSET</code> part
		 */
		default LimitedNoOffset limit(SqlExpression limitExpression) {
			return new LimitedNoOffset(this, new IntegralValueClause.Limit(limitExpression));
		}
	}

	/**
	 * This interface may be used to add <code>ORDER BY</code> sorting keys
	 * in a manner uniform both for a <code>SELECT</code> statement not yet having
	 * an <code>ORDER BY</code> clause and a <code>SELECT</code> statement already having
	 * at least one sorting key specified.
	 */
	public interface Sortable {

		/**
		 * Adds an <code>ORDER BY</code> sorting term with the default sorting order
		 * (creating an <code>ORDER BY</code> clause is this is the first sorting term).<br>
		 * The result is a complete SQL statement and expression.
		 * @param sortingKey the expression to sort by the value of
		 * @return the <code>SELECT</code> statement ending with a <code>ORDER BY</code>
		 * clause whose last sorting term is the one specified as the parameter of this method
		 */
		Sorted orderBy(SqlExpression sortingKey);

		/**
		 * Adds an <code>ORDER BY</code> sorting term with the specified sorting order
		 * (creating an <code>ORDER BY</code> clause is this is the first sorting term).<br>
		 * The result is a complete SQL statement and expression.
		 * @param sortingKey the expression to sort by the value of
		 * @param order the sorting order
		 * @return the <code>SELECT</code> statement ending with a <code>ORDER BY</code>
		 * clause whose last sorting term is the one defined by the parameters of this method
		 */
		Sorted orderBy(SqlExpression sortingKey, SortingOrder order);
	}

	/**
	 * This interface is implemented by the classes representing <code>SELECT</code>
	 * statements having no <code>ORDER BY</code> clause.<br>
	 * All the implementations are complete SQL statements and expressions.
	 */
	public interface NotSorted extends NotLimited, Sortable {

		@Override
		NotSorted copy();

		/**
		 * Determines if the <code>SELECT</code> statement is prepended with a common table
		 * expression (a <code>WITH</code> clause). If so, it cannot be used in some contexts,
		 * for instance, with compound operators like <code>UNION</code>.
		 * @return {@literal true} if and only if the statement starts with a <code>WITH</code> clause
		 */
		boolean hasCte();

		@Override
		default Sorted orderBy(SqlExpression sortingKey) {
			return orderBy(sortingKey, null);
		}

		@Override
		default Sorted orderBy(SqlExpression sortingKey, SortingOrder order) { // accepts null as sorting order
			return new Sorted(this, new Sort(sortingKey, order));
		}

		/**
		 * Applies the <code>UNION</code> compound operator to this <code>SELECT</code> statement
		 * and another <code>SELECT</code> (or <code>VALUES</code>) statement with no
		 * <code>ORDER BY</code> or <code>LIMIT</code> clause.<br>
		 * <strong>Note</strong> that if the passed statement already is a compound statement, the order
		 * of compound operations may be not the one expected as SQLite has no compound
		 * statement precedence or ability to reorder the compound operator evaluation order
		 * using parentheses.<br>
		 * The result is a complete SQL statement and expression.
		 * @param select the <code>SELECT</code> (or <code>VALUES</code>) statement
		 * @return the compound <code>SELECT</code> statement having the form<br>
		 * <code><strong><em>selectOperand<sub>L</sub></em> UNION
		 * <em>selectOperand<sub>R</sub></em></strong></code><br>
		 * where <code><em>selectOperand<sub>L</sub></em></code> is this statement and
		 * <code><em>selectOperand<sub>R</sub></em></code> is the one passed as the parameter
		 */
		default CompoundSet union(NotSorted select) {
			if (select.hasCte()) {
				throw new IllegalArgumentException("The argument must not have a common table expression.");
			}
			return new CompoundSet(this, select, CompoundOperator.UNION);
		}

		/**
		 * Applies the <code>UNION ALL</code> compound operator to this <code>SELECT</code> statement
		 * and another <code>SELECT</code> (or <code>VALUES</code>) statement with no
		 * <code>ORDER BY</code> or <code>LIMIT</code> clause.<br>
		 * <strong>Note</strong> that if the passed statement already is a compound statement, the order
		 * of compound operations may be not the one expected as SQLite has no compound
		 * statement precedence or ability to reorder the compound operator evaluation order
		 * using parentheses.<br>
		 * The result is a complete SQL statement and expression.
		 * @param select the <code>SELECT</code> (or <code>VALUES</code>) statement
		 * @return the compound <code>SELECT</code> statement having the form<br>
		 * <code><strong><em>selectOperand<sub>L</sub></em> UNION ALL
		 * <em>selectOperand<sub>R</sub></em></strong></code><br>
		 * where <code><em>selectOperand<sub>L</sub></em></code> is this statement and
		 * <code><em>selectOperand<sub>R</sub></em></code> is the one passed as the parameter
		 */
		default CompoundSet unionAll(NotSorted select) {
			if (select.hasCte()) {
				throw new IllegalArgumentException("The argument must not have a common table expression.");
			}
			return new CompoundSet(this, select, CompoundOperator.UNION_ALL);
		}

		/**
		 * Applies the <code>INTERSECT</code> compound operator to this <code>SELECT</code> statement
		 * and another <code>SELECT</code> (or <code>VALUES</code>) statement with no
		 * <code>ORDER BY</code> or <code>LIMIT</code> clause.<br>
		 * <strong>Note</strong> that if the passed statement already is a compound statement, the order
		 * of compound operations may be not the one expected as SQLite has no compound
		 * statement precedence or ability to reorder the compound operator evaluation order
		 * using parentheses.<br>
		 * The result is a complete SQL statement and expression.
		 * @param select the <code>SELECT</code> (or <code>VALUES</code>) statement
		 * @return the compound <code>SELECT</code> statement having the form<br>
		 * <code><strong><em>selectOperand<sub>L</sub></em> INTERSECT
		 * <em>selectOperand<sub>R</sub></em></strong></code><br>
		 * where <code><em>selectOperand<sub>L</sub></em></code> is this statement and
		 * <code><em>selectOperand<sub>R</sub></em></code> is the one passed as the parameter
		 */
		default CompoundSet intersect(NotSorted select) {
			if (select.hasCte()) {
				throw new IllegalArgumentException("The argument must not have a common table expression.");
			}
			return new CompoundSet(this, select, CompoundOperator.INTERSECT);
		}

		/**
		 * Applies the <code>EXCEPT</code> compound operator to this <code>SELECT</code> statement
		 * and another <code>SELECT</code> (or <code>VALUES</code>) statement with no
		 * <code>ORDER BY</code> or <code>LIMIT</code> clause.<br>
		 * <strong>Note</strong> that if the passed statement already is a compound statement, the order
		 * of compound operations may be not the one expected as SQLite has no compound
		 * statement precedence or ability to reorder the compound operator evaluation order
		 * using parentheses.<br>
		 * The result is a complete SQL statement and expression.
		 * @param select the <code>SELECT</code> (or <code>VALUES</code>) statement
		 * @return the compound <code>SELECT</code> statement having the form<br>
		 * <code><strong><em>selectOperand<sub>L</sub></em> EXCEPT
		 * <em>selectOperand<sub>R</sub></em></strong></code><br>
		 * where <code><em>selectOperand<sub>L</sub></em></code> is this statement and
		 * <code><em>selectOperand<sub>R</sub></em></code> is the one passed as the parameter
		 */
		default CompoundSet except(NotSorted select) {
			if (select.hasCte()) {
				throw new IllegalArgumentException("The argument must not have a common table expression.");
			}
			return new CompoundSet(this, select, CompoundOperator.EXCEPT);
		}

		/**
		 * Initializes construction of the <code>UNION</code> compound statement
		 * having this statement as the left operand.
		 * @return the <code>UNION</code> compound statement builder whose left
		 * operand is this <code>SELECT</code> (or <code>VALUE</code>) statement
		 */
		default CompoundSetBuilder union() {
			return new CompoundSetBuilder(this, CompoundOperator.UNION);
		}

		/**
		 * Initializes construction of the <code>UNION ALL</code> compound statement
		 * having this statement as the left operand.
		 * @return the <code>UNION ALL</code> compound statement builder whose left
		 * operand is this <code>SELECT</code> (or <code>VALUE</code>) statement
		 */
		default CompoundSetBuilder unionAll() {
			return new CompoundSetBuilder(this, CompoundOperator.UNION_ALL);
		}

		/**
		 * Initializes construction of the <code>INTERSECT</code> compound statement
		 * having this statement as the left operand.
		 * @return the <code>INTERSECT</code> compound statement builder whose left
		 * operand is this <code>SELECT</code> (or <code>VALUE</code>) statement
		 */
		default CompoundSetBuilder intersect() {
			return new CompoundSetBuilder(this, CompoundOperator.INTERSECT);
		}

		/**
		 * Initializes construction of the <code>UNION</code> compound statement
		 * having this statement as the left operand.
		 * @return the <code>EXCEPT</code> compound statement builder whose left
		 * operand is this <code>SELECT</code> (or <code>VALUE</code>) statement
		 */
		default CompoundSetBuilder except() {
			return new CompoundSetBuilder(this, CompoundOperator.EXCEPT);
		}
	}

	/**
	 * This interface is implemented by the classes representing <code>SELECT</code>
	 * statements having no <code>GROUP BY</code> nor <code>LIMIT</code> clause.<br>
	 * All the implementations are complete SQL statements and expressions.
	 */
	public interface NotGrouped extends NotSorted {

		@Override
		NotGrouped copy();

		/**
		 * Adds a <code>GROUP BY</code> clause to this statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param groupingKeys the set of expressions to perform the grouping by
		 * @return the <code>SELECT</code> statement ending with<br>
		 * <code><strong>GROUP BY <em>groupingKey<sub>0</sub></em>{,
		 * <em>groupingKey<sub>i</sub></em>}</strong></code>
		 */
		default GroupedNotFiltered groupBy(SqlExpression... groupingKeys) {
			return new GroupedNotFiltered(this, Arrays.asList(groupingKeys));
		}

		/**
		 * Adds a <code>GROUP BY</code> clause to this statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param groupingKeys the set of expressions to perform the grouping by
		 * @return the <code>SELECT</code> statement ending with<br>
		 * <code><strong>GROUP BY <em>groupingKey<sub>0</sub></em>{,
		 * <em>groupingKey<sub>i</sub></em>}</strong></code>
		 */
		default GroupedNotFiltered groupBy(Iterable<? extends SqlExpression> groupingKeys) {
			return new GroupedNotFiltered(this, groupingKeys);
		}
	}

	/**
	 * This interface is implemented by the classes representing <code>SELECT</code>
	 * statements having no <code>WHERE</code>, <code>GROUP BY</code>, nor <code>LIMIT</code>
	 * clause.<br>
	 * All the implementations are complete SQL statements and expressions.
	 */
	public interface NotFiltered extends NotGrouped {

		@Override
		NotFiltered copy();

		/**
		 * Adds a filtering <code>WHERE</code> clause to the statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param expression the filtering condition expression
		 * @return the <code>SELECT</code> statement ending with<br>
		 * <code><strong>WHERE <em>expression</em></strong></code>
		 */
		default Filtered where(SqlExpression expression) {
			return new Filtered(this, expression);
		}
	}

	/**
	 * Represents the simplest <code>SELECT</code> expression having
	 * no clauses but <code>SELECT</code>.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class NoSource implements NotFiltered {

		private final boolean distinct;

		private final NotSorted previous;

		private final CompoundOperator operator;

		private final CommonTableExpression cte;

		private final Iterable<? extends ResultElement> columns;

		private final boolean hasCte;

		NoSource(boolean distinct, Iterable<? extends ResultElement> columns) {
			this(null, null, null, distinct, columns);
		}

		NoSource(CommonTableExpression cte, boolean distinct, Iterable<? extends ResultElement> columns) {
			this(null, null, cte, distinct, columns);
		}

		NoSource(NotSorted previous, CompoundOperator operator,
			boolean distinct, Iterable<? extends ResultElement> columns) {

			this(previous, operator, null, distinct, columns);
		}

		private NoSource(NotSorted previous, CompoundOperator operator, CommonTableExpression cte,
			boolean distinct, Iterable<? extends ResultElement> columns) {

			this.previous = previous;
			this.operator = operator;
			this.cte = cte;
			this.distinct = distinct;
			this.columns = columns;
			this.hasCte = cte != null || previous != null && previous.hasCte();
		}

		@Override
		public boolean hasCte() {
			return hasCte;
		}

		/**
		 * Adds a <code>FROM</code> clause to the statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param from the table expression composed of one or more table references
		 * and subqueries, serving as the source for the <code>SELECT</code> statement
		 * @return the <code>SELECT</code> statement ending with a <code>FROM</code> clause
		 */
		public WithSource from(TableExpression from) {
			return new WithSource(this, from);
		}

		@Override
		public NoSource copy() throws IllegalStateException {
			NotSorted previousCopy = previous == null ? null : previous.copy();
			CommonTableExpression cteCopy = cte == null ? null : cte.copy();
			Iterable<? extends ResultElement> columnsCopy = ReadonlyIterable.of(columns, ResultElement::copy);

			return previousCopy == previous && cteCopy == cte && columnsCopy == columns
				? this : new NoSource(previousCopy, operator, cteCopy, distinct, columnsCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			if (previous != null) {
				previous.build(receptacle);
				receptacle.append(' ').append(operator.toString()).append(' ');
			} else if (cte != null) {
				cte.appendTo(receptacle);
				receptacle.append(' ');
			}

			receptacle.append("SELECT ");
			if (distinct) {
				receptacle.append("DISTINCT ");
			}

			Iterator<? extends ResultElement> columnIterator = columns.iterator();
			if (!columnIterator.hasNext()) {
				throw new IllegalStateException("At least one column must be specified in the SELECT clause.");
			}
			ResultElement last = columnIterator.next();
			while (columnIterator.hasNext()) {
				last.appendTo(receptacle);
				receptacle.append(", ");
				last = columnIterator.next();
			}
			last.appendTo(receptacle);
		}
	}

	/**
	 * Represents the <code>SELECT</code> expression having <code>FROM</code>
	 * as the last clause.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class WithSource implements NotFiltered {

		private final NoSource previous;

		private final TableExpression from;

		WithSource(NoSource previous, TableExpression from) {
			this.previous = previous;
			this.from = from;
		}

		@Override
		public boolean hasCte() {
			return previous.hasCte();
		}

		@Override
		public WithSource copy() {
			NoSource previousCopy = previous.copy();
			TableExpression fromCopy = from.copy();

			return previousCopy == previous && fromCopy == from
				? this : new WithSource(previousCopy, fromCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			receptacle.append(" FROM ");
			from.appendTo(receptacle);
		}
	}

	/**
	 * Represents two <code>SELECT</code> statements joint with a compound operator:
	 * <code>UNION</code>, <code>UNION ALL</code>, <code>INTERSECT</code>, or <code>EXCEPT</code>.<br>
	 * Note that SQLite has no compound operator precedence nor there is a feature of reordering
	 * the composition sequence using parentheses, so all the compound operators are always evaluated
	 * from left to right.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class CompoundSet implements NotSorted {

		private final NotSorted previous;

		private final NotSorted current;

		private final CompoundOperator operator;

		private final boolean hasCte;

		CompoundSet(NotSorted previous, NotSorted current, CompoundOperator operator) {
			this.previous = previous;
			this.current = current;
			this.operator = operator;
			this.hasCte = previous.hasCte();
		}

		@Override
		public boolean hasCte() {
			return hasCte;
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			receptacle.append(' ').append(operator.toString()).append(' ');
			current.build(receptacle);
		}

		@Override
		public CompoundSet copy() throws IllegalStateException {
			NotSorted previousCopy = previous.copy();
			NotSorted currentCopy = current.copy();

			return previousCopy == previous && currentCopy == current
				? this : new CompoundSet(previous.copy(), current.copy(), operator);
		}
	}

	/**
	 * Represents a compound <code>SELECT</code> statement with yet undefined right
	 * operand of the compound operator.
	 */
	public static final class CompoundSetBuilder extends ValueProducer {

		private final NotSorted previous;

		private final CompoundOperator operator;

		CompoundSetBuilder(NotSorted previous, CompoundOperator operator) {
			this.previous = previous;
			this.operator = operator;
		}

		/**
		 * Appends a <code>SELECT</code> statement with the specified result columns.
		 * This statement may be further supplemented with other clauses: <code>FROM</code>,
		 * <code>GROUP BY .. [HAVING]</code>, <code>ORDER BY</code>, and <code>LIMIT .. [OFFSET]</code>.<br>
		 * The result is a complete SQL statement and expression.
		 * @param columns the columns to include in the <code>SELECT</code> statement output
		 * @return the statement being a composition of the previous <code>SELECT</code>
		 * (or <code>VALUES</code>) statement and of the new <code>SELECT</code> statement
		 * joint with a previously specified compound operator (<code>UNION</code>, <code>UNION ALL</code>,
		 * <code>INTERSECT</code>, or <code>EXCEPT</code>)
		 */
		public NoSource select(ResultElement... columns) {
			return new NoSource(previous, operator, false, Arrays.asList(columns));
		}

		/**
		 * Appends a <code>SELECT DISTINCT</code> statement with the specified result columns.
		 * This statement may be further supplemented with other clauses: <code>FROM</code>,
		 * <code>GROUP BY .. [HAVING]</code>, <code>ORDER BY</code>, and <code>LIMIT .. [OFFSET]</code>.<br>
		 * The result is a complete SQL statement and expression.
		 * @param columns the columns to include in the <code>SELECT</code> statement output
		 * @return the statement being a composition of the previous <code>SELECT</code>
		 * (or <code>VALUES</code>) statement and of the new <code>SELECT DISTINCT</code> statement
		 * joint with a previously specified compound operator (<code>UNION</code>, <code>UNION ALL</code>,
		 * <code>INTERSECT</code>, or <code>EXCEPT</code>)
		 */
		public NoSource selectDistinct(ResultElement... columns) {
			return new NoSource(previous, operator, true, Arrays.asList(columns));
		}

		@Override
		public Values values(int... values) {
			return new IntegerColumn(previous, operator, values, false);
		}

		@Override
		public Values values(long... values) {
			return new LongColumn(previous, operator, values, false);
		}

		@Override
		public Values values(float... values) {
			return new FloatColumn(previous, operator, values, false);
		}

		@Override
		public Values values(double... values) {
			return new DoubleColumn(previous, operator, values, false);
		}

		@Override
		public Values stringValues(Iterable<? extends CharSequence> values) {
			return new TextColumn(previous, operator, values);
		}

		@Override
		public Values numericValues(Iterable<? extends Number> values) {
			return new NumericColumn(previous, operator, values);
		}

		@Override
		public Values blobValues(Iterable<byte[]> values) {
			return new BlobColumn(previous, operator, values);
		}

		@Override
		public Values values(Iterable<? extends RowExpression> rows) {
			return new Rows(previous, operator, rows);
		}
	}

	/**
	 * Represents a <code>SELECT</code> statement with a filtering <code>WHERE</code> clause
	 * but no <code>GROUP BY</code> or <code>LIMIT</code> clause.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class Filtered implements NotGrouped {

		private final NotFiltered previous;

		private final SqlExpression condition;

		private final boolean hasCte;

		Filtered(NotFiltered previous, SqlExpression condition) {
			this.previous = previous;
			this.condition = condition;
			this.hasCte = previous.hasCte();
		}

		@Override
		public boolean hasCte() {
			return hasCte;
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
	 * Represents a <code>SELECT</code> statement with a <code>GROUP BY</code> clause
	 * but no <code>HAVING</code>, <code>ORDER BY</code>, or <code>LIMIT</code> clause.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class GroupedNotFiltered implements NotSorted {

		private final NotGrouped previous;

		private final Iterable<? extends SqlExpression> groupings; // allowed to be empty

		private final boolean hasCte;

		GroupedNotFiltered(NotGrouped previous, Iterable<? extends SqlExpression> groupings) {
			this.previous = previous;
			this.groupings = groupings;
			this.hasCte = previous.hasCte();
		}

		/**
		 * Adds a filtering <code>HAVING</code> clause to the statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param condition the group filtering condition expression
		 * @return the <code>SELECT</code> statement ending
		 * with a <code>GROUP BY .. HAVING</code> clause
		 */
		public GroupedFiltered having(SqlExpression condition) {
			return new GroupedFiltered(this, condition);
		}

		@Override
		public boolean hasCte() {
			return hasCte;
		}

		@Override
		public GroupedNotFiltered copy() {
			NotGrouped previousCopy = previous.copy();
			Iterable<SqlExpression> groupingsCopy = ReadonlyIterable.of(groupings, SqlExpression::copy);

			return previousCopy == previous && groupingsCopy == groupings
				? this : new GroupedNotFiltered(previousCopy, groupingsCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			Iterator<? extends SqlExpression> iterator = groupings.iterator();
			if (iterator.hasNext()) {
				receptacle.append(" GROUP BY ");
				SqlExpression last = iterator.next();
				while (iterator.hasNext()) {
					last.appendTo(receptacle);
					receptacle.append(", ");
					last = iterator.next();
				}
				last.appendTo(receptacle);
			}
		}
	}

	/**
	 * Represents a <code>SELECT</code> statement ending with a <code>GROUP BY .. HAVING</code> clause.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class GroupedFiltered implements NotSorted {

		private final GroupedNotFiltered previous;

		private final SqlExpression condition;

		private final boolean hasCte;

		GroupedFiltered(GroupedNotFiltered previous, SqlExpression condition) {
			this.previous = previous;
			this.condition = condition;
			this.hasCte = previous.hasCte();
		}

		@Override
		public boolean hasCte() {
			return hasCte;
		}

		@Override
		public GroupedFiltered copy() {
			GroupedNotFiltered previousCopy = previous.copy();
			SqlExpression conditionCopy = condition.copy();

			return previousCopy == previous && conditionCopy == condition
				? this : new GroupedFiltered(previousCopy, conditionCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			receptacle.append(" HAVING ");
			condition.appendTo(receptacle);
		}
	}

	/**
	 * Represents a <code>SELECT</code> statement ending with an <code>ORDER BY</code>
	 * clause.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class Sorted implements NotLimited, Sortable {

		private final NotSorted previousNotSorted;

		private final Sorted previousSorted;

		private final Sort sort;

		Sorted(NotSorted previousNotSorted, Sort sort) {
			this(previousNotSorted, null, sort);
		}

		Sorted(Sorted previousSorted, Sort sort) {
			this(null, previousSorted, sort);
		}

		private Sorted(NotSorted previousNotSorted, Sorted previousSorted, Sort sort) {
			this.previousNotSorted = previousNotSorted;
			this.previousSorted = previousSorted;
			this.sort = sort;
		}

		@Override
		public Sorted orderBy(SqlExpression sortingKey) {
			return new Sorted(this, new Sort(sortingKey, null));
		}

		@Override
		public Sorted orderBy(SqlExpression sortingKey, SortingOrder order) {
			return new Sorted(this, new Sort(sortingKey, order));
		}

		@Override
		public Sorted copy() {
			NotSorted previousNotSortedCopy = previousNotSorted == null ? null : previousNotSorted.copy();
			Sorted previousSortedCopy = previousSorted == null ? null : previousSorted.copy();
			Sort sortCopy = sort.copy();

			return
				previousNotSortedCopy == previousNotSorted
				&& previousSortedCopy == previousSorted && sortCopy == sort
					? this : new Sorted(previousNotSortedCopy, previousSortedCopy, sortCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			if (previousNotSorted != null) {
				previousNotSorted.build(receptacle);
				receptacle.append(" ORDER BY ");
			} else {
				previousSorted.build(receptacle);
				receptacle.append(", ");
			}

			sort.appendTo(receptacle);
		}
	}

	/**
	 * Represents a <code>SELECT</code> statement ending with a <code>LIMIT</code>
	 * clause without the <code>OFFSET</code> part.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class LimitedNoOffset implements SelectStatement {

		private final NotLimited previous;

		private final IntegralValueClause.Limit limit;

		LimitedNoOffset(NotLimited previous, IntegralValueClause.Limit limit) {
			this.previous = previous;
			this.limit = limit;
		}

		/**
		 * Adds an <code>OFFSET</code> clause to the statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param offset the number determining the offset
		 * @return the <code>SELECT</code> statement finalized with an <code>OFFSET</code> clause
		 */
		public LimitedWithOffset offset(long offset) {
			return new LimitedWithOffset(this, new IntegralValueClause.Offset(offset));
		}

		/**
		 * Adds an <code>OFFSET</code> clause to the statement.<br>
		 * The result is a complete SQL statement and expression.
		 * @param offsetExpression the expression determining the offset
		 * @return the <code>SELECT</code> statement finalized with an <code>OFFSET</code> clause
		 */
		public LimitedWithOffset offset(SqlExpression offsetExpression) {
			return new LimitedWithOffset(this, new IntegralValueClause.Offset(offsetExpression));
		}

		@Override
		public LimitedNoOffset copy() throws IllegalStateException {
			NotLimited previousCopy = previous.copy();
			IntegralValueClause.Limit limitCopy = limit.copy();

			return previousCopy == previous && limitCopy == limit
				? this : new LimitedNoOffset(previousCopy, limitCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			limit.appendTo(receptacle);
		}
	}

	/**
	 * Represents a <code>SELECT</code> statement ending with a <code>LIMIT .. OFFSET</code>
	 * clause.<br>
	 * This is a complete SQL statement and expression.
	 */
	public static final class LimitedWithOffset implements SelectStatement {

		private final LimitedNoOffset previous;

		private final IntegralValueClause.Offset offset;

		LimitedWithOffset(LimitedNoOffset previous, IntegralValueClause.Offset offset) {
			this.previous = previous;
			this.offset = offset;
		}

		@Override
		public LimitedWithOffset copy() throws IllegalStateException {
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

	/**
	 * Represents a <code>VALUES</code> statement.<br>
	 * This is a complete SQL statement and expression.
	 */
	public abstract static class Values implements NotSorted {

		protected final NotSorted previous;

		protected final CompoundOperator operator;

		protected final CommonTableExpression cte;

		protected final boolean hasCte;

		Values() {
			this(null, null, null);
		}

		Values(CommonTableExpression cte) {
			this(null, null, cte);
		}

		Values(NotSorted previous, CompoundOperator operator) {
			this(previous, operator, null);
		}

		Values(
			NotSorted previous, CompoundOperator operator, CommonTableExpression cte) {

			this.previous = previous;
			this.operator = operator;
			this.cte = cte;
			this.hasCte = cte != null || previous != null && previous.hasCte();
		}

		@Override
		public boolean hasCte() {
			return hasCte;
		}

		@Override
		public void build(StringBuilder receptacle) {
			if (cte != null) {
				cte.appendTo(receptacle);
				receptacle.append(' ');
			} else if (previous != null) {
				previous.build(receptacle);
				receptacle.append(' ').append(operator.toString()).append(' ');
			}

			receptacle.append("VALUES ");
		}

		@Override
		public abstract Values copy();
	}

	abstract static class ValueProducer {

		ValueProducer() { }

		/**
		 * Appends a <code>VALUES</code> statement returning a single column of integral values.<br>
		 * The result is a complete SQL statement and expression.
		 * @param values the values to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
		 */
		public abstract Select.Values values(int... values);

		/**
		 * Appends a <code>VALUES</code> statement returning a single column of integral values.<br>
		 * The result is a complete SQL statement and expression.
		 * @param values the values to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
		 */
		public abstract Select.Values values(long... values);

		/**
		 * Constructs a <code>VALUES</code> statement returning a single column of floating-point values.<br>
		 * The result is a complete SQL statement and expression.
		 * @param values the values to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
		 */
		public abstract Select.Values values(float... values);

		/**
		 * Constructs a <code>VALUES</code> statement returning a single column of floating-point values.<br>
		 * The result is a complete SQL statement and expression.
		 * @param values the values to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
		 */
		public abstract Select.Values values(double... values);

		/**
		 * Constructs a <code>VALUES</code> statement returning a single column of text values.<br>
		 * The result is a complete SQL statement and expression.
		 * @param values the values to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
		 */
		public Select.Values values(CharSequence... values) {
			return stringValues(Arrays.asList(values));
		}

		/**
		 * Constructs a <code>VALUES</code> statement returning a single column of text values.<br>
		 * The result is a complete SQL statement and expression.
		 * @param values the values to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
		 */
		public abstract Select.Values stringValues(Iterable<? extends CharSequence> values);

		/**
		 * Constructs a <code>VALUES</code> statement returning a single column of numeric values.<br>
		 * The result is a complete SQL statement and expression.
		 * @param values the values to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
		 */
		public abstract Select.Values numericValues(Iterable<? extends Number> values);

		/**
		 * Constructs a <code>VALUES</code> statement returning a single column of BLOBs.<br>
		 * The result is a complete SQL statement and expression.
		 * @param values the values to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
		 */
		public abstract Select.Values blobValues(Iterable<byte[]> values);

		/**
		 * Constructs a <code>VALUES</code> statement returning a single row consisting
		 * of the specified expressions.<br>
		 * The result is a complete SQL statement and expression.
		 * @param expressions the expressions to be arranged in a single row in the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>expression<sub>0</sub></em>{, (<em>expression<sub>i</sub></em>)})</strong></code>
		 */
		public Select.Values valuesInRow(SqlExpression... expressions) {
			return valuesInRow(Arrays.asList(expressions));
		}

		/**
		 * Constructs a <code>VALUES</code> statement returning a single row consisting
		 * of the specified expressions.<br>
		 * The result is a complete SQL statement and expression.
		 * @param expressions the expressions to be arranged in a single row in the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>expression<sub>0</sub></em>{, <em>expression<sub>i</sub></em>})</strong></code>
		 */
		public Select.Values valuesInRow(Iterable<? extends SqlExpression> expressions) {
			return values(Collections.singleton(SqlExpressions.rowOf(expressions)));
		}

		/**
		 * Constructs a <code>VALUES</code> statement returning the specified rows.<br>
		 * The result is a complete SQL statement and expression.
		 * @param rows the rows to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>expression<sub>0,0</sub></em>{, <em>expression<sub>0,j</sub></em>}){,
		 * (<em>expression<sub>i,0</sub></em>{, <em>expression<sub>i,j</sub></em>})}</strong></code>
		 */
		public Select.Values values(RowExpression... rows) {
			return values(Arrays.asList(rows));
		}

		/**
		 * Constructs a <code>VALUES</code> statement returning the specified rows.<br>
		 * The result is a complete SQL statement and expression.
		 * @param rows the rows to be returned by the <code>VALUES</code> statement
		 * @return the exact result depends on the preceding part of the statement;
		 * the appended suffix has the form<br>
		 * <code><strong>VALUES (<em>expression<sub>0,0</sub></em>{, <em>expression<sub>0,j</sub></em>}){,
		 * (<em>expression<sub>i,0</sub></em>{, <em>expression<sub>i,j</sub></em>})}</strong></code>
		 */
		public abstract Select.Values values(Iterable<? extends RowExpression> rows);
	}

	abstract static class PrimitiveColumn extends Values {

		protected final boolean isReadonly;

		PrimitiveColumn(boolean isReadonly) {
			this.isReadonly = isReadonly;
		}

		PrimitiveColumn(CommonTableExpression cte, boolean isReadonly) {
			super(cte);
			this.isReadonly = isReadonly;
		}

		PrimitiveColumn(NotSorted previous, CompoundOperator operator, boolean isReadonly) {
			super(previous, operator);
			this.isReadonly = isReadonly;
		}

		PrimitiveColumn(
			NotSorted previous, CompoundOperator operator, CommonTableExpression cte, boolean isReadonly) {

			super(previous, operator, cte);
			this.isReadonly = isReadonly;
		}

		@Override
		public Values copy() {
			return isReadonly ? this : deepCopy();
		}

		abstract Values deepCopy();
	}

	static final class IntegerColumn extends PrimitiveColumn {

		private final int[] values;

		IntegerColumn(int[] values, boolean isReadonly) {
			super(isReadonly);
			this.values = values;
		}

		IntegerColumn(CommonTableExpression cte, int[] values, boolean isReadonly) {
			super(cte, isReadonly);
			this.values = values;
		}

		IntegerColumn(NotSorted previous, CompoundOperator operator, int[] values, boolean isReadonly) {
			super(previous, operator, isReadonly);
			this.values = values;
		}

		private IntegerColumn(NotSorted previous, CompoundOperator operator, CommonTableExpression cte,
			int[] values, boolean isReadonly) {

			super(previous, operator, cte, isReadonly);
			this.values = values;
		}

		@Override
		public void build(StringBuilder receptacle) {
			super.build(receptacle);
			for (int i = 0; i < values.length; i++) {
				receptacle.append('(').append(values[i]).append(')');
				if (i != values.length - 1) {
					receptacle.append(", ");
				}
			}
		}

		@Override
		public Values deepCopy() {
			return new IntegerColumn(
				previous == null ? null : previous.copy(), operator,
				cte == null ? null : cte.copy(), values.clone(), true);
		}
	}

	static final class LongColumn extends PrimitiveColumn {

		private final long[] values;

		LongColumn(long[] values, boolean isReadonly) {
			super(isReadonly);
			this.values = values;
		}

		LongColumn(CommonTableExpression cte, long[] values, boolean isReadonly) {
			super(cte, isReadonly);
			this.values = values;
		}

		LongColumn(NotSorted previous, CompoundOperator operator, long[] values, boolean isReadonly) {
			super(previous, operator, isReadonly);
			this.values = values;
		}

		private LongColumn(NotSorted previous, CompoundOperator operator, CommonTableExpression cte,
			long[] values, boolean isReadonly) {

			super(previous, operator, cte, isReadonly);
			this.values = values;
		}

		@Override
		public void build(StringBuilder receptacle) {
			super.build(receptacle);
			for (int i = 0; i < values.length; i++) {
				receptacle.append('(').append(values[i]).append(')');
				if (i != values.length - 1) {
					receptacle.append(", ");
				}
			}
		}

		@Override
		public Values deepCopy() {
			return new LongColumn(
				previous == null ? null : previous.copy(), operator,
				cte == null ? null : cte.copy(), values.clone(), true);
		}
	}

	static final class FloatColumn extends PrimitiveColumn {

		private final float[] values;

		FloatColumn(float[] values, boolean isReadonly) {
			super(isReadonly);
			this.values = values;
		}

		FloatColumn(CommonTableExpression cte, float[] values, boolean isReadonly) {
			super(cte, isReadonly);
			this.values = values;
		}

		FloatColumn(NotSorted previous, CompoundOperator operator, float[] values, boolean isReadonly) {
			super(previous, operator, isReadonly);
			this.values = values;
		}

		private FloatColumn(NotSorted previous, CompoundOperator operator, CommonTableExpression cte,
			float[] values, boolean isReadonly) {

			super(previous, operator, cte, isReadonly);
			this.values = values;
		}

		@Override
		public void build(StringBuilder receptacle) {
			super.build(receptacle);
			for (int i = 0; i < values.length; i++) {
				receptacle.append('(').append(values[i]).append(')');
				if (i != values.length - 1) {
					receptacle.append(", ");
				}
			}
		}

		@Override
		public Values deepCopy() {
			return new FloatColumn(
				previous == null ? null : previous.copy(), operator,
				cte == null ? null : cte.copy(), values.clone(), true);
		}
	}

	static final class DoubleColumn extends PrimitiveColumn {

		private final double[] values;

		DoubleColumn(double[] values, boolean isReadonly) {
			super(isReadonly);
			this.values = values;
		}

		DoubleColumn(CommonTableExpression cte, double[] values, boolean isReadonly) {
			super(cte, isReadonly);
			this.values = values;
		}

		DoubleColumn(NotSorted previous, CompoundOperator operator, double[] values, boolean isReadonly) {
			super(previous, operator, isReadonly);
			this.values = values;
		}

		private DoubleColumn(NotSorted previous, CompoundOperator operator, CommonTableExpression cte,
			double[] values, boolean isReadonly) {

			super(previous, operator, cte, isReadonly);
			this.values = values;
		}

		@Override
		public void build(StringBuilder receptacle) {
			super.build(receptacle);
			for (int i = 0; i < values.length; i++) {
				receptacle.append('(').append(values[i]).append(')');
				if (i != values.length - 1) {
					receptacle.append(", ");
				}
			}
		}

		@Override
		public Values deepCopy() {
			return new DoubleColumn(
				previous == null ? null : previous.copy(), operator,
				cte == null ? null : cte.copy(), values.clone(), true);
		}
	}

	abstract static class ObjectColumn<T> extends Values {

		final Iterable<? extends T> values;

		ObjectColumn(Iterable<? extends T> values) {
			this.values = values;
		}

		ObjectColumn(CommonTableExpression cte, Iterable<? extends T> values) {
			super(cte);
			this.values = values;
		}

		ObjectColumn(NotSorted previous, CompoundOperator operator, Iterable<? extends T> values) {
			super(previous, operator);
			this.values = values;
		}

		ObjectColumn(
			NotSorted previous, CompoundOperator operator, CommonTableExpression cte, Iterable<? extends T> values) {

			super(previous, operator, cte);
			this.values = values;
		}

		@Override
		public void build(StringBuilder receptacle) {
			super.build(receptacle);
			Iterator<? extends T> iterator = values.iterator();
			if (!iterator.hasNext()) {
				throw new IllegalStateException("The value sequence must have at least one element.");
			}

			T last = iterator.next();
			while (iterator.hasNext()) {
				receptacle.append('(');
				appendValue(receptacle, last);
				receptacle.append("), ");
				last = iterator.next();
			}
			receptacle.append('(');
			appendValue(receptacle, last);
			receptacle.append(')');
		}

		abstract void appendValue(StringBuilder receptacle, T value);
	}

	static final class TextColumn extends ObjectColumn<CharSequence> {

		TextColumn(Iterable<? extends CharSequence> values) {
			super(values);
		}

		TextColumn(CommonTableExpression cte, Iterable<? extends CharSequence> values) {
			super(cte, values);
		}

		TextColumn(NotSorted previous, CompoundOperator operator, Iterable<? extends CharSequence> values) {
			super(previous, operator, values);
		}

		TextColumn(NotSorted previous, CompoundOperator operator,
			CommonTableExpression cte, Iterable<? extends CharSequence> values) {

			super(previous, operator, cte, values);
		}

		@Override
		public Values copy() {
			NotSorted previousCopy = previous == null ? null : previous.copy();
			CommonTableExpression cteCopy = cte == null ? null : cte.copy();
			Iterable<CharSequence> valuesCopy = ReadonlyIterable.of(values, CharSequence::toString);

			return previousCopy == previous && cteCopy == cte && valuesCopy == values
				? this : new TextColumn(previousCopy, operator, cteCopy, valuesCopy);
		}

		@Override
		void appendValue(StringBuilder receptacle, CharSequence value) {
			receptacle.append('\'');
			SqliteUtilities.escapeSingleQuotes(receptacle, value);
			receptacle.append('\'');
		}
	}

	static final class NumericColumn extends ObjectColumn<Number> {

		NumericColumn(Iterable<? extends Number> values) {
			super(values);
		}

		NumericColumn(CommonTableExpression cte, Iterable<? extends Number> values) {
			super(cte, values);
		}

		NumericColumn(NotSorted previous, CompoundOperator operator, Iterable<? extends Number> values) {
			super(previous, operator, values);
		}

		NumericColumn(NotSorted previous, CompoundOperator operator,
			CommonTableExpression cte, Iterable<? extends Number> values) {

			super(previous, operator, cte, values);
		}

		@Override
		public Values copy() {
			NotSorted previousCopy = previous == null ? null : previous.copy();
			CommonTableExpression cteCopy = cte == null ? null : cte.copy();
			// assuming immutability of Number implementations
			Iterable<Number> valuesCopy = ReadonlyIterable.ofReadonly(values);

			return previousCopy == previous && cteCopy == cte && valuesCopy == values
				? this : new NumericColumn(previousCopy, operator, cteCopy, valuesCopy);
		}

		@Override
		void appendValue(StringBuilder receptacle, Number value) {
			receptacle.append(value.toString());
		}
	}

	static final class BlobColumn extends ObjectColumn<byte[]> {

		BlobColumn(Iterable<? extends byte[]> values) {
			super(values);
		}

		BlobColumn(CommonTableExpression cte, Iterable<? extends byte[]> values) {
			super(cte, values);
		}

		BlobColumn(NotSorted previous, CompoundOperator operator, Iterable<? extends byte[]> values) {
			super(previous, operator, values);
		}

		BlobColumn(NotSorted previous, CompoundOperator operator,
			CommonTableExpression cte, Iterable<? extends byte[]> values) {

			super(previous, operator, cte, values);
		}

		@Override
		public Values copy() {
			NotSorted previousCopy = previous == null ? null : previous.copy();
			CommonTableExpression cteCopy = cte == null ? null : cte.copy();
			// assuming immutability of Number implementations
			Iterable<byte[]> valuesCopy = ReadonlyIterable.of(values, byte[]::clone);

			return previousCopy == previous && cteCopy == cte && valuesCopy == values
				? this : new BlobColumn(previousCopy, operator, cteCopy, valuesCopy);
		}

		@Override
		void appendValue(StringBuilder receptacle, byte[] value) {
			Literal.BlobLiteral.append(value, receptacle);
		}
	}

	static final class Rows extends Values {

		private final Iterable<? extends RowExpression> rows;

		Rows(Iterable<? extends RowExpression> rows) {
			this.rows = rows;
		}

		Rows(CommonTableExpression cte, Iterable<? extends RowExpression> rows) {
			super(cte);
			this.rows = rows;
		}

		Rows(NotSorted previous, CompoundOperator operator, Iterable<? extends RowExpression> rows) {
			super(previous, operator);
			this.rows = rows;
		}

		Rows(NotSorted previous, CompoundOperator operator,
			CommonTableExpression cte, Iterable<? extends RowExpression> rows) {

			super(previous, operator, cte);
			this.rows = rows;
		}

		@Override
		public void build(StringBuilder receptacle) {
			super.build(receptacle);
			Iterator<? extends RowExpression> iterator = rows.iterator();
			if (!iterator.hasNext()) {
				throw new IllegalStateException("The row sequence must contain at least one element.");
			}

			RowExpression last = iterator.next();
			while (iterator.hasNext()) {
				last.appendTo(receptacle);
				receptacle.append(", ");
				last = iterator.next();
			}
			last.appendTo(receptacle);
		}

		@Override
		public Values copy() {
			NotSorted previousCopy = previous == null ? null : previous.copy();
			CommonTableExpression cteCopy = cte == null ? null : cte.copy();
			Iterable<? extends RowExpression> rowsCopy = ReadonlyIterable.of(rows, RowExpression::copy);

			return previousCopy == previous && cteCopy == cte && rowsCopy == rows
				? this : new Rows(previousCopy, operator, cteCopy, rowsCopy);
		}
	}

	private Select() { }
}
