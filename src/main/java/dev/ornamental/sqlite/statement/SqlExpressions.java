package dev.ornamental.sqlite.statement;

import java.util.Arrays;

/**
 * This class contains static factory methods used to create SQL expression instances
 * ({@link SqlExpression}).
 */
public final class SqlExpressions {

	private static final SqlExpression ROWID = column("ROWID");

	private SqlExpressions() { }

	/**
	 * Creates an expression applying the unary prefix <code><strong>+</strong></code> operator to an expression.
	 * @param expression the expression to apply the operator to
	 * @return the no-operation expression
	 */
	public static SqlExpression nop(SqlExpression expression) {
		return new PrefixUnaryOperator(expression, Operator.NOP);
	}

	/**
	 * Creates an expression applying the unary prefix <code><strong>-</strong></code> operator to an expression.
	 * @param expression the expression to apply the operator to
	 * @return the negation expression
	 */
	public static SqlExpression neg(SqlExpression expression) {
		return new PrefixUnaryOperator(expression, Operator.NEG);
	}

	/**
	 * Creates an expression applying the unary prefix <code><strong>~</strong></code> operator to an expression.
	 * @param expression the expression to apply the operator to
	 * @return the bitwise negation (inversion) expression
	 */
	public static SqlExpression inv(SqlExpression expression) {
		return new PrefixUnaryOperator(expression, Operator.INV);
	}

	/**
	 * Creates an expression applying the unary prefix <code><strong>NOT</strong></code> operator to an expression.
	 * @param expression the expression to apply the operator to
	 * @return the logical negation expression
	 */
	public static SqlExpression not(SqlExpression expression) {
		return new PrefixUnaryOperator(expression, Operator.NOT);
	}

	/**
	 * Returns the unnamed binding parameter expression (<code><strong>?</strong></code>).
	 * @return the unnamed binding parameter expression
	 */
	public static SqlExpression parameter() {
		return BindingParameter.NAMELESS;
	}

	/**
	 * Returns a numbered / named binding parameter expression.
	 * @param name the parameter name (or number); it must either be an integer prepended with the '?'
	 * character or be an alphanumeric (latin letters and arabic digits only) prepended with one
	 * of the characters '@', '$', or ':'
	 * @return the numbered or named binding parameter expression
	 */
	public static SqlExpression parameter(String name) {
		return new BindingParameter(name);
	}

	/**
	 * Returns the expression referring to a column of a source table.
	 * @param schemaName the table schema name
	 * @param tableName the table name
	 * @param columnName the referenced column
	 * @return the expression having the form<br>
	 * <code><strong><em>schemaName</em>.<em>tableName</em>.<em>columnName</em></strong></code>
	 */
	public static SqlExpression column(CharSequence schemaName, CharSequence tableName, CharSequence columnName) {
		return new ColumnExpression(schemaName, tableName, columnName);
	}

	/**
	 * Returns the expression referring to a column of a source table.
	 * @param tableName the table name
	 * @param columnName the referenced column
	 * @return the expression having the form<br>
	 * <code><strong><em>tableName</em>.<em>columnName</em></strong></code>
	 */
	public static SqlExpression column(CharSequence tableName, CharSequence columnName) {
		return column(null, tableName, columnName);
	}

	/**
	 * Returns the expression referring to a column of a source table.
	 * @param columnName the referenced column
	 * @return the expression having the form <code><strong><em>columnName</em></strong></code>
	 */
	public static SqlExpression column(CharSequence columnName) {
		return column(null, null, columnName);
	}

	/**
	 * Returns the <code>ROWID</code> column of the source table.
	 * @return the <code>ROWID</code> column of the source table
	 */
	public static SqlExpression rowId() {
		return ROWID;
	}

	/**
	 * Returns the <code>ROWID</code> column of the specified table (or table alias).
	 * @param tableName the nameo of the source table (or an alias)
	 * @return the <code><strong><em>tableName</em>."ROWID"</strong></code> column
	 */
	public static SqlExpression rowIdOf(CharSequence tableName) {
		return column(tableName, "ROWID");
	}

	/**
	 * Returns the old column value defined in <code>DELETE</code> and <code>UPDATE</code> triggers.
	 * @param columnName the name of the old column
	 * @return the <code><strong>OLD.<em>columnName</em></strong></code> column
	 */
	public static SqlExpression oldValue(CharSequence columnName) {
		return column("OLD", columnName);
	}

	/**
	 * Returns the new column value defined in <code>INSERT</code> and <code>UPDATE</code> triggers.
	 * @param columnName the name of the new column
	 * @return the <code><strong>NEW.<em>columnName</em></strong></code> column
	 */
	public static SqlExpression newValue(CharSequence columnName) {
		return column("NEW", columnName);
	}

