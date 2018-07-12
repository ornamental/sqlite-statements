package dev.ornamental.sqlite.statement;

/**
 * Defines the flavours of <code>INSERT</code> statements.<br>
 * Note that from the two equivalent statements <code>INSERT OR REPLACE</code> and <code>REPLACE</code>
 * only the former is retained while both <code>INSERT</code> and <code>INSERT OR ABORT</code>
 * (which are also equivalent) are present.
 */
public enum InsertVerb {

	/**
	 * Ordinary <code>INSERT</code> statement (alias of <code>INSERT OR ABORT</code>, cancels all the
	 * changes made by the failed statement but the current transaction remains active).
	 */
	INSERT("INSERT"),

	/**
	 * <code>INSERT OR REPLACE</code> statement deletes the rows which would cause conflict
	 * before the insertion. It is an alias of <code>INSERT OR REPLACE</code>.
	 */
	REPLACE("REPLACE"),

	/**
	 * <code>INSERT OR REPLACE</code> statement deletes the rows which would cause conflict
	 * before the insertion. It is an alias of <code>REPLACE</code>.
	 */
	INSERT_OR_REPLACE("INSERT OR REPLACE"),

	/**
	 * <code>INSERT OR ROLLBACK</code> statement (rolls back the current transaction in case of
	 * conflict).
	 */
	INSERT_OR_ROLLBACK("INSERT OR ROLLBACK"),

	/**
	 * <code>INSERT OR ABORT</code> statement (alias of the simple <code>INSERT</code>, cancels all the
	 * changes made by the failed statement but the current transaction remains active).
	 */
	INSERT_OR_ABORT("INSERT OR ABORT"),

	/**
	 * <code>INSERT OR FAIL</code> statement (aborts the current statement on conflict, but does not
	 * cancel the changes already made; the current transaction remains active).
	 */
	INSERT_OR_FAIL("INSERT OR FAIL"),

	/**
	 * <code>INSERT OR IGNORE</code> statement (skips the rows which would cause conflict if inserted,
	 * the statement does not fail).
	 */
	INSERT_OR_IGNORE("INSERT OR IGNORE");

	private final String keyword;

	InsertVerb(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return keyword;
	}
}
