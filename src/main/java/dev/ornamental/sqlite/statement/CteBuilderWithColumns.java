package dev.ornamental.sqlite.statement;

/**
 * Represents a stage of common table expression definition with explicitly specified column names
 * but yet no <code>SELECT</code> (or <code>VALUES</code>) statement corresponding to the CTE contents.
 */
public final class CteBuilderWithColumns implements Variable<CteBuilderWithColumns> {

	private final CteBuilderNoColumns previous;

	private final Iterable<? extends CharSequence> columnNames;

	CteBuilderWithColumns(CteBuilderNoColumns previous, Iterable<? extends CharSequence> columnNames) {
		this.previous = previous;
		this.columnNames = columnNames;
	}

	/**
	 * Finishes the last common table expression definition by supplying the <code>SELECT</code>
	 * (or <code>VALUE</code>) statement to which the CTE will correspond.<br>
	 * The result is a complete common table expression. It may be further extended by adding
	 * another CTE starting from the {@link CommonTableExpression#andWith(CharSequence)} method invocation.
	 * @param select the <code>SELECT</code> (or <code>VALUE</code>) statement defining the contents
	 * of the last common table expression
	 * @return the complete common table expression
	 */
	public CommonTableExpression as(SelectStatement select) {
		return new CommonTableExpression(previous.previous, previous.tableName, columnNames, select);
	}

	@Override
	public CteBuilderWithColumns copy() {
		CteBuilderNoColumns previousCopy = previous.copy();
		Iterable<? extends CharSequence> columnNamesCopy =
			ReadonlyIterable.of(columnNames, CharSequence::toString);

		return previousCopy == previous && columnNamesCopy == columnNames
			? this : new CteBuilderWithColumns(previousCopy, columnNamesCopy);
	}
}
