package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression having the form
 * <code><strong><em>leftExpression</em> <em>OPERATOR</em> <em>rightExpression</em></strong></code>
 * where <code><em>OPERATOR</em></code> is one of the binary operators defined in {@link Operator}.<br>
 * This is a complete SQL expression.
 */
public final class BinaryOperator implements SqlExpression {

	private final SqlExpression left;

	private final SqlExpression right;

	private final Operator operator;

	public BinaryOperator(SqlExpression left, SqlExpression right, Operator operator) {
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		boolean leftParentheses = left.getPrecedence() < operator.getPrecedence();
		boolean rightParentheses = right.getPrecedence() <= operator.getPrecedence();

		SqliteUtilities.parentheses(receptacle, leftParentheses, left::appendTo);
		receptacle.append(' ').append(operator.getSymbol()).append(' ');
		SqliteUtilities.parentheses(receptacle, rightParentheses, right::appendTo);
	}

	@Override
	public int getPrecedence() {
		return operator.getPrecedence();
	}

	@Override
	public BinaryOperator copy() {
		SqlExpression leftCopy = left.copy();
		SqlExpression rightCopy = right.copy();

		return leftCopy == left && rightCopy == right
			? this : new BinaryOperator(leftCopy, rightCopy, operator);
	}
}
