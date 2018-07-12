package dev.ornamental.sqlite.statement;

/**
 * Represents the <code><strong>COUNT(*)</strong></code> SQL expression.<br>
 * This is a complete SQL expression.
 */
public final class CountAllAggregate implements SqlExpression {

	static final CountAllAggregate INSTANCE = new CountAllAggregate();

	private CountAllAggregate() { }

	@Override
	public void appendTo(StringBuilder receptacle) {
		receptacle.append("COUNT(*)");
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE;
	}

	@Override
	public SqlExpression copy() {
		return this;
	}
}
