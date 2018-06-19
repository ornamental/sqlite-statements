package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression having the form<br/>
 * <code><strong><em>expression</em> [NOT] LIKE|GLOB|REGEXP|MATCH
 * <em>patternExpression</em></strong></code><br/>
 * matching the result of evaluation of the left operand expression
 * to the result of evaluation of the pattern expression using one
 * of the algorithms defined in {@link MatchOperator}.<br/>
 * It is a complete SQL expression.
 */
public final class MatchExpression implements SqlExpression {

	private static final int PRECEDENCE = 4;

	private final MatchOperator operator;

	private final boolean not;

	private final SqlExpression left;

	private final SqlExpression right;

	MatchExpression(MatchOperator operator, boolean not, SqlExpression left, SqlExpression right) {
		this.operator = operator;
		this.not = not;
		this.left = left;
		this.right = right;
	}

	/**
	 * Adds an <code>ESCAPE</code> clause to the expression using the specified character.
	 * @param ch the character to use as the escape symbol
	 * @return the match expression having the form<br/>
	 * <code><em>expression</em> [NOT] LIKE|GLOB|REGEXP|MATCH
	 * <em>patternExpression</em> <strong>ESCAPE <em>ch</em></strong></code><br/>
	 * where <code><em>ch</em></code> is the specified character
	 */
	public MatchEscapeExpression escape(char ch) {
		return escape(Literal.value(String.valueOf(ch)));
	}

	/**
	 * Adds an <code>ESCAPE</code> clause to the expression.
	 * @param escapeExpression the expression whose evaluation result must be used as the escape symbol
	 * @return the match expression having the form<br/>
	 * <code><em>expression</em> [NOT] LIKE|GLOB|REGEXP|MATCH
	 * <em>patternExpression</em> <strong>ESCAPE <em>escapeExpression</em></strong></code><br/>
	 */
	public MatchEscapeExpression escape(SqlExpression escapeExpression) {
		return new MatchEscapeExpression(this, escapeExpression);
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		boolean leftParentheses = left.getPrecedence() < PRECEDENCE;
		boolean rightParentheses = right.getPrecedence() <= PRECEDENCE;

		SqliteUtilities.parentheses(receptacle, leftParentheses, left::appendTo);
		if (not) {
			receptacle.append(" NOT");
		}
		receptacle.append(' ').append(operator.toString()).append(' ');
		SqliteUtilities.parentheses(receptacle, rightParentheses, right::appendTo);
	}

	@Override
	public int getPrecedence() {
		return PRECEDENCE;
	}

	@Override
	public MatchExpression copy() {
		SqlExpression leftCopy = left.copy();
		SqlExpression rightCopy = right.copy();

		return leftCopy == left && rightCopy == right
			? this : new MatchExpression(operator, not, leftCopy, rightCopy);
	}
}
