package dev.ornamental.sqlite.statement;

/**
 * Defines the operations on row sets. Note that in SQLite the row set operators
 * have equal precedence so the result is evaluated from left to right.
 */
public enum CompoundOperator {

	/**
	 * The <code><strong>UNION</strong></code> operator performing row set union
	 * (with row deduplication).
	 */
	UNION("UNION"),

	/**
	 * The <code><strong>UNION ALL</strong></code> operator performing row multiset union
	 * (without row deduplication).
	 */
	UNION_ALL("UNION ALL"),

	/**
	 * The <code><strong>INTERSECT</strong></code> operator performing row set intersection.
	 */
	INTERSECT("INTERSECT"),

	/**
	 * The <code><strong>EXCEPT</strong></code> operator performing row set subtraction.
	 */
	EXCEPT("EXCEPT");

	private final String keyword;

	CompoundOperator(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return keyword;
	}
}
