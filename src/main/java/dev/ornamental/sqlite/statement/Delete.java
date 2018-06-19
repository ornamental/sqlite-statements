package dev.ornamental.sqlite.statement;

/**
 * This class has no functionality of its own. It is destined for grouping the classes pertaining
 * to the <code>DELETE</code> statement.
 */
public final class Delete {

	/**
	 * This interface may be used to add <code>ORDER BY</code> sorting keys
	 * in a manner uniform both for a <code>DELETE</code> statement not yet having
	 * an <code>ORDER BY</code> clause and a limited <code>DELETE</code> statement stub
	 * already having at least one sorting key specified.
	 */
	public interface Sortable {

		/**
		 * Adds a sorting key expression with the default sorting order to the <code>ORDER BY</code>
		 * clause (in case this is the first sorting key, adds the clause itself).
		 * @param sortingKey the expression by whose value the sorting will be performed
		 * @return the incomplete <code>DELETE</code> statement ending with an <code>ORDER BY</code>
		 * clause whose last sorting key is the specified expression
		 */
		Ordered orderBy(SqlExpression sortingKey);

		/**
		 * Adds a sorting key expression with the specified sorting order to the <code>ORDER BY</code>
		 * clause (in case this is the first sorting key, adds the clause itself).
		 * @param sortingKey the expression by whose value the sorting will be performed
		 * @param order the sorting order for the key
		 * @return the incomplete <code>DELETE</code> statement ending with an <code>ORDER BY</code>
		 * clause whose last sorting key is the specified expression
		 */
		Ordered orderBy(SqlExpression sortingKey, SortingOrder order);

		/**
		 * Adds a <code>LIMIT</code> clause to the <code>DELETE</code> statement.<br/>
		 * The result is a complete SQL statement.
		 * @param limitExpression the expression returning the maximum number of rows to delete
		 * @return the <code>DELETE</code> statement ending with
		 * the <code>LIMIT <em>limitExpression</em></code> clause
		 */
		LimitedNoOffset limit(SqlExpression limitExpression);

		/**
		 * Adds a <code>LIMIT</code> clause to the <code>DELETE</code> statement.<br/>
		 * The result is a complete SQL statement.
		 * @param maxRows maximum number of rows to delete
		 * @return the <code>DELETE</code> statement ending with the <code>LIMIT <em>limit</em></code>
		 * clause
		 */
		LimitedNoOffset limit(long maxRows);
	}

	private abstract static class NotLimited implements Sortable, TriggerStatement {

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

		@Override
		public abstract NotLimited copy();
	}

	private abstract static class NotFiltered extends NotLimited {

		@Override
		public abstract NotFiltered copy();

		public Filtered where(SqlExpression condition) {
			return new Filtered(this, condition);
		}
	}

	/**
	 * Represents a <code>DELETE</code> statement without filtering, deleted row limitation,
	 * nor index use directive:<br/>
	 * <code><strong>[WITH <em>commonTableExpression</em>]
	 * DELETE FROM [<em>schemaName</em>.]<em>tableName</em></strong></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class All extends NotFiltered {

		private final CommonTableExpression cte; // may be null

		private final CharSequence schemaName; // may be null

		private final CharSequence tableName;

		All(CommonTableExpression cte, CharSequence schemaName, CharSequence tableName) {
			this.cte = cte;
			this.schemaName = schemaName;
			this.tableName = tableName;
		}

		/**
		 * Adds a <code>INDEXED BY</code> clause to the <code>DELETE</code> statement.<br/>
		 * The result is a complete SQL statement.
		 * @param indexName the name of the index to force the use of
		 * @return the <code>DELETE</code> statement with enforced index use:<br/>
		 * <code>[WITH <em>commonTableExpression</em>] DELETE FROM [<em>schemaName</em>.]<em>tableName</em>
		 * <strong>INDEXED BY <em>indexName</em></strong></code><br/>
		 */
		public WithIndexDirective indexedBy(CharSequence indexName) {
			return new WithIndexDirective(this, indexName);
		}

		/**
		 * Adds a <code>NOT INDEXED</code> clause to the <code>DELETE</code> statement.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>DELETE</code> statement with forbidden index use:<br/>
		 * <code>[WITH <em>commonTableExpression</em>] DELETE FROM [<em>schemaName</em>.]<em>tableName</em>
		 * <strong>NOT INDEXED</strong></code><br/>
		 */
		public WithIndexDirective notIndexed() {
			return new WithIndexDirective(this, null);
		}

