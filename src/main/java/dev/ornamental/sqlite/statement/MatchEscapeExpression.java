package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression having the form<br>
 * <code><strong><em>expression</em> [NOT] LIKE|GLOB|REGEXP|MATCH
 * <em>patternExpression</em> ESCAPE escapeExpression</strong></code><br>
 * matching the result of evaluation of the leftmost operand expression
 * to the result of evaluation of the pattern (middle) expression using one
 * of the algorithms defined in {@link MatchOperator}, taking into
 * account the escape character returned by the rightmost operand.<br>
 * It is a complete SQL expression.
 */
public final class MatchEscapeExpression implements SqlExpression {

	private static final int PRECEDENCE = 4;

	private final MatchExpression expression;

	private final SqlExpression escape;

	MatchEscapeExpression(MatchExpression expression, SqlExpression escape) {
		this.expression = expression;
		this.escape = escape;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		expression.appendTo(receptacle);
		boolean escapeParentheses = escape.getPrecedence() <= PRECEDENCE;
		receptacle.append(" ESCAPE ");
		SqliteUtilities.parentheses(receptacle, escapeParentheses, escape::appendTo);
	}

	@Override
	public int getPrecedence() {
		return PRECEDENCE;
	}

	@Override
	public MatchEscapeExpression copy() {
		MatchExpression expressionCopy = expression.copy();
		SqlExpression escapeCopy = escape.copy();

		return expressionCopy == expression && escapeCopy == escape
			? this : new MatchEscapeExpression(expressionCopy, escapeCopy);
	}
}
