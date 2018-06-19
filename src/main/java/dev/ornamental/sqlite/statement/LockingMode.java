package dev.ornamental.sqlite.statement;

/**
 * Defines the database file locking modes.
 */
public enum LockingMode {

	/**
	 * The connection unlocks the database file at the conclusion of each read or write transaction.
	 */
	NORMAL,

	/**
	 * The connection never releases the database file locks. On the first read
	 * a shared lock is obtained and held. On the first write an exclusive lock is
	 * obtained and held.
	 */
	EXCLUSIVE
}
