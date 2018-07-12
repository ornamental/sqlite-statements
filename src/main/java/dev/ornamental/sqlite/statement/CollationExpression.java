package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression of form<br>
 * <code><strong><em>expression</em> COLLATE <em>collationName</em></strong></code><br>
 * applying the specified collation sequence to the result of expression evaluation.<br>
 * This is a complete SQL expression.
 */
public final class CollationExpression implements SqlExpression {

	private static final int PRECEDENCE = 11;

	private final SqlExpression expression;

	private final Collation collation;

	CollationExpression(SqlExpression expression, Collation collation) {
		this.expression = expression;
		this.collation = collation;
	}

	@Override
	public int getPrecedence() {
		return PRECEDENCE;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		boolean parentheses = expression.getPrecedence() < PRECEDENCE;
		SqliteUtilities.parentheses(receptacle, parentheses, expression::appendTo);
		receptacle.append(" COLLATE ");
		collation.appendTo(receptacle);
	}

	@Override
	public CollationExpression copy() {
		SqlExpression expressionCopy = expression.copy();

		return expressionCopy == expression
			? this : new CollationExpression(expressionCopy, collation);
	}
}
