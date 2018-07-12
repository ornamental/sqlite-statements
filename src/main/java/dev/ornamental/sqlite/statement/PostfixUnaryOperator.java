package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression being a postfix unary operator applied to another SQL expression.<br>
 * This is a complete SQL expression.
 */
public final class PostfixUnaryOperator implements SqlExpression {

	private final Operator operator;

	private final SqlExpression operand;

	PostfixUnaryOperator(SqlExpression operand, Operator operator) {
		this.operator = operator;
		this.operand = operand;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		boolean parentheses = operand.getPrecedence() < operator.getPrecedence();
		SqliteUtilities.parentheses(receptacle, parentheses, operand::appendTo);
		receptacle.append(operator.getSymbol());
	}

	@Override
	public int getPrecedence() {
		return operator.getPrecedence();
	}

	@Override
	public SqlExpression copy() {
		SqlExpression operandCopy = operand.copy();

		return operandCopy == operand ? this : new PostfixUnaryOperator(operandCopy, operator);
	}
}
