package dev.ornamental.sqlite.statement;

/**
 * Defines the supported <code>JOIN</code> types.<br/>
 * Note that <code>JOIN</code> and comma (<code>,</code>) are not included as they
 * are just less explicit aliases to <code>INNER JOIN</code>.
 */
public enum JoinType {

	/**
	 * <code>INNER JOIN</code>
	 */
	INNER("INNER JOIN"),

	/**
	 * <code>LEFT JOIN</code>
	 */
	LEFT("LEFT JOIN"),

	/**
	 * <code>CROSS JOIN</code>
	 */
	CROSS("CROSS JOIN");

	private final String keyword;

	JoinType(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return keyword;
	}
}
