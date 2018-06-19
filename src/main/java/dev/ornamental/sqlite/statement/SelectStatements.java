package dev.ornamental.sqlite.statement;

import java.util.Arrays;
import java.util.Collections;

/**
 * The class contains static factory methods serving as the starting points for construction
 * of <code>SELECT</code> and <code>VALUE</code> statements.<br/>
 */
public final class SelectStatements {

	private SelectStatements() { }

	/**
	 * Constructs the simplest form of <code>SELECT</code> statement having
	 * no source table expression and simply enumerating the result columns to return.
	 * The source table expression and other elements of the statement may be added at
	 * the later stages of its construction.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param columns the columns of the output row set; see {@link ResultElement}
	 * for possible alternatives
	 * @return the <code>SELECT</code> statement having the form<br/>
	 * <code><strong>SELECT <em>resultColumn<sub>0</sub></em>{, <em>resultColumn<sub>i</sub></em>}</strong></code>
	 */
	public static Select.NoSource select(ResultElement... columns) {
		return select(Arrays.asList(columns));
	}

	/**
	 * Constructs the simplest form of <code>SELECT</code> statement having
	 * no source table expression and simply enumerating the result columns to return.
	 * The source table expression and other elements of the statement may be added at
	 * the later stages of its construction.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param columns the columns of the output row set; see {@link ResultElement}
	 * for possible alternatives
	 * @return the <code>SELECT</code> statement having the form<br/>
	 * <code><strong>SELECT <em>resultColumn<sub>0</sub></em>{,
	 * <em>resultColumn<sub>i</sub></em>}</strong></code>
	 */
	public static Select.NoSource select(Iterable<? extends ResultElement> columns) {
		return new Select.NoSource(false, columns);
	}

	/**
	 * Constructs the simplest form of <code>SELECT DISTINCT</code> statement having
	 * no source table expression and simply enumerating the result columns to return.
	 * The source table expression and other elements of the statement may be added at
	 * the later stages of its construction.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param columns the columns of the output row set; see {@link ResultElement}
	 * for possible alternatives
	 * @return the <code>SELECT</code> statement having the form<br/>
	 * <code><strong>SELECT DISTINCT <em>resultColumn<sub>0</sub></em>{,
	 * <em>resultColumn<sub>i</sub></em>}</strong></code>
	 */
	public static Select.NoSource selectDistinct(ResultElement... columns) {
		return selectDistinct(Arrays.asList(columns));
	}

	/**
	 * Constructs the simplest form of <code>SELECT DISTINCT</code> statement having
	 * no source table expression and simply enumerating the result columns to return.
	 * The source table expression and other elements of the statement may be added at
	 * the later stages of its construction.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param columns the columns of the output row set; see {@link ResultElement}
	 * for possible alternatives
	 * @return the <code>SELECT</code> statement having the form<br/>
	 * <code><strong>SELECT DISTINCT <em>resultColumn<sub>0</sub></em>{,
	 * <em>resultColumn<sub>i</sub></em>}</strong></code>
	 */
	public static Select.NoSource selectDistinct(Iterable<? extends ResultElement> columns) {
		return new Select.NoSource(true, columns);
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single column of integral values.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param values the values to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
	 */
	public static Select.Values values(int... values) {
		return new Select.IntegerColumn(values, false);
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single column of integral values.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param values the values to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
	 */
	public static Select.Values values(long... values) {
		return new Select.LongColumn(values, false);
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single column of floating-point values.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param values the values to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
	 */
	public static Select.Values values(float... values) {
		return new Select.FloatColumn(values, false);
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single column of floating-point values.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param values the values to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
	 */
	public static Select.Values values(double... values) {
		return new Select.DoubleColumn(values, false);
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single column of text values.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param values the values to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
	 */
	public static Select.Values values(CharSequence... values) {
		return new Select.TextColumn(Arrays.asList(values));
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single column of text values.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param values the values to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
	 */
	public static Select.Values stringValues(Iterable<? extends CharSequence> values) {
		return new Select.TextColumn(values);
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single column of numeric values.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param values the values to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
	 */
	public static Select.Values numericValues(Iterable<? extends Number> values) {
		return new Select.NumericColumn(values);
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single column of BLOBs.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param values the values to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>value<sub>0</sub></em>){, (<em>value<sub>i</sub></em>)}</strong></code>
	 */
	public static Select.Values blobValues(Iterable<byte[]> values) {
		return new Select.BlobColumn(values);
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single row consisting
	 * of the specified expressions.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param expressions the expressions to be arranged in a single row in the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>expression<sub>0</sub></em>{, (<em>expression<sub>i</sub></em>)})</strong></code>
	 */
	public static Select.Values valuesInRow(SqlExpression... expressions) {
		return new Select.Rows(Collections.singleton(SqlExpressions.rowOf(expressions)));
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning a single row consisting
	 * of the specified expressions.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param expressions the expressions to be arranged in a single row in the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>expression<sub>0</sub></em>{, <em>expression<sub>i</sub></em>})</strong></code>
	 */
	public static Select.Values valuesInRow(Iterable<? extends SqlExpression> expressions) {
		return new Select.Rows(Collections.singleton(SqlExpressions.rowOf(expressions)));
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning the specified rows.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param rows the rows to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>expression<sub>0,0</sub></em>{, <em>expression<sub>0,j</sub></em>}){,
	 * (<em>expression<sub>i,0</sub></em>{, <em>expression<sub>i,j</sub></em>})}</strong></code>
	 */
	public static Select.Values values(RowExpression... rows) {
		return new Select.Rows(Arrays.asList(rows));
	}

	/**
	 * Constructs a <code>VALUES</code> statement returning the specified rows.<br/>
	 * The result is a complete SQL statement and expression.
	 * @param rows the rows to be returned by the <code>VALUES</code> statement
	 * @return the <code>VALUES</code> statement having the form<br/>
	 * <code><strong>VALUES (<em>expression<sub>0,0</sub></em>{, <em>expression<sub>0,j</sub></em>}){,
	 * (<em>expression<sub>i,0</sub></em>{, <em>expression<sub>i,j</sub></em>})}</strong></code>
	 */
	public static Select.Values values(Iterable<? extends RowExpression> rows) {
		return new Select.Rows(rows);
	}
}
