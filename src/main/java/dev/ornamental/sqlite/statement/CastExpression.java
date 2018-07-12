package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression having the form<br>
 * <code><strong>CAST (<em>expression</em> AS <em>type</em>)</strong></code>
 * explicitly casting the given expression to the given type.<br>
 * This is a complete SQL expression.
 */
public final class CastExpression implements SqlExpression {

	private final SqlExpression expression;

	private final String type;

	CastExpression(SqlExpression expression, String type) {
		this.expression = expression;
		this.type = type;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		receptacle.append("CAST (");
		expression.appendTo(receptacle);
		receptacle.append(" AS ");
		SqliteUtilities.quoteType(receptacle, type);
		receptacle.append(')');
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE;
	}

	@Override
	public CastExpression copy() {
		SqlExpression expressionCopy = expression.copy();

		return expressionCopy == expression ? this : new CastExpression(expressionCopy, type);
	}
}
