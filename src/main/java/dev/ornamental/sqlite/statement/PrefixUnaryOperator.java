package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression being a prefix unary operator applied to another SQL expression.<br>
 * This is a complete SQL expression.
 */
public final class PrefixUnaryOperator implements SqlExpression {

	private final Operator operator;

	private final SqlExpression operand;

	public PrefixUnaryOperator(SqlExpression operand, Operator operator) {
		this.operator = operator;
		this.operand = operand;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		receptacle.append(operator.getSymbol());
		boolean parentheses = operand.getPrecedence() < operator.getPrecedence();
		SqliteUtilities.parentheses(receptacle, parentheses, operand::appendTo);
	}

	@Override
	public int getPrecedence() {
		return operator.getPrecedence();
	}

	@Override
	public SqlExpression copy() {
		SqlExpression operandCopy = operand.copy();

		return operandCopy == operand ? this : new PrefixUnaryOperator(operandCopy, operator);
	}
}
