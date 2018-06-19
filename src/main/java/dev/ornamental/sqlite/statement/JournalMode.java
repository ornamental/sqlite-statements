package dev.ornamental.sqlite.statement;

/**
 * Defines the supported database journaling modes.
 */
public enum JournalMode {

	/**
	 * The rollback journal file is deleted at the conclusion of each transaction.
	 */
	DELETE,

	/**
	 * The rollback journal file is truncated at the conclusion of each transaction.
	 */
	TRUNCATE,

	/**
	 * The header of the journal is overwritten with zeros at the conclusion of each transaction.
	 */
	PERSIST,

	/**
	 * The rollback journal is stored in volatile RAM.
	 */
	MEMORY,

	/**
	 * A write-ahead log is used instead of a rollback journal.
	 */
	WAL,

	/**
	 *  No rollback journal is created. Note that the <code>ROLLBACK</code> statement
	 *  will have undefined effect if this mode is chosen.
	 */
	OFF
}
