package dev.ornamental.sqlite.statement;

/**
 * Defines the pattern-matching operators.
 */
public enum MatchOperator {

	/**
	 * The <code>LIKE</code> operator uses the percent (<code>%</code>) character to match
	 * any number of arbitrary characters (including no characters) and the underscore (<code>_</code>)
	 * character to match one arbitrary character.<br>
	 * Matching is case insensitive unless set otherwise using the <code>PRAGMA case_sensitive_like</code>
	 * statement (see {@link Pragmas#caseSensitiveLike(boolean)}).
	 */
	LIKE,

	/**
	 * The <code>GLOB</code> operator uses the Unix file globbing syntax for its wildcards.
	 * It is always case sensitive.
	 */
	GLOB,

	/**
	 * The <code>REGEXP</code> operator uses the user-defined <code>regexp(Y, X)</code> function
	 * to perform the matching. If no such function is registered, the expression evaluation
	 * will result in an error.
	 */
	REGEXP,

	/**
	 * The <code>MATCH</code> operator may be used is special contexts like full-text index queries.
	 */
	MATCH
}
