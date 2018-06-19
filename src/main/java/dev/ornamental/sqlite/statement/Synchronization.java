package dev.ornamental.sqlite.statement;

/**
 * Defines the file system synchronization modes used by the <code>PRAGMA synchronous</code>
 * statement (see {@link Pragmas#synchronous(CharSequence, Synchronization)}).
 */
public enum Synchronization {

	/**
	 * SQLite continues without syncing as soon as it has handed data off to the operating system.
	 */
	OFF,

	/**
	 * SQLite will sync at the most critical moments, but less often than in {@link #FULL} mode.
	 */
	NORMAL,

	/**
	 * SQLite will use the xSync method of the VFS to ensure that all content is safely written
	 * to the disk surface prior to continuing.
	 */
	FULL,

	/**
	 * This mode is like {@link #FULL} with the addition that the directory containing
	 * the rollback journal is synced after the journal is unlinked to commit a transaction
	 * in <code>DELETE</code> mode (see {@link JournalMode#DELETE}).
	 */
	EXTRA
}
