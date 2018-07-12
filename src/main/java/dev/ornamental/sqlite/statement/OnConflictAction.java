package dev.ornamental.sqlite.statement;

/**
 * Defines possible actions for SQLite to perform in case a table (column) constraint fails.
 */
public enum OnConflictAction {

	/**
	 * The current transaction (explicit or implicit) is rolled back.
	 */
	ROLLBACK,

	/**
	 * The current statement is aborted (any changes it may have produced are cancelled).
	 * The changes made by the previous statements of the same (explicit) transaction are preserved,
	 * the transaction itself remains active.
	 */
	ABORT,

	/**
	 * The current statement fails but no changes it may have produced prior to the failure are cancelled.
	 * The changes made by the previous statements of the same (explicit) transaction are preserved,
	 * the transaction itself remains active.
	 */
	FAIL,

	/**
	 * The row leading to the constraint violation is skipped, the statement continues processing
	 * the rest of the rows.
	 */
	IGNORE,

	/**
	 * For the primary key and uniqueness constraints this action leads to deletion of the previously
	 * existing rows causing the conflict to occur.<br>
	 * For the non-nullity constraint, the inadmissible <code>NULL</code> value is replaced with
	 * the default value for the column (if it has none or the default is <code>NULL</code>,
	 * works as {@link #ABORT}).<br>
	 * For a check constraint this action is equivalent to {@link #ABORT}.
	 */
	REPLACE
}
