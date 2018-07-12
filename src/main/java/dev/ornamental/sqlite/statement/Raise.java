package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression having the form<br>
 * <code><strong>RAISE (IGNORE)</strong></code> or <br>
 * <code><strong>RAISE (ROLLBACK|ABORT|FAIL, <em>message</em>)</strong></code>.<br>
 * Use the static methods of this class to obtain expression instances.<br>
 * This is a complete SQL expression only usable in <code>CREATE TRIGGER</code> statement
 * (more precisely, in the trigger body nested statements).
 */
public final class Raise implements SqlExpression {

	private static final Raise IGNORE = new Raise("IGNORE", null);

	private final String verb;

	private final CharSequence message;

	private Raise(String verb, CharSequence message) {
		this.verb = verb;
		this.message = message;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		receptacle.append("RAISE(").append(verb);
		if (message != null) {
			receptacle.append(", ").append('\'');
			SqliteUtilities.escapeSingleQuotes(receptacle, message);
			receptacle.append('\'');
		}
		receptacle.append(')');
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE;
	}

	@Override
	public SqlExpression copy() {
		CharSequence messageCopy = message == null ? null : message.toString();

		return messageCopy == message ? this : new Raise(verb, messageCopy);
	}

	/**
	 * Returns the <code>RAISE (IGNORE)</code> statement making the remainder of the current trigger program,
	 * the statement that caused the trigger program to execute, and any subsequent trigger programs that
	 * would have been executed be abandoned. No error is issued, and the current transaction remains active.
	 * @return the <code>RAISE (IGNORE)</code> statement
	 */
	public static Raise raiseIgnore() {
		return IGNORE;
	}

	/**
	 * Returns the <code>RAISE</code> statement making the current trigger
	 * statement fail and roll the transaction back.
	 * @param message the error message
	 * @return the <code><strong>RAISE (ROLLBACK, <em>message</em>)</strong></code> statement
	 */
	public static Raise raiseRollback(String message) {
		return createRequireMessage("ROLLBACK", message);
	}

	/**
	 * Returns the <code>RAISE</code> statement making the current trigger
	 * statement fail cancelling the changes made by the causing statement but keeping
	 * the current transaction active.
	 * @param message the error message
	 * @return the <code><strong>RAISE (ABORT, <em>message</em>)</strong></code> statement
	 */
	public static Raise raiseAbort(String message) {
		return createRequireMessage("ABORT", message);
	}

	/**
	 * Returns the <code>RAISE</code> statement making the current trigger
	 * statement fail preserving the changes made by the causing statement and keeping
	 * the current transaction active.
	 * @param message the error message
	 * @return the <code><strong>RAISE (FAIL, <em>message</em>)</strong></code> statement
	 */
	public static Raise raiseFail(String message) {
		return createRequireMessage("FAIL", message);
	}

	private static Raise createRequireMessage(String verb, String message) {
		if (message == null) {
			throw new IllegalArgumentException("A message must be specified.");
		}
		return new Raise(verb, message);
	}
}
