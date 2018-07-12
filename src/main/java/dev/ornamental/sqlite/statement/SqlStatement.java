package dev.ornamental.sqlite.statement;

/**
 * The interface implemented by the classes representing SQL statements.<br>
 * Note that the statements which may be <code>EXPLAIN</code>ed implement
 * the child marker interface {@link ExplicableStatement}; the statements
 * which may be used in trigger bodies implement the {@link TriggerStatement} interface.
 */
public interface SqlStatement {

	/**
	 * Returns a deep copy of this statement with all the mutable fields having their current values fixed.
	 * @return the {@link SqlStatement} which will result in the same {@link String} when {@link #build()} is invoked
	 * as would this {@link SqlStatement} if {@link #build()} were invoked on it at this moment; the resulting
	 * instance will not be affected by any future changes to any of the placeholders used in this {@link SqlStatement}.
	 */
	// TODO [LOW] @throws IllegalStateException if at least one placeholder does not have an admissible value set
	SqlStatement copy();

	/**
	 * Appends the string representation of this statement to the given {@link StringBuilder}.
	 * The current values of the employed placeholders (if any) are used.
	 * @param receptacle the {@link StringBuilder} to append the statement to
	 * @throws IllegalStateException if at least one placeholder does not have an admissible value set
	 */
	void build(StringBuilder receptacle);

	/**
	 * Produces a {@link String} representation of this statement ready to be passed to the SQLite engine.
	 * The current values of the employed placeholders (if any) are used.
	 * @return the {@link String} containing the statement to execute against the SQLite engine
	 * @throws IllegalStateException if at least one placeholder does not have an admissible value set
	 */
	default String build() throws IllegalStateException {
		StringBuilder receptacle = new StringBuilder();
		build(receptacle);
		return receptacle.toString();
	}
}