		@Override
		public All copy() throws IllegalStateException {
			CommonTableExpression cteCopy = cte == null ? null : cte.copy();
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();

			return cteCopy == cte && schemaNameCopy == schemaName && tableNameCopy == tableName
				? this : new All(cteCopy, schemaNameCopy, tableNameCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			if (cte != null) {
				cte.appendTo(receptacle);
				receptacle.append(' ');
			}
			receptacle.append("DELETE FROM ");
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
		}
	}

	/**
	 * Represents a <code>DELETE</code> statement without filtering nor deleted row limitation.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class WithIndexDirective extends NotFiltered {

		private final All previous;

		private final CharSequence indexName; // may be null

		WithIndexDirective(All previous, CharSequence indexName) {
			this.previous = previous;
			this.indexName = indexName;
		}

		@Override
		public WithIndexDirective copy() throws IllegalStateException {
			All previousCopy = previous.copy();
			CharSequence indexNameCopy = indexName == null ? null : indexName.toString();

			return previousCopy == previous && indexNameCopy == indexName
				? this : new WithIndexDirective(previousCopy, indexNameCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			TableWithIndex.appendIndexDirective(receptacle, indexName);
		}
	}

	/**
	 * Represents an incomplete <code>DELETE</code> statement ending with an <code>ORDER BY</code>
	 * clause with one or more sorting keys.
	 */
	public static final class Ordered implements Sortable {

		private final NotLimited previousNotLimited;

		private final Ordered previousOrdered;

		private final Sort sort;

		Ordered(NotLimited previous, Sort sort) {
			this(previous, null, sort);
		}

		Ordered(Ordered previous, Sort sort) {
			this(null, previous, sort);
		}

		private Ordered(
			NotLimited previousNotLimited, Ordered previousOrdered, Sort sort) {

			this.previousNotLimited = previousNotLimited;
			this.previousOrdered = previousOrdered;
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
			NotLimited previousUnlimitedCopy = previousNotLimited == null ? null : previousNotLimited.copy();
			Ordered previousOrderedCopy = previousOrdered == null ? null : previousOrdered.copy();
			Sort sortCopy = sort.copy();

			return
				previousUnlimitedCopy == previousNotLimited
				&& previousOrderedCopy == previousOrdered && sortCopy == sort
					? this : new Ordered(previousUnlimitedCopy, previousOrderedCopy, sortCopy);
		}

		void appendTo(StringBuilder receptacle) {
			if (previousNotLimited != null) {
				previousNotLimited.build(receptacle);
				receptacle.append(" ORDER BY ");
			} else {
				previousOrdered.appendTo(receptacle);
				receptacle.append(", ");
			}

			sort.appendTo(receptacle);
		}
	}

	/**
	 * Represents a <code>DELETE</code>statement with a <code>LIMIT</code> clause
	 * but no offset specified.<br/>
	 * This is a complete SQL statement.
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
		 * Adds an <code>OFFSET</code> clause to the <code>DELETE</code> statement.<br/>
		 * The result is a complete SQL statement.
		 * @param offsetExpression the expression returning the offset value
		 * @return the <code>DELETE</code> statement finalized with an <code>OFFSET</code>
		 * clause
		 */
		public LimitedWithOffset offset(SqlExpression offsetExpression) {
			return new LimitedWithOffset(this, new IntegralValueClause.Offset(offsetExpression));
		}

		/**
		 * Adds an <code>OFFSET</code> clause to the <code>DELETE</code> statement.<br/>
		 * The result is a complete SQL statement.
		 * @param offset the offset value
		 * @return the <code>DELETE</code> statement finalized with an <code>OFFSET</code>
		 * clause
		 */
		public LimitedWithOffset offset(long offset) {
			if (offset < 0) {
				throw new IllegalArgumentException("The offset value must not be negative.");
			}
			return new LimitedWithOffset(this, new IntegralValueClause.Offset(offset));
		}

		@Override
		public LimitedNoOffset copy() throws IllegalStateException {
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
	 * Represents a <code>DELETE</code> statement ending with an <code>OFFSET</code> clause.<br/>
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
	 * Represents a <code>DELETE</code> statement with a filtering <code>WHERE</code> clause.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class Filtered extends NotLimited {

		private final NotFiltered previous;

		private final SqlExpression condition;

		Filtered(NotFiltered previous, SqlExpression condition) {
			this.previous = previous;
			this.condition = condition;
		}

		@Override
		public Filtered copy() throws IllegalStateException {
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

	private Delete() { }
}
