package dev.ornamental.sqlite.statement;

/**
 * Represents a table expression which is given an alias.
 */
public final class AliasedTableExpression implements TableExpression {

	private final TableExpression tableExpression;

	private final CharSequence alias;

	/**
	 * Creates an aliased table expression based on the given table expression and an alias.
	 * @param tableExpression the table epression to give an alias to
	 * @param alias the alias
	 */
	public AliasedTableExpression(TableExpression tableExpression, CharSequence alias) {
		this.tableExpression = tableExpression;
		this.alias = alias;
	}

	@Override
	public boolean isJoin() {
		return false;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		receptacle.append('(');
		tableExpression.appendTo(receptacle);
		receptacle.append(") AS ");
		SqliteUtilities.appendQuotedName(receptacle, alias);
	}

	@Override
	public TableExpression copy() {
		TableExpression tableExpressionCopy = tableExpression.copy();
		CharSequence aliasCopy = alias.toString();

		return tableExpressionCopy == tableExpression && aliasCopy == alias
			? this : new AliasedTableExpression(tableExpressionCopy, aliasCopy);
	}
}
