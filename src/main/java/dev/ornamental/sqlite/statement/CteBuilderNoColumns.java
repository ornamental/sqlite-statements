package dev.ornamental.sqlite.statement;

import java.util.Arrays;

/**
 * Represents the initial stage of common table expression definition when only the CTE name
 * is defined for the last common table expression.
 */
public final class CteBuilderNoColumns implements Variable<CteBuilderNoColumns> {

	final CommonTableExpression previous;

	final CharSequence tableName;

	CteBuilderNoColumns(CommonTableExpression previous, CharSequence tableName) {
		this.previous = previous;
		this.tableName = tableName;
	}

	/**
	 * Adds explicit common table expression column names to the last CTE definition.
	 * @param columnNames the names assigned to the column of the last CTE
	 * @return the next step of the common table expression construction flow
	 * containing the information about the CTE column names
	 */
	public CteBuilderWithColumns ofColumns(CharSequence... columnNames) {
		return ofColumns(Arrays.asList(columnNames));
	}

	/**
	 * Adds explicit common table expression column names to the last CTE definition.
	 * @param columnNames the names assigned to the column of the last CTE
	 * @return the next step of the common table expression construction flow
	 * containing the information about the CTE column names
	 */
	public CteBuilderWithColumns ofColumns(Iterable<? extends CharSequence> columnNames) {
		return new CteBuilderWithColumns(this, columnNames);
	}

	/**
	 * Finishes the last common table expression definition by supplying the <code>SELECT</code>
	 * (or <code>VALUE</code>) statement to which the CTE will correspond.<br/>
	 * The result is a complete common table expression. It may be further extended by adding
	 * another CTE starting from the {@link CommonTableExpression#andWith(CharSequence)} method invocation.
	 * @param select the <code>SELECT</code> (or <code>VALUE</code>) statement defining the contents
	 * of the last common table expression
	 * @return the complete common table expression
	 */
	public CommonTableExpression as(SelectStatement select) {
		return new CommonTableExpression(previous, tableName, null, select);
	}

	@Override
	public CteBuilderNoColumns copy() {
		CommonTableExpression previousCopy = previous == null ? null : previous.copy();
		CharSequence tableNameCopy = tableName.toString();

		return previousCopy == previous && tableNameCopy == tableName
			? this : new CteBuilderNoColumns(previousCopy, tableNameCopy);
	}
}
