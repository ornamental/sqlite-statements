package dev.ornamental.sqlite.statement;

/**
 * This is a mutable implementation of the {@link SqlExpression} interface. It may be
 * used to replace expressions used virtually anywhere in other expressions and statements.<br>
 * All the {@link SelectStatement} methods are redirected to the currently wrapped backing instance.
 */
public final class MutableSqlExpression implements SqlExpression {

	private SqlExpression sqlExpression;

	/**
	 * Creates a new wrapper containing no backing {@link SqlExpression}.
	 */
	public MutableSqlExpression() {
		this.sqlExpression = null;
	}

	/**
	 * Creates a new wrapper containing a backing {@link SqlExpression}.
	 * @param initial the initial {@link SqlExpression} to wrap
	 */
	public MutableSqlExpression(SqlExpression initial) {
		this.sqlExpression = initial;
	}

	/**
	 * Replaces the current backing {@link SqlExpression} (if any) with a new one.
	 * @param value the new {@link SqlExpression} instance to wrap
	 */
	public void set(SqlExpression value) {
		this.sqlExpression = value;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		checkContent();
		sqlExpression.appendTo(receptacle);
	}

	@Override
	public SqlExpression copy() {
		checkContent();
		return sqlExpression.copy();
	}

	@Override
	public int getPrecedence() {
		checkContent();
		return sqlExpression.getPrecedence();
	}

	private void checkContent() {
		if (sqlExpression == null) {
			throw new IllegalStateException(
				"This method must not be invoked when the underlying SqlExpression instance is not set.");
		}
	}
}
