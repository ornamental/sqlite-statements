package dev.ornamental.sqlite.statement;

/**
 * Represents a collation sequence name. The three predefined SQLite collation sequences
 * are available through constants {@link #BINARY}, {@link #NOCASE}, and {@link #RTRIM}.
 */
public final class Collation {

	/**
	 * The standard <code>BINARY</code> collation sequence.
	 */
	public static final Collation BINARY = new Collation("BINARY");

	/**
	 * The standard <code>NOCASE</code> collation sequence.
	 */
	public static final Collation NOCASE = new Collation("NOCASE");

	/**
	 * The standard <code>RTRIM</code> collation sequence.
	 */
	public static final Collation RTRIM = new Collation("RTRIM");

	private final String collationName;

	private Collation(String collationName) {
		this.collationName = collationName;
	}

	/**
	 * Returns the name of the collation sequence.
	 * @return the name of the collation sequence
	 */
	public String getName() {
		return collationName;
	}

	/**
	 * Creates a new collation sequence name object.
	 * @param collationName the collation sequence name
	 * @return the new collation sequence name object
	 */
	public static Collation named(String collationName) {
		return new Collation(collationName);
	}

	void appendTo(StringBuilder receptacle) {
		SqliteUtilities.quoteNameIfNecessary(receptacle, collationName);
	}
}
