package dev.ornamental.sqlite.statement;

import java.util.Arrays;

/**
 * Represents a common table expression (a. k. a. CTE, or <code><strong>WITH</strong></code> clause)
 * being an optional prefix of <code>SELECT</code>, <code>DELETE</code>, <code>INSERT</code>,
 * <code>REPLACE</code>, and <code>UPDATE</code> statements.
 */
public final class CommonTableExpression extends Select.ValueProducer implements Variable<CommonTableExpression> {

	private final CharSequence name;

	private final Iterable<? extends CharSequence> columns; // may be null

	private final SelectStatement expression;

	private final CommonTableExpression previous;

	CommonTableExpression(CommonTableExpression previous,
		CharSequence name, Iterable<? extends CharSequence> columns, SelectStatement expression) {

		this.previous = previous;
		this.name = name;
		this.columns = columns;
		this.expression = expression;
	}

	/**
	 * Adds another table to the common table expression.
	 * @param nextCteName the name of the next table of the <code>WITH</code> clause
	 * @return the CTE builder allowing to specify the column names of the common table
	 * or proceed with table construction directly
	 */
	public CteBuilderNoColumns andWith(CharSequence nextCteName) {
		return new CteBuilderNoColumns(this, nextCteName);
	}

	/**
	 * Defines the values to select. You may use {@link SqlExpression} instances as well
	 * as instances returned by {@link ResultElements#all()} and {@link ResultElements#allOf(CharSequence)}
	 * methods.<br>
	 * The result is a complete SQL statement (expression) as long as the selected values do not
	 * refer to an unspecified table (in which case a <code>FROM</code> clause will have to be specified).
	 * @param columns the definitions of columns to include in the <code>SELECT</code> clause
	 * @return the <code>SELECT</code> statement having the form<br>
	 * <code><strong>WITH <em>cte</em> SELECT <em>resultColumn<sub>0</sub></em>
	 * {, <em>resultColumn<sub>i</sub></em>}</strong></code><br>
	 * where <code><em>resultColumn<sub>i</sub></em></code> is either an expression,
	 * or an asterisk with optional table name qualifier (<code><strong>[<em>tableName</em>.]*</strong></code>)
	 * and <code><em>cte</em></code> is this common table expression
	 */
	public Select.NoSource select(ResultElement... columns) {
		return select(Arrays.asList(columns));
	}

	/**
	 * Defines the values to select. You may use {@link SqlExpression} instances as well
	 * as instances returned by {@link ResultElements#all()} and {@link ResultElements#allOf(CharSequence)}
	 * methods.<br>
	 * The result is a complete SQL statement (expression) as long as the selected values do not
	 * refer to an unspecified table (in which case a <code>FROM</code> clause will have to be specified).
	 * @param columns the definitions of columns to include in the <code>SELECT</code> clause
	 * @return the <code>SELECT</code> statement having the form<br>
	 * <code><strong>WITH <em>cte</em> SELECT <em>resultColumn<sub>0</sub></em>
	 * {, <em>resultColumn<sub>i</sub></em>}</strong></code><br>
	 * where <code><em>resultColumn<sub>i</sub></em></code> is either an expression,
	 * or an asterisk with optional table name qualifier (<code><strong>[<em>tableName</em>.]*</strong></code>)
	 * and <code><em>cte</em></code> is this common table expression
	 */
	public Select.NoSource select(Iterable<? extends ResultElement> columns) {
		return new Select.NoSource(this, false, columns);
	}

	/**
	 * Defines the values to select with result row deduplication. You may use {@link SqlExpression}
	 * instances as well as instances returned by {@link ResultElements#all()} and
	 * {@link ResultElements#allOf(CharSequence)} methods.<br>
	 * The result is a complete SQL statement (expression) as long as the selected values do not
	 * refer to an unspecified table (in which case a <code>FROM</code> clause will have to be specified).
	 * @param columns the definitions of columns to include in the <code>SELECT</code> clause
	 * @return the <code>SELECT</code> statement having the form<br>
	 * <code><strong>WITH <em>cte</em> SELECT DISCTINCT <em>resultColumn<sub>0</sub></em>
	 * {, <em>resultColumn<sub>i</sub></em>}</strong></code><br>
	 * where <code><em>resultColumn<sub>i</sub></em></code> is either an expression,
	 * or an asterisk with optional table name qualifier (<code><strong>[<em>tableName</em>.]*</strong></code>)
	 * and <code><em>cte</em></code> is this common table expression
	 */
	public Select.NoSource selectDistinct(ResultElement... columns) {
		return selectDistinct(Arrays.asList(columns));
	}