	/**
	 * Returns the row expression composed of the supplied expressions.
	 * @param expressions the expression to create a row of
	 * @return the expression having the form <code><strong>(<em>expression<sub>0</sub></em>{,
	 * <em>expression<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOf(SqlExpression... expressions) {
		return rowOf(Arrays.asList(expressions));
	}

	/**
	 * Returns the row expression composed of the supplied expressions.
	 * @param expressions the expression to create a row of
	 * @return the expression having the form <code><strong>(<em>expression<sub>0</sub></em>{,
	 * <em>expression<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOf(Iterable<? extends SqlExpression> expressions) {
		return new RowExpression.OfExpressions(expressions);
	}

	/**
	 * Returns the row expression composed of the supplied numbers.
	 * @param elements the numbers to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOf(int... elements) {
		return new RowExpression.OfIntegers(elements, false);
	}

	/**
	 * Returns the row expression composed of the supplied numbers.
	 * @param elements the numbers to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOf(float... elements) {
		return new RowExpression.OfFloats(elements, false);
	}

	/**
	 * Returns the row expression composed of the supplied numbers.
	 * @param elements the numbers to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOf(long... elements) {
		return new RowExpression.OfLongs(elements, false);
	}

	/**
	 * Returns the row expression composed of the supplied numbers.
	 * @param elements the numbers to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOf(double... elements) {
		return new RowExpression.OfDoubles(elements, false);
	}

	/**
	 * Returns the row expression composed of the supplied strings.
	 * @param elements the strings to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOf(CharSequence... elements) {
		return rowOfStrings(Arrays.asList(elements));
	}

	/**
	 * Returns the row expression composed of the supplied BLOBs.
	 * @param elements the BLOBs to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOf(byte[]... elements) {
		return rowOfBlobs(Arrays.asList(elements));
	}

	/**
	 * Returns the row expression composed of the supplied strings.
	 * @param elements the strings to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOfStrings(Iterable<? extends CharSequence> elements) {
		return new RowExpression.OfStrings(elements);
	}

	/**
	 * Returns the row expression composed of the supplied numbers.
	 * @param elements the numbers to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOfNumbers(Iterable<? extends Number> elements) {
		return new RowExpression.OfNumbers(elements);
	}

	/**
	 * Returns the row expression composed of the supplied BLOBs.
	 * @param elements the BLOBs to create a row of
	 * @return the expression having the form <code><strong>(<em>element<sub>0</sub></em>{,
	 * <em>element<sub>i</sub></em>})</strong></code>
	 */
	public static RowExpression rowOfBlobs(Iterable<byte[]> elements) {
		return new RowExpression.OfBlobs(elements);
	}

	/**
	 * Returns an expression having the form<br>
	 * <code><strong>EXISTS (<em>selectStatement</em>)</strong></code>.
	 * @param selectStatement the <code>SELECT</code> (or <code>VALUES</code>) statement whose result
	 * is to be checked for emptiness
	 * @return the <code>EXISTS</code> statement
	 */
	public static SqlExpression exists(SelectStatement selectStatement) {
		return new ExistsExpression(false, selectStatement);
	}

	/**
	 * Returns an expression having the form<br>
	 * <code><strong>NOT EXISTS (<em>selectStatement</em>)</strong></code>.
	 * @param selectStatement the <code>SELECT</code> (or <code>VALUES</code>) statement whose result
	 * is to be checked for emptiness
	 * @return the <code>NOT EXISTS</code> statement
	 */
	public static SqlExpression notExists(SelectStatement selectStatement) {
		return new ExistsExpression(true, selectStatement);
	}

	/**
	 * Returns the first stage of <code>CASE</code> expression construction
	 * for an expression without a comparand (starting with <code>CASE WHEN</code>).
	 * @return a <code>CASE</code> statement stub
	 */
	public static CaseExpression.Stub caseOf() {
		return new CaseExpression.Stub(null);
	}

	/**
	 * Returns the first stage of <code>CASE</code> expression construction
	 * for an expression with a comparand (starting with <code>CASE <em>comparand</em> WHEN</code>).
	 * @param comparand the comparand expression
	 * @return a <code>CASE</code> statement stub
	 */
	public static CaseExpression.Stub caseOf(SqlExpression comparand) {
		return new CaseExpression.Stub(comparand);
	}

	/**
	 * Returns the <code>CAST</code> expression having the form<br>
	 * <code><strong>CAST (<em>expression</em> AS <em>type</em>)</strong></code>.
	 * @param expression the expression to cast explicitly
	 * @param type the type to cast to
	 * @return the <code>CAST</code> expression
	 */
	public static SqlExpression cast(SqlExpression expression, String type) {
		return new CastExpression(expression, type);
	}
}
