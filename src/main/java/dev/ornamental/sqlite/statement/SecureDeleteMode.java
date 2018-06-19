package dev.ornamental.sqlite.statement;

/**
 * Defines the modes of data deletion from the database file.
 */
public enum SecureDeleteMode {

	/**
	 * The data is not overwritten with zeroes and may be read unless overwritten by other data.
	 */
	OFF,

	/**
	 * The deleted data are overwritten with zeroes if that does not increase the amount of I/O.
	 */
	FAST,

	/**
	 * The deleted data are always overwritten with zeroes.
	 */
	ON
}
