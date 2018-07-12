package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Functions.function;
import static dev.ornamental.sqlite.statement.Literal.NOW;

/**
 * The class contains static methods returning {@link SqlExpression} instances
 * for date and time function invocations.
 */
public final class DateTimeFunctions {

	private DateTimeFunctions() { }

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>DATE(<em>dateTime</em>{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param dateTime the argument expression
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>DATE</code> function invocation expression
	 */
	public static SqlExpression date(SqlExpression dateTime, SqlExpression... modifiers) {
		return function(StandardFunction.DATE.toString(),
			SqliteUtilities.prependArray(modifiers, dateTime));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>DATE('now'{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>DATE</code> function invocation expression
	 */
	public static SqlExpression dateNow(SqlExpression... modifiers) {
		return function(StandardFunction.DATE.toString(),
			SqliteUtilities.prependArray(modifiers, NOW));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>TIME(<em>dateTime</em>{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param dateTime the argument expression
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>TIME</code> function invocation expression
	 */
	public static SqlExpression time(SqlExpression dateTime, SqlExpression... modifiers) {
		return function(StandardFunction.TIME.toString(),
			SqliteUtilities.prependArray(modifiers, dateTime));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>TIME('now'{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>TIME</code> function invocation expression
	 */
	public static SqlExpression timeNow(SqlExpression... modifiers) {
		return function(StandardFunction.TIME.toString(),
			SqliteUtilities.prependArray(modifiers, NOW));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>DATETIME(<em>dateTime</em>{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param dateTime the argument expression
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>DATETIME</code> function invocation expression
	 */
	public static SqlExpression dateTime(SqlExpression dateTime, SqlExpression... modifiers) {
		return function(StandardFunction.DATETIME.toString(),
			SqliteUtilities.prependArray(modifiers, dateTime));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>DATETIME('now'{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>DATETIME</code> function invocation expression
	 */
	public static SqlExpression dateTimeNow(SqlExpression... modifiers) {
		return function(StandardFunction.DATETIME.toString(),
			SqliteUtilities.prependArray(modifiers, NOW));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>JULIANDAY(<em>dateTime</em>{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param dateTime the argument expression
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>JULIANDAY</code> function invocation expression
	 */
	public static SqlExpression julianDay(SqlExpression dateTime, SqlExpression... modifiers) {
		return function(StandardFunction.JULIANDAY.toString(),
			SqliteUtilities.prependArray(modifiers, dateTime));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>JULIANDAY('now'{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>JULIANDAY</code> function invocation expression
	 */
	public static SqlExpression julianDay(SqlExpression... modifiers) {
		return function(StandardFunction.JULIANDAY.toString(),
			SqliteUtilities.prependArray(modifiers, NOW));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>STRFTIME(<em>format</em>, <em>dateTime</em>{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param format the format string expression
	 * @param dateTime the argument expression
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>STRFTIME</code> function invocation expression
	 */
	public static SqlExpression strftime(
		SqlExpression format, SqlExpression dateTime, SqlExpression... modifiers) {

		return function(StandardFunction.STRFTIME.toString(),
			SqliteUtilities.prependArray(modifiers, format, dateTime));
	}

	/**
	 * Returns the expression having the form<br>
	 * <code><strong>STRFTIME(<em>format</em>, 'now'{, <em>modifier<sub>i</sub></em>})</strong></code>
	 * @param format the format string expression
	 * @param modifiers the optional date and time modifier string expressions
	 * @return the <code>STRFTIME</code> function invocation expression
	 */
	public static SqlExpression strftimeNow(SqlExpression format, SqlExpression... modifiers) {
		return function(StandardFunction.STRFTIME.toString(),
			SqliteUtilities.prependArray(modifiers, format, NOW));
	}
}
