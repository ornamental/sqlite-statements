package dev.ornamental.sqlite.statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has no functionality of its own. It is destined for grouping the classes pertaining
 * to the <code>CREATE INDEX</code> statement.
 */
public final class CreateIndex {

	/**
	 * Represents the initial part of <code>CREATE INDEX</code> statement, namely<br>
	 * <code><strong>CREATE [UNIQUE] INDEX [IF NOT EXISTS]</strong></code>.
	 */
	public static final class Stub {

		private final boolean unique;

		private final boolean ifNotExists;

		Stub(boolean unique, boolean ifNotExists) {
			this.unique = unique;
			this.ifNotExists = ifNotExists;
		}

		/**
		 * Appends the index name to the <code>CREATE INDEX</code> statement prefix.
		 * @param indexName the name of the index to be created
		 * @return the initial part of <code>CREATE INDEX</code> statement containing the index name:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS] <strong><em>indexName</em></strong></code>
		 */
		public Named named(CharSequence indexName) {
			return named(null, indexName);
		}

		/**
		 * Appends the schema-qualified index name to the <code>CREATE INDEX</code> statement prefix.
		 * @param schemaName the name of the target schema
		 * @param indexName the name of the index to be created
		 * @return the initial part of <code>CREATE INDEX</code> statement containing the index name:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS]
		 * <strong><em>schemaName</em>.<em>indexName</em></strong></code>
		 */
		public Named named(CharSequence schemaName, CharSequence indexName) {
			return new Named(unique, ifNotExists, schemaName, indexName);
		}
	}

	/**
	 * Represents the initial part of <code>CREATE INDEX</code> statement, namely<br>
	 * <code><strong>CREATE [UNIQUE] INDEX [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>indexName</em></strong></code>.
	 */
	public static final class Named {

		private final boolean unique;

		private final boolean ifNotExists;

		private final CharSequence schemaName; // may be null

		private final CharSequence indexName;

		Named(boolean unique, boolean ifNotExists, CharSequence schemaName, CharSequence indexName) {
			this.unique = unique;
			this.ifNotExists = ifNotExists;
			this.schemaName = schemaName;
			this.indexName = indexName;
		}

		/**
		 * Adds the target table name to the prefix of the <code>CREATE INDEX</code> statement
		 * @param tableName the name of the target table of the index
		 * @return the initial part of <code>CREATE INDEX</code> statement containing
		 * the target table name:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>indexName</em> <strong>ON <em>tableName</em></strong></code>
		 */
		public OnTable onTable(CharSequence tableName) {
			return new OnTable(unique, ifNotExists, schemaName, indexName, tableName);
		}

		/**
		 * Adds the target table name to the prefix of the <code>CREATE INDEX</code> statement
		 * @param table the target table of the index
		 * @return the initial part of <code>CREATE INDEX</code> statement containing
		 * the target table name:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>indexName</em> <strong>ON <em>tableName</em></strong></code>
		 */
		public OnTable onTable(Table table) {
			if (table.schemaName() != null
				&& (schemaName == null && !"main".equalsIgnoreCase(table.schemaName())
					|| schemaName != null && !table.schemaName().equalsIgnoreCase(schemaName.toString()))) {

				throw new IllegalArgumentException(
					"The table has a schema specified, and it is not the one of the trigger.");
			}

			return new OnTable(unique, ifNotExists, schemaName, indexName, table.tableName());
		}
	}

	/**
	 * This interface contains overloaded method for adding an indexed column definition
	 * to the index definition.<br>
	 * It may be used to add columns uniformly to the instances
	 * of the implementing classes: {@link OnTable} not yet containing any columns
	 * and {@link OnColumns} containing at least one column definition.
	 */
	public interface ColumnList {

		/**
		 * Adds a column from the original table to the index definition (with default
		 * sorting order).<br>The result is a complete SQL statement.
		 * @param indexedColumn the original table column name
		 * @return a <code>CREATE INDEX</code> statement having the specified column as the last one:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS] [<em>schemaName</em>.]<em>indexName</em>
		 * ON <em>tableName</em>({<em>indexedColumnDefinition<sub>i</sub></em>,
		 * }<strong><em>indexedColumn</em></strong>)</code>
		 */
		default OnColumns addColumn(String indexedColumn) {
			return addColumn(indexedColumn, null);
		}

