package dev.ornamental.sqlite.statement;

/**
 * Defines the flavours of <code>INSERT</code> statements.<br>
 * Note that <code>UPDATE</code> and <code>UPDATE OR ABORT</code> perform in the same manner.
 */
public enum UpdateVerb {

	/**
	 * Ordinary <code>UPDATE</code> statement (alias of <code>UPDATE OR ABORT</code>, cancels all the
	 * changes made by the failed statement but the current transaction remains active).
	 */
	UPDATE("UPDATE"),

	/**
	 * <code>UPDATE OR REPLACE</code> statement deletes the rows which would cause conflict
	 * before the update.
	 */
	UPDATE_OR_REPLACE("UPDATE OR REPLACE"),

	/**
	 * <code>UPDATE OR ROLLBACK</code> statement (rolls back the current transaction in case of
	 * conflict).
	 */
	UPDATE_OR_ROLLBACK("UPDATE OR ROLLBACK"),

	/**
	 * <code>UPDATE OR ABORT</code> statement (alias of the simple <code>UPDATE</code>, cancels all the
	 * changes made by the failed statement but the current transaction remains active).
	 */
	UPDATE_OR_ABORT("UPDATE OR ABORT"),

	/**
	 * <code>UPDATE OR FAIL</code> statement (aborts the current statement on conflict, but does not
	 * cancel the changes already made; the current transaction remains active).
	 */
	UPDATE_OR_FAIL("UPDATE OR FAIL"),

	/**
	 * <code>UPDATE OR IGNORE</code> statement (skips the rows which would cause conflict if updated,
	 * the statement does not fail).
	 */
	UPDATE_OR_IGNORE("UPDATE OR IGNORE");

	private final String keyword;

	UpdateVerb(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return keyword;
	}
}
