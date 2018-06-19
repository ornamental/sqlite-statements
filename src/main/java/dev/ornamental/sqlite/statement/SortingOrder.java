package dev.ornamental.sqlite.statement;

/**
 * Defines the two sorting orders.
 */
public enum SortingOrder {

	/**
	 * The ascending sorting order
	 */
	ASC("ASC"),

	/**
	 * The descending sorting order
	 */
	DESC("DESC");

	private final String keyword;

	SortingOrder(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return keyword;
	}
}