		/**
		 * Adds a column from the original table to the index definition (with the specified
		 * sorting order).<br>The result is a complete SQL statement.
		 * @param indexedColumn the original table column name
		 * @param order the explicit sort order to use for the indexed column
		 * @return a <code>CREATE INDEX</code> statement having the specified column as the last one:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS] [<em>schemaName</em>.]<em>indexName</em>
		 * ON <em>tableName</em>({<em>indexedColumnDefinition<sub>i</sub></em>,
		 * }<strong><em>indexedColumn</em> ASC|DESC</strong>)</code>
		 */
		default OnColumns addColumn(String indexedColumn, SortingOrder order) {
			return addColumn(SqlExpressions.column(indexedColumn), order);
		}

		/**
		 * Adds a computed column to the index definition (with default sorting order).<br>
		 * The result is a complete SQL statement.
		 * @param indexedExpression the expression (computed column) whose value has to be indexed
		 * @return a <code>CREATE INDEX</code> statement having the specified computed column
		 * as the last column:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS] [<em>schemaName</em>.]<em>indexName</em>
		 * ON <em>tableName</em>({<em>indexedColumnDefinition<sub>i</sub></em>,
		 * }<strong><em>indexedExpression</em></strong>)</code>
		 */
		default OnColumns addColumn(SqlExpression indexedExpression) {
			return addColumn(indexedExpression, null);
		}

		/**
		 * Adds a computed column to the index definition (with the specified sorting order).<br>
		 * The result is a complete SQL statement.
		 * @param indexedExpression the expression (computed column) whose value has to be indexed
		 * @param order the explicit sort order to use for the indexed expression
		 * @return a <code>CREATE INDEX</code> statement having the specified computed column
		 * as the last column:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS] [<em>schemaName</em>.]<em>indexName</em>
		 * ON <em>tableName</em>({<em>indexedColumnDefinition<sub>i</sub></em>,
		 * }<strong><em>indexedExpression</em> ASC|DESC</strong>)</code>
		 */
		OnColumns addColumn(SqlExpression indexedExpression, SortingOrder order);
	}

	/**
	 * Represents the initial part of a <code>CREATE INDEX</code> statement
	 * containing the target table name but yet no indexed column definitions:<br>
	 * <code><strong>CREATE [UNIQUE] INDEX [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>indexName</em> ON <em>tableName</em></strong></code>
	 */
	public static final class OnTable implements ColumnList {

		private final boolean unique;

		private final boolean ifNotExists;

		private final CharSequence schemaName; // may be null

		private final CharSequence indexName;

		private final CharSequence tableName;

		OnTable(boolean unique, boolean ifNotExists,
			CharSequence schemaName, CharSequence indexName, CharSequence tableName) {

			this.unique = unique;
			this.ifNotExists = ifNotExists;
			this.schemaName = schemaName;
			this.indexName = indexName;
			this.tableName = tableName;
		}

		@Override
		public OnColumns addColumn(SqlExpression indexedExpression, SortingOrder order) {
			return new OnColumns(this, indexedExpression, order);
		}

		OnTable copy() {
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence indexNameCopy = indexName.toString();
			CharSequence tableNameCopy = tableName.toString();

			return schemaNameCopy == schemaName && indexNameCopy == indexName && tableNameCopy == tableName
				? this : new OnTable(unique, ifNotExists, schemaNameCopy, indexNameCopy, tableNameCopy);
		}

		void appendTo(StringBuilder receptacle) {
			receptacle.append("CREATE ");
			if (unique) {
				receptacle.append("UNIQUE ");
			}
			receptacle.append("INDEX ");
			if (ifNotExists) {
				receptacle.append("IF NOT EXISTS ");
			}
			SqliteUtilities.appendQuotedName(receptacle, schemaName, indexName);
			receptacle.append(" ON ");
			SqliteUtilities.appendQuotedName(receptacle, tableName);
		}
	}

