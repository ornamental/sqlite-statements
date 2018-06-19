package dev.ornamental.sqlite.statement;

/**
 * The class contains the flags determining the mode in which the <code>PRAGMA optimize</code>
 * statement operates (see {@link Pragmas#optimize(CharSequence, int)}).
 */
public final class OptimizeMode {

	/**
	 * The debug mode is on. Lists the optimizations that would be performed but performs none.
	 */
	public static final int DEBUG = 1;

	/**
	 * Runs <code>ANALYZE</code> on the tables for which that might be useful.
	 */
	public static final int ANALYZE = 2;

	/**
	 * Perform those of the default optimizations which might be useful.
	 */
	public static final int DEFAULT = 0xFFFE;

	/**
	 * Find which of all optimizations would be performed (debug mode).
	 */
	public static final int DEBUG_ALL = -1;

	private OptimizeMode() { }
}
