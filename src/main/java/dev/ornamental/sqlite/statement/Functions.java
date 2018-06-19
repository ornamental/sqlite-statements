package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Literal.value;

import java.util.Arrays;
import java.util.Collections;

/**
 * The class contains static factory methods returning {@link SqlExpression} instances
 * for the core function invocations (including the aggregate functions).
 */
public final class Functions {

	private Functions() { }

	/**
	 * Returns an function invocation expression.
	 * @param functionName the name of the function to invoke
	 * @param args the function arguments
	 * @return the expression having the form<br/>
	 * <code><strong><em>functionName</em>([<em>arg<sub>0</sub></em>{, <em>arg<sub>i</sub></em>}])</strong></code>
	 */
	public static SqlExpression function(CharSequence functionName, SqlExpression... args) {
		return new FunctionExpression(functionName, false, Arrays.asList(args));
	}

	/**
	 * Returns an aggregate function invocation expression.
	 * @param aggregateName the name of the function to invoke
	 * @param args the function arguments
	 * @return the expression having the form<br/>
	 * <code><strong><em>aggregateName</em>([<em>arg<sub>0</sub></em>{, <em>arg<sub>i</sub></em>}])</strong></code>
	 */
	public static SqlExpression aggregate(CharSequence aggregateName, SqlExpression... args) {
		// synonym for function(..), as non-DISTINCT aggregate and simple function calls have the same form
		return function(aggregateName, args);
	}

	/**
	 * Returns an aggregate function invocation expression with <code>DISTINCT</code> argument.
	 * @param aggregateName the name of the function to invoke
	 * @param argument the function argument
	 * @return the expression having the form<br/>
	 * <code><strong><em>aggregateName</em>(DISTINCT <em>arg<sub>i</sub></em>)</strong></code>
	 */
	public static SqlExpression aggregateDistinct(CharSequence aggregateName, SqlExpression argument) {
		// DISTINCT is admitted for single-argument aggregates only
		return new FunctionExpression(aggregateName, true, Collections.singleton(argument));
	}

