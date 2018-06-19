package dev.ornamental.sqlite.statement;

/**
 * Defines possible automatic vacuum operation options.
 */
public enum AutoVacuumMode {

	/**
	 * Automatic vacuum is disabled.
	 */
	NONE,

	/**
	 * Automatic vacuum is automatically performed at every transaction commit.
	 */
	FULL,

	/**
	 * The data needed to perform the vacuuming is stored in the database but remains unused
	 * unless vacuuming is specifically invoked by a <code>PRAGMA incremental_vacuum</code> statement
	 * (see the {@link Pragmas#incrementalVacuum()} method and its overloads).
	 */
	INCREMENTAL
}