	/**
	 * Represents an unconstrained <code>CREATE INDEX</code> statement
	 * (without a <code>WHERE</code> clause):<br>
	 * <code><strong>CREATE [UNIQUE] INDEX [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>indexName</em> ON <em>tableName</em>(<em>indexedColumnDefinition<sub>0</sub></em>{,
	 * <em>indexedColumnDefinition<sub>i</sub></em>})</strong></code>.<br>
	 * This is a complete SQL statement.
	 */
	public static final class OnColumns implements ExplicableStatement, ColumnList {

		private final OnTable stub;

		private final OnColumns previous;

		private final SqlExpression indexedColumn;

		private final SortingOrder order; // may be null

		OnColumns(OnTable stub, SqlExpression indexedColumn, SortingOrder order) {
			this(null, stub, indexedColumn, order);
		}

		private OnColumns(OnColumns previous,
			OnTable stub, SqlExpression indexedColumn, SortingOrder order) {

			this.stub = stub;
			this.previous = previous;
			this.indexedColumn = indexedColumn;
			this.order = order;
		}

		@Override
		public OnColumns copy() throws IllegalStateException {
			OnColumns previousCopy = previous == null ? null : previous.copy();
			OnTable stubCopy = stub == null ? null : stub.copy();
			SqlExpression indexedColumnCopy = indexedColumn.copy();

			return previousCopy == previous && stubCopy == stub && indexedColumnCopy == indexedColumn
				? this : new OnColumns(previousCopy, stubCopy, indexedColumnCopy, order);
		}

		@Override
		public void build(StringBuilder receptacle) {
			List<OnColumns> chain = new ArrayList<>();
			for (OnColumns current = this; current != null; current = current.previous) {
				chain.add(current);
			}

			chain.get(chain.size() - 1).stub.appendTo(receptacle);
			receptacle.append('(');
			for (int i = chain.size() - 1; i >= 0; i--) {
				chain.get(i).indexedColumn.appendTo(receptacle);
				SortingOrder explicitOrder = chain.get(i).order;
				if (explicitOrder != null) {
					receptacle.append(' ').append(explicitOrder.toString());
				}
				if (i != 0) {
					receptacle.append(", ");
				}
			}
			receptacle.append(')');
		}

		@Override
		public OnColumns addColumn(SqlExpression indexedExpression, SortingOrder order) {
			return new OnColumns(this, null, indexedExpression, order);
		}

		/**
		 * Adds an index constraint condition (a <code>WHERE</code> clause) to this
		 * <code>CREATE INDEX</code> statement.<br>The result is a complete SQL statement.
		 * @param condition the condition constraining the indexed row set
		 * @return the <code>CREATE INDEX</code> statement in its most complete form:<br>
		 * <code>CREATE [UNIQUE] INDEX [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>indexName</em> ON <em>tableName</em>(<em>indexedColumnDefinition<sub>0</sub>
		 * </em>{, <em>indexedColumnDefinition<sub>i</sub></em>})
		 * <strong>WHERE <em>condition</em></strong></code>
		 */
		public Constrained where(SqlExpression condition) {
			return new Constrained(this, condition);
		}
	}

	/**
	 * Represents a <code>CREATE INDEX</code> statement in its most complete form:<br>
	 * <code><strong>CREATE [UNIQUE] INDEX [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>indexName</em> ON <em>tableName</em>(<em>indexedColumnDefinition<sub>0</sub>
	 * </em>{, <em>indexedColumnDefinition<sub>i</sub></em>})
	 * WHERE <em>condition</em></strong></code>.
	 */
	public static final class Constrained implements ExplicableStatement {

		private final OnColumns previous;

		private final SqlExpression condition;

		Constrained(OnColumns previous, SqlExpression condition) {
			this.previous = previous;
			this.condition = condition;
		}

		@Override
		public Constrained copy() throws IllegalStateException {
			OnColumns previousCopy = previous.copy();
			SqlExpression conditionCopy = condition.copy();

			return previousCopy == previous && conditionCopy == condition
				? this : new Constrained(previousCopy, conditionCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			receptacle.append(" WHERE ");
			condition.appendTo(receptacle);
		}
	}

	private CreateIndex() { }
}