	/**
	 * Defines the values to select with result row deduplication. You may use {@link SqlExpression}
	 * instances as well as instances returned by {@link ResultElements#all()} and
	 * {@link ResultElements#allOf(CharSequence)} methods.<br>
	 * The result is a complete SQL statement (expression) as long as the selected values do not
	 * refer to an unspecified table (in which case a <code>FROM</code> clause will have to be specified).
	 * @param columns the definitions of columns to include in the <code>SELECT</code> clause
	 * @return the <code>SELECT</code> statement having the form<br>
	 * <code><strong>WITH <em>cte</em> SELECT DISCTINCT <em>resultColumn<sub>0</sub></em>
	 * {, <em>resultColumn<sub>i</sub></em>}</strong></code><br>
	 * where <code><em>resultColumn<sub>i</sub></em></code> is either an expression,
	 * or an asterisk with optional table name qualifier (<code><strong>[<em>tableName</em>.]*</strong></code>)
	 * and <code><em>cte</em></code> is this common table expression
	 */
	public Select.NoSource selectDistinct(Iterable<? extends ResultElement> columns) {
		return new Select.NoSource(this, true, columns);
	}

	/**
	 * Returns the simplest <code>DELETE</code> statement preceded by this common table expression,
	 * namely<br>
	 * <code><strong>WITH <em>cte</em> DELETE FROM <em>tableName</em></strong></code>. Though in such
	 * a statement the <code>WITH</code> clause is useless, it may serve as a base for more complex
	 * filtered and/or limited <code>DELETE</code> statement.
	 * @param tableName the name of the table to delete rows from
	 * @return the <code>DELETE</code> statement having the form<br>
	 * <code><strong>WITH <em>cte</em> DELETE FROM <em>tableName</em></strong></code><br>
	 * where <code><em>cte</em></code> is this common table expression
	 */
	public Delete.All deleteFrom(CharSequence tableName) {
		return new Delete.All(this, null, tableName);
	}

	/**
	 * Returns the simplest <code>DELETE</code> statement preceded by this common table expression,
	 * namely<br>
	 * <code><strong>WITH <em>cte</em> DELETE FROM <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * Though in such a statement the <code>WITH</code> clause is useless, it may serve as a base for more
	 * complex filtered and/or limited <code>DELETE</code> statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to delete rows from
	 * @return the <code>DELETE</code> statement having the form<br>
	 * <code><strong>WITH <em>cte</em> DELETE FROM <em>schemaName</em>.<em>tableName</em></strong></code><br>
	 * where <code><em>cte</em></code> is this common table expression
	 */
	public Delete.All deleteFrom(CharSequence schemaName, CharSequence tableName) {
		return new Delete.All(this, schemaName, tableName);
	}

