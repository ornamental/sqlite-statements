package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression having the form<br>
 * <code><strong>EXISTS (<em>selectStatement</em>)</strong></code><br>
 * where <code><em>selectStatement</em></code> is a <code>SELECT</code> statement.<br>
 * This is a complete SQL expression.
 */
public final class ExistsExpression implements SqlExpression {

	private final SelectStatement select;

	private final boolean not;

	ExistsExpression(boolean not, SelectStatement select) {
		this.not = not;
		this.select = select;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		if (not) {
			receptacle.append("NOT ");
		}
		receptacle.append("EXISTS (");
		select.build(receptacle);
		receptacle.append(")");
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE;
	}

	@Override
	public SqlExpression copy() {
		SelectStatement selectCopy = select.copy();

		return selectCopy == select ? this : new ExistsExpression(not, selectCopy);
	}
}