	/**
	 * Returns the <code><strong>COUNT(*)</strong></code> expression.
	 * @return the <code><strong>COUNT(*)</strong></code> expression
	 */
	public static SqlExpression countAll() {
		// COUNT is the only SQLite function taking * as the argument
		return CountAllAggregate.INSTANCE;
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>COUNT(<em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>COUNT</code> aggregate function invocation expression
	 */
	public static SqlExpression count(SqlExpression expression) {
		return aggregate(StandardFunction.COUNT.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>COUNT(DISTINCT <em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>COUNT</code> aggregate function invocation expression
	 */
	public static SqlExpression countDistinct(SqlExpression expression) {
		return aggregateDistinct(StandardFunction.COUNT.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>MAX(<em>expression<sub>0</sub></em>{, <em>expression<sub>i</sub></em>})</strong></code>.
	 * @param expressions the function argument(s)
	 * @return the <code>MAX</code> aggregate function invocation expression
	 * (may serve as aggregate as well as a normal function)
	 */
	public static SqlExpression max(SqlExpression... expressions) {
		return function(StandardFunction.MAX.toString(), expressions);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>MIN(<em>expression<sub>0</sub></em>{, <em>expression<sub>i</sub></em>})</strong></code>.
	 * @param expressions the function argument(s)
	 * @return the <code>MIN</code> function invocation expression
	 * (may serve as aggregate as well as a normal function)
	 */
	public static SqlExpression min(SqlExpression... expressions) {
		return function(StandardFunction.MIN.toString(), expressions);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>AVG(<em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>AVG</code> aggregate function invocation expression
	 */
	public static SqlExpression avg(SqlExpression expression) {
		return aggregate(StandardFunction.AVG.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>AVG(DISTINCT <em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>AVG</code> aggregate function invocation expression
	 */
	public static SqlExpression avgDistinct(SqlExpression expression) {
		return aggregateDistinct(StandardFunction.AVG.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SUM(<em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>SUM</code> aggregate function invocation expression
	 */
	public static SqlExpression sum(SqlExpression expression) {
		return aggregate(StandardFunction.SUM.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SUM(DISTINCT <em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>SUM</code> aggregate function invocation expression
	 */
	public static SqlExpression sumDistinct(SqlExpression expression) {
		return aggregateDistinct(StandardFunction.SUM.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>TOTAL(<em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>TOTAL</code> aggregate function invocation expression
	 */
	public static SqlExpression total(SqlExpression expression) {
		return aggregate(StandardFunction.TOTAL.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>TOTAL(DISTINCT <em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>TOTAL</code> aggregate function invocation expression
	 */
	public static SqlExpression totalDistinct(SqlExpression expression) {
		return aggregateDistinct(StandardFunction.TOTAL.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>GROUP_CONCAT(<em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>GROUP_CONCAT</code> aggregate function invocation expression
	 */
	public static SqlExpression groupConcat(SqlExpression expression) {
		return aggregate(StandardFunction.GROUP_CONCAT.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>GROUP_CONCAT(<em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>GROUP_CONCAT</code> aggregate function invocation expression
	 */
	public static SqlExpression groupConcat(SqlExpression expression, SqlExpression delimiterExpression) {
		return aggregate(StandardFunction.GROUP_CONCAT.toString(), expression, delimiterExpression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>GROUP_CONCAT(<em>expression</em>, <em>delimiter</em>)</strong></code>.
	 * @param expression the function argument defining the items to concatenate
	 * @param delimiter the concatenation delimiter
	 * @return the <code>GROUP_CONCAT</code> aggregate function invocation expression
	 */
	public static SqlExpression groupConcat(SqlExpression expression, String delimiter) {
		return groupConcat(expression, value(delimiter));
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>GROUP_CONCAT(DISTINCT <em>expression</em>)</strong></code>.
	 * @param expression the function argument
	 * @return the <code>GROUP_CONCAT</code> aggregate function invocation expression
	 */
	public static SqlExpression groupConcatDistinct(SqlExpression expression) {
		return aggregateDistinct(StandardFunction.GROUP_CONCAT.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>ABS(<em>expression</em>)</strong></code>
	 * @param expression the function argument
	 * @return the <code>ABS</code> function invocation expression
	 */
	public static SqlExpression abs(SqlExpression expression) {
		return function(StandardFunction.ABS.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>CHANGES()</strong></code>
	 * @return the <code>CHANGES</code> function invocation expression
	 */
	public static SqlExpression changes() {
		return function(StandardFunction.CHANGES.toString());
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>CHAR([<em>expression<sub>1</sub></em>{, <em>expression<sub>i</sub></em>}])</strong></code>
	 * @param expression the function arguments
	 * @return the <code>CHAR</code> function invocation expression
	 */
	public static SqlExpression character(SqlExpression... expression) {
		return function(StandardFunction.CHAR.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>COALESCE([<em>expression<sub>1</sub></em>{, <em>expression<sub>i</sub></em>}])</strong></code>
	 * @param expression the function arguments
	 * @return the <code>COALESCE</code> function invocation expression
	 */
	public static SqlExpression coalesce(SqlExpression... expression) {
		return function(StandardFunction.COALESCE.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>GLOB(<em>pattern</em>, <em>string</em>)</strong></code>
	 * @param pattern the GLOB pattern expression
	 * @param string the expression returning the string to test
	 * @return the <code>GLOB</code> function invocation expression
	 */
	public static SqlExpression glob(SqlExpression pattern, SqlExpression string) {
		return function(StandardFunction.GLOB.toString(), pattern, string);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>HEX(<em>expression</em>)</strong></code>
	 * @param expression the function argument
	 * @return the <code>HEX</code> function invocation expression
	 */
	public static SqlExpression hex(SqlExpression expression) {
		return function(StandardFunction.HEX.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>IFNULL(<em>arg1</em>, <em>arg2</em>)</strong></code>
	 * @param arg1 the first argument expression
	 * @param arg2 the second argument expression
	 * @return the <code>IFNULL</code> function invocation expression
	 */
	public static SqlExpression ifNull(SqlExpression arg1, SqlExpression arg2) {
		return function(StandardFunction.IFNULL.toString(), arg1, arg2);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>INSTR(<em>string</em>, <em>substring</em>)</strong></code>
	 * @param string the string expression being searched in
	 * @param substring the string expression being sought
	 * @return the <code>INSTR</code> function invocation expression
	 */
	public static SqlExpression instr(SqlExpression string, SqlExpression substring) {
		return function(StandardFunction.IFNULL.toString(), string, substring);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LAST_INSERT_ROWID()</strong></code>
	 * @return the <code>LAST_INSERT_ROWID</code> function invocation expression
	 */
	public static SqlExpression lastInsertRowId() {
		return function(StandardFunction.LAST_INSERT_ROWID.toString());
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LENGTH(<em>expression</em>)</strong></code>
	 * @param expression the function argument
	 * @return the <code>LENGTH</code> function invocation expression
	 */
	public static SqlExpression length(SqlExpression expression) {
		return function(StandardFunction.LENGTH.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LIKE(<em>pattern</em>, <em>string</em>)</strong></code>
	 * @param pattern the pattern expression
	 * @param string the matched string expression
	 * @return the <code>LIKE</code> function invocation expression
	 */
	public static SqlExpression like(SqlExpression pattern, SqlExpression string) {
		return function(StandardFunction.LIKE.toString(), pattern, string);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LIKE(<em>pattern</em>, <em>string</em>, <em>escape</em>)</strong></code>
	 * @param pattern the pattern expression
	 * @param string the matched string expression
	 * @param escape the escape expression
	 * @return the <code>LIKE</code> function invocation expression
	 */
	public static SqlExpression like(SqlExpression pattern, SqlExpression string, SqlExpression escape) {
		return function(StandardFunction.LIKE.toString(), pattern, string);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LIKELIHOOD(<em>expression</em>, <em>probability</em>)</strong></code>
	 * @param expression the function argument
	 * @param probability the probability of the {@code expression} returning <code>TRUE</code>
	 * @return the <code>LIKELIHOOD</code> function invocation expression
	 */
	public static SqlExpression likelihood(SqlExpression expression, SqlExpression probability) {
		return function(StandardFunction.LIKELIHOOD.toString(), expression, probability);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LIKELY(<em>expression</em>)</strong></code>
	 * @param expression the function argument
	 * @return the <code>LIKELY</code> function invocation expression
	 */
	public static SqlExpression likely(SqlExpression expression) {
		return function(StandardFunction.LIKELY.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LOAD_EXTENSION(<em>library</em>)</strong></code>
	 * @param library the extension shared library name
	 * @return the <code>LOAD_EXTENSION</code> function invocation expression
	 */
	public static SqlExpression loadExtension(SqlExpression library) {
		return function(StandardFunction.LOAD_EXTENSION.toString(), library);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LOAD_EXTENSION(<em>library</em>, <em>entryPoint</em>)</strong></code>
	 * @param library the extension shared library name
	 * @param entryPoint the entry point name
	 * @return the <code>LOAD_EXTENSION</code> function invocation expression
	 */
	public static SqlExpression loadExtension(SqlExpression library, SqlExpression entryPoint) {
		return function(StandardFunction.LOAD_EXTENSION.toString(), library, entryPoint);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LOWER(<em>expression</em>)</strong></code>
	 * @param expression the argument expression
	 * @return the <code>LOWER</code> function invocation expression
	 */
	public static SqlExpression lower(SqlExpression expression) {
		return function(StandardFunction.LOWER.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LTRIM(<em>string</em>)</strong></code>
	 * @param string the string to trim from the left
	 * @return the <code>LTRIM</code> function invocation expression
	 */
	public static SqlExpression ltrim(SqlExpression string) {
		return function(StandardFunction.LTRIM.toString(), string);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>LTRIM(<em>string</em>, <em>characters</em>)</strong></code>
	 * @param string the string to trim from the left
	 * @param characters the characters to remove
	 * @return the <code>LTRIM</code> function invocation expression
	 */
	public static SqlExpression ltrim(SqlExpression string, SqlExpression characters) {
		return function(StandardFunction.LTRIM.toString(), string, characters);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>NULLIF(<em>arg1</em>, <em>arg2</em>)</strong></code>
	 * @param arg1 the first argument expression
	 * @param arg2 the second argument expression
	 * @return the <code>NULLIF</code> function invocation expression
	 */
	public static SqlExpression nullIf(SqlExpression arg1, SqlExpression arg2) {
		return function(StandardFunction.NULLIF.toString(), arg1, arg2);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>PRINTF(<em>format</em>{, <em>arg<sub>i</sub></em>})</strong></code>
	 * @param format the format spring expression
	 * @param args the arguments to use for formatting
	 * @return the <code>PRINTF</code> function invocation expression
	 */
	public static SqlExpression printf(SqlExpression format, SqlExpression... args) {
		return function(StandardFunction.PRINTF.toString(), SqliteUtilities.prependArray(args, format));
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>RANDOM()</strong></code>
	 * @return the <code>RANDOM</code> function invocation expression
	 */
	public static SqlExpression random() {
		return function(StandardFunction.RANDOM.toString());
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>RANDOMBLOB(<em>length</em>)</strong></code>
	 * @param length the BLOB length expression
	 * @return the <code>RANDOMBLOB</code> function invocation expression
	 */
	public static SqlExpression randomBlob(SqlExpression length) {
		return function(StandardFunction.RANDOMBLOB.toString(), length);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>REPLACE(<em>original</em>, <em>searchString</em>, <em>replacement</em>)</strong></code>
	 * @param original the string to replace in
	 * @param searchString the substring to replace
	 * @param replacement the string to use as replacement
	 * @return the <code>REPLACE</code> function invocation expression
	 */
	public static SqlExpression replace(
		SqlExpression original, SqlExpression searchString, SqlExpression replacement) {

		return function(StandardFunction.REPLACE.toString(), original, searchString, replacement);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>ROUND(<em>expression</em>)</strong></code>
	 * @param expression the argument expression
	 * @return the <code>ROUND</code> function invocation expression
	 */
	public static SqlExpression round(SqlExpression expression) {
		return function(StandardFunction.ROUND.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>ROUND(<em>expression</em>, <em>digits</em>)</strong></code>
	 * @param expression the expression whose result to round
	 * @param digits the expression returning the number of digits to round to
	 * @return the <code>ROUND</code> function invocation expression
	 */
	public static SqlExpression round(SqlExpression expression, SqlExpression digits) {
		return function(StandardFunction.ROUND.toString(), expression, digits);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>RTRIM(<em>string</em>)</strong></code>
	 * @param string the string to trim from the right
	 * @return the <code>RTRIM</code> function invocation expression
	 */
	public static SqlExpression rtrim(SqlExpression string) {
		return function(StandardFunction.RTRIM.toString(), string);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>RTRIM(<em>string</em>, <em>characters</em>)</strong></code>
	 * @param string the string to trim from the right
	 * @param characters the characters to remove
	 * @return the <code>RTRIM</code> function invocation expression
	 */
	public static SqlExpression rtrim(SqlExpression string, SqlExpression characters) {
		return function(StandardFunction.RTRIM.toString(), string, characters);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SOUNDEX(<em>string</em>)</strong></code>
	 * @param string the string argument
	 * @return the <code>SOUNDEX</code> function invocation expression
	 */
	public static SqlExpression soundex(SqlExpression string) {
		return function(StandardFunction.SOUNDEX.toString(), string);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SQLITE_COMPILEOPTION_GET(<em>name</em>)</strong></code>
	 * @param index the compile option index
	 * @return the <code>SQLITE_COMPILEOPTION_GET</code> function invocation expression
	 */
	public static SqlExpression sqliteCompileOptionGet(int index) {
		return function(StandardFunction.SQLITE_COMPILEOPTION_GET.toString(), value(index));
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SQLITE_COMPILEOPTION_USED(<em>name</em>)</strong></code>
	 * @param name the compile option name
	 * @return the <code>SQLITE_COMPILEOPTION_USED</code> function invocation expression
	 */
	public static SqlExpression sqliteCompileOptionUsed(String name) {
		return function(StandardFunction.SQLITE_COMPILEOPTION_USED.toString(), value(name));
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SQLITE_OFFSET(<em>columnName</em>)</strong></code>
	 * @param columnName the column columnName
	 * @return the <code>SQLITE_OFFSET</code> function invocation expression
	 */
	public static SqlExpression sqliteOffset(String columnName) {
		return function(StandardFunction.SQLITE_OFFSET.toString(), value(columnName));
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SQLITE_SOURCE_ID()</strong></code>
	 * @return the <code>SQLITE_SOURCE_ID</code> function invocation expression
	 */
	public static SqlExpression sqliteSourceId() {
		return function(StandardFunction.SQLITE_SOURCE_ID.toString());
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SQLITE_VERSION()</strong></code>
	 * @return the <code>SQLITE_VERSION</code> function invocation expression
	 */
	public static SqlExpression sqliteVersion() {
		return function(StandardFunction.SQLITE_VERSION.toString());
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SUBSTR(<em>string</em>, <em>from</em>)</strong></code>
	 * @param string the string to extract the substring from
	 * @param from the start index expression
	 * @return the <code>SUBSTR</code> function invocation expression
	 */
	public static SqlExpression substr(SqlExpression string, SqlExpression from) {
		return function(StandardFunction.SUBSTR.toString(), string, from);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>SUBSTR(<em>string</em>, <em>from</em>, <em>length</em>)</strong></code>
	 * @param string the string to extract the substring from
	 * @param from the start index expression
	 * @param length the substring length expression
	 * @return the <code>SUBSTR</code> function invocation expression
	 */
	public static SqlExpression substr(SqlExpression string, SqlExpression from, SqlExpression length) {
		return function(StandardFunction.SUBSTR.toString(), string, from, length);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>TOTAL_CHANGES()</strong></code>
	 * @return the <code>TOTAL_CHANGES</code> function invocation expression
	 */
	public static SqlExpression totalChanges() {
		return function(StandardFunction.TOTAL_CHANGES.toString());
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>TRIM(<em>string</em>)</strong></code>
	 * @param string the string to trim
	 * @return the <code>TRIM</code> function invocation expression
	 */
	public static SqlExpression trim(SqlExpression string) {
		return function(StandardFunction.TRIM.toString(), string);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>TRIM(<em>string</em>, <em>characters</em>)</strong></code>
	 * @param string the string to trim
	 * @param characters the characters to remove
	 * @return the <code>TRIM</code> function invocation expression
	 */
	public static SqlExpression trim(SqlExpression string, SqlExpression characters) {
		return function(StandardFunction.TRIM.toString(), string, characters);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>TYPEOF(<em>expression</em>)</strong></code>
	 * @param expression the expression to determine the type of
	 * @return the <code>TYPEOF</code> function invocation expression
	 */
	public static SqlExpression typeOf(SqlExpression expression) {
		return function(StandardFunction.TYPEOF.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>UNICODE(<em>expression</em>)</strong></code>
	 * @param expression the argument expression
	 * @return the <code>UNICODE</code> function invocation expression
	 */
	public static SqlExpression unicode(SqlExpression expression) {
		return function(StandardFunction.UNICODE.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>UNLIKELY(<em>expression</em>)</strong></code>
	 * @param expression the function argument
	 * @return the <code>UNLIKELY</code> function invocation expression
	 */
	public static SqlExpression unlikely(SqlExpression expression) {
		return function(StandardFunction.UNLIKELY.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>UPPER(<em>expression</em>)</strong></code>
	 * @param expression the argument expression
	 * @return the <code>UPPER</code> function invocation expression
	 */
	public static SqlExpression upper(SqlExpression expression) {
		return function(StandardFunction.UPPER.toString(), expression);
	}

	/**
	 * Returns the expression having the form<br/>
	 * <code><strong>ZEROBLOB(<em>length</em>)</strong></code>
	 * @param length the BLOB length expression
	 * @return the <code>ZEROBLOB</code> function invocation expression
	 */
	public static SqlExpression zeroBlob(SqlExpression length) {
		return function(StandardFunction.ZEROBLOB.toString(), length);
	}
}