	/**
	 * Returns an <code>INSERT</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT INTO <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT</code> statement stub  using this common table expression
	 */
	public Insert.Into insertInto(CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT, null, tableName);
	}

	/**
	 * Returns an <code>INSERT</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT INTO <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT</code> statement stub  using this common table expression
	 */
	public Insert.Into insertInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT, schemaName, tableName);
	}

	/**
	 * Returns a <code>REPLACE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> REPLACE INTO <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to replace rows in
	 * @return the <code>REPLACE</code> statement stub  using this common table expression
	 */
	public Insert.Into replaceInto(CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.REPLACE, null, tableName);
	}

	/**
	 * Returns a <code>REPLACE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> REPLACE INTO <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to replace rows in
	 * @return the <code>REPLACE</code> statement stub  using this common table expression
	 */
	public Insert.Into replaceInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.REPLACE, schemaName, tableName);
	}

	/**
	 * Returns an <code>INSERT OR REPLACE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR REPLACE INTO <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR REPLACE</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrReplaceInto(CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_REPLACE, null, tableName);
	}

	/**
	 * Returns an <code>INSERT OR REPLACE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR REPLACE INTO <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR REPLACE</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrReplaceInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_REPLACE, schemaName, tableName);
	}

	/**
	 * Returns an <code>INSERT OR ABORT</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR ABORT INTO <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR ABORT</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrAbortInto(CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_ABORT, null, tableName);
	}

	/**
	 * Returns an <code>INSERT OR ABORT</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR ABORT INTO <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR ABORT</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrAbortInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_ABORT, schemaName, tableName);
	}

	/**
	 * Returns an <code>INSERT OR ROLLBACK</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR ROLLBACK INTO <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR ROLLBACK</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrRollbackInto(CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_ROLLBACK, null, tableName);
	}

	/**
	 * Returns an <code>INSERT OR ROLLBACK</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR ROLLBACK INTO <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR ROLLBACK</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrRollbackInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_ROLLBACK, schemaName, tableName);
	}

	/**
	 * Returns an <code>INSERT OR FAIL</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR FAIL INTO <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR FAIL</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrFailInto(CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_FAIL, null, tableName);
	}

	/**
	 * Returns an <code>INSERT OR FAIL</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR FAIL INTO <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR FAIL</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrFailInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_FAIL, schemaName, tableName);
	}

	/**
	 * Returns an <code>INSERT OR IGNORE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR IGNORE INTO <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR IGNORE</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrIgnoreInto(CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_IGNORE, null, tableName);
	}

	/**
	 * Returns an <code>INSERT OR IGNORE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> INSERT OR IGNORE INTO <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to insert rows into
	 * @return the <code>INSERT OR IGNORE</code> statement stub  using this common table expression
	 */
	public Insert.Into insertOrIgnoreInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(this, InsertVerb.INSERT_OR_IGNORE, schemaName, tableName);
	}

	/**
	 * Returns an <code>UPDATE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE</code> statement stub  using this common table expression
	 */
	public Update.Stub update(CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE, null, tableName);
	}

	/**
	 * Returns an <code>UPDATE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE</code> statement stub  using this common table expression
	 */
	public Update.Stub update(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE, schemaName, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR ABORT</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR ABORT <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR ABORT</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrAbort(CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_ABORT, null, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR ABORT</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR ABORT <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR ABORT</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrAbort(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_ABORT, schemaName, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR FAIL</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR FAIL <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR FAIL</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrFail(CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_FAIL, null, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR FAIL</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR FAIL <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR FAIL</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrFail(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_FAIL, schemaName, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR IGNORE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR IGNORE <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR IGNORE</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrIgnore(CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_IGNORE, null, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR IGNORE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR IGNORE <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR IGNORE</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrIgnore(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_IGNORE, schemaName, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR REPLACE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR REPLACE <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR REPLACE</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrReplace(CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_REPLACE, null, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR REPLACE</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR REPLACE <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR REPLACE</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrReplace(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_REPLACE, schemaName, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR ROLLBACK</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR ROLLBACK <em>tableName</em></strong></code>.
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR ROLLBACK</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrRollback(CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_ROLLBACK, null, tableName);
	}

	/**
	 * Returns an <code>UPDATE OR ROLLBACK</code> statement stub using this common table expression.
	 * It corresponds to the following part of the statement:<br>
	 * <code><strong>WITH <em>cte</em> UPDATE OR ROLLBACK <em>schemaName</em>.<em>tableName</em></strong></code>.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to update rows in
	 * @return the <code>UPDATE OR ROLLBACK</code> statement stub  using this common table expression
	 */
	public Update.Stub updateOrRollback(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(this, UpdateVerb.UPDATE_OR_ROLLBACK, schemaName, tableName);
	}

	@Override
	public Select.Values values(int... values) {
		return new Select.IntegerColumn(this, values, false);
	}

	@Override
	public Select.Values values(long... values) {
		return new Select.LongColumn(this, values, false);
	}

	@Override
	public Select.Values values(float... values) {
		return new Select.FloatColumn(this, values, false);
	}

	@Override
	public Select.Values values(double... values) {
		return new Select.DoubleColumn(this, values, false);
	}

	@Override
	public Select.Values stringValues(Iterable<? extends CharSequence> values) {
		return new Select.TextColumn(this, values);
	}

	@Override
	public Select.Values numericValues(Iterable<? extends Number> values) {
		return new Select.NumericColumn(this, values);
	}

	@Override
	public Select.Values blobValues(Iterable<byte[]> values) {
		return new Select.BlobColumn(this, values);
	}

	@Override
	public Select.Values values(Iterable<? extends RowExpression> rows) {
		return new Select.Rows(this, rows);
	}

	@Override
	public CommonTableExpression copy() {
		CommonTableExpression previousCopy = previous == null ? null : previous.copy();
		CharSequence nameCopy = name.toString();
		SelectStatement expressionCopy = expression.copy();
		Iterable<? extends CharSequence> columnsCopy =
			columns == null ? null : ReadonlyIterable.of(columns, CharSequence::toString);

		return
			previousCopy == previous && nameCopy == name
			&& expressionCopy == expression && columnsCopy == columns
				? this
				: new CommonTableExpression(previousCopy, nameCopy, columnsCopy, expressionCopy);
	}

	void appendTo(StringBuilder receptacle) {
		if (previous != null) {
			previous.appendTo(receptacle);
			receptacle.append(", ");
		} else {
			receptacle.append("WITH ");
		}
		SqliteUtilities.appendQuotedName(receptacle, name);

		if (columns != null) {
			receptacle.append('(');
			SqliteUtilities.appendQuotedDelimited(receptacle, columns);
			receptacle.append(')');
		}
		receptacle.append(" AS ");
		expression.appendTo(receptacle);
	}
}
