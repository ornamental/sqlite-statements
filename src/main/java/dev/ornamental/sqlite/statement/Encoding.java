package dev.ornamental.sqlite.statement;

/**
 * Defines the database encodings supported by SQLite.
 */
public enum Encoding {

	/**
	 * UTF-8 encoding
	 */
	UTF_8("UTF-8"),

	/**
	 * UTF-16 encoding with native byte order
	 */
	UTF_16_NATIVE("UTF-16"),

	/**
	 * UTF-16 encoding with little-endian byte order
	 */
	UTF_16_LE("UTF-16le"),

	/**
	 * UTF-16 encoding with big-endian byte order
	 */
	UTF_16_BE("UTF-16be");

	private final String encoding;

	Encoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public String toString() {
		return encoding;
	}
}
