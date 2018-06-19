package dev.ornamental.sqlite.statement;

/**
 * Defines the modes of <code>PRAGMA wal_checkpoint</code> command
 * (see {@link Pragmas#walCheckpoint(CharSequence, WalCheckpoint)}).
 */
public enum WalCheckpoint {

	/**
	 * Checkpoint as many frames as possible without waiting for any database
	 * readers or writers to finish.
	 */
	PASSIVE,

	/**
	 * Block until there is no database writer and all the readers are reading
	 * from the most recent database snapshot, then checkpoint all the frames
	 * in the log file and sync the database file.
	 */
	FULL,

	/**
	 * This mode works the same way as {@link #FULL} with the addition that after checkpointing
	 * the log file it blocks until all the readers are finished with the log file.
	 */
	RESTART,

	/**
	 * This mode works the same way as {@link #RESTART} with the addition that the WAL file
	 * is truncated to zero bytes upon successful completion.
	 */
	TRUNCATE
}
