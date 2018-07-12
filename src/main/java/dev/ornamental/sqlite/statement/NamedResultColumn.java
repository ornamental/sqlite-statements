package dev.ornamental.sqlite.statement;

/**
 * Represents a named SQL expression for use in <code>SELECT</code> statements.<br>
 * This is not an SQL expression itself.
 */
public final class NamedResultColumn implements ResultElement {

	private final SqlExpression expression;

	private final CharSequence columnAlias;

	NamedResultColumn(SqlExpression expression, CharSequence columnAlias) {
		this.expression = expression;
		this.columnAlias = columnAlias;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		expression.appendTo(receptacle);
		receptacle.append(" AS ");
		SqliteUtilities.appendQuotedName(receptacle, columnAlias);
	}

	@Override
	public NamedResultColumn copy() {
		SqlExpression expressionCopy = expression.copy();
		CharSequence columnAliasCopy = columnAlias.toString();

		return expressionCopy == expression && columnAliasCopy == columnAlias
			? this : new NamedResultColumn(expressionCopy, columnAliasCopy);
	}
}
