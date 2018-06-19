package dev.ornamental.sqlite.statement;

/**
 * This class contains factory methods for the implementations of {@link ResultElement} interface
 * which are not SQL expressions.
 */
public final class ResultElements {

	private ResultElements() { }

	/**
	 * Returns the result column list denoted by an asterisk (<code><strong>*</strong></code>)
	 * (all the columns of the source table expression).
	 * @return the result column list denoted by an asterisk
	 */
	public static ResultElement all() {
		return AllOfTable.ALL_OF_UNSPECIFIED;
	}

	/**
	 * Returns the result column list designating all the columns of a specific table.
	 * @param tableName the name of the source table (possibly an alias)
	 * @return the column list having the form <code><strong><em>tableName</em>.*</strong></code>
	 */
	public static ResultElement allOf(CharSequence tableName) {
		return new AllOfTable(tableName);
	}
}
