package dev.ornamental.sqlite.statement;

/**
 * Defines the temporary store options for the temporary database.
 */
public enum TempStore {

	/**
	 * The default option specified upon SQLite compilation is used.
	 */
	DEFAULT,

	/**
	 * The temporary database is stored in a file.
	 */
	FILE,

	/**
	 * The temporary database is stored in volatile RAM.
	 */
	MEMORY
}
