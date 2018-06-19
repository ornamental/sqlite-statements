package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression having the form<br/>
 * <code><strong><em>comparandExpression</em>
 * [NOT] BETWEEN <em>minExpression</em> AND <em>maxExpression</em></strong></code>.<br/>
 * This is a complete SQL expression.
 */
public final class BetweenExpression implements SqlExpression {

	private static final int PRECEDENCE = 4;

	private final SqlExpression comparand;

	private final SqlExpression min;

	private final SqlExpression max;

	private final boolean not;

	BetweenExpression(boolean not, SqlExpression comparand, SqlExpression min, SqlExpression max) {
		this.comparand = comparand;
		this.min = min;
		this.max = max;
		this.not = not;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		boolean comparandParentheses = comparand.getPrecedence() < PRECEDENCE;
		boolean minParentheses = min.getPrecedence() <= PRECEDENCE;
		boolean maxParentheses = max.getPrecedence() <= PRECEDENCE;

		SqliteUtilities.parentheses(receptacle, comparandParentheses, comparand::appendTo);
		if (not) {
			receptacle.append(" NOT");
		}
		receptacle.append(" BETWEEN ");
		SqliteUtilities.parentheses(receptacle, minParentheses, min::appendTo);
		receptacle.append(" AND ");
		SqliteUtilities.parentheses(receptacle, maxParentheses, max::appendTo);
	}

	@Override
	public int getPrecedence() {
		return PRECEDENCE;
	}

	@Override
	public BetweenExpression copy() {
		SqlExpression comparandCopy = comparand.copy();
		SqlExpression minCopy = min.copy();
		SqlExpression maxCopy = max.copy();

		return comparandCopy == comparand && minCopy == min && maxCopy == max
			? this : new BetweenExpression(not, comparandCopy, minCopy, maxCopy);
	}
}
