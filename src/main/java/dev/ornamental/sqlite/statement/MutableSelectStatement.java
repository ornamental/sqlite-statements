package dev.ornamental.sqlite.statement;

/**
 * This is a mutable implementation of the {@link SelectStatement} interface. It may be
 * used to replace the <code>SELECT</code> statement (expression) used in other statement.<br>
 * All the {@link SelectStatement} methods are redirected to the currently wrapped backing instance.
 */
public final class MutableSelectStatement implements SelectStatement {

	private SelectStatement selectStatement;

	/**
	 * Creates a new wrapper containing no backing {@link SelectStatement}.
	 */
	public MutableSelectStatement() {
		this.selectStatement = null;
	}

	/**
	 * Creates a new wrapper containing a backing {@link SelectStatement}.
	 * @param initial the initial {@link SelectStatement} to wrap
	 */
	public MutableSelectStatement(SelectStatement initial) {
		this.selectStatement = initial;
	}

	/**
	 * Replaces the current backing {@link SelectStatement} (if any) with a new one.
	 * @param value the new {@link SelectStatement} instance to wrap
	 */
	public void set(SelectStatement value) {
		this.selectStatement = value;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		checkContent();
		selectStatement.appendTo(receptacle);
	}

	@Override
	public SelectStatement copy() {
		checkContent();
		return selectStatement.copy();
	}

	@Override
	public void build(StringBuilder receptacle) {
		checkContent();
		selectStatement.build(receptacle);
	}

	private void checkContent() {
		if (selectStatement == null) {
			throw new IllegalStateException(
				"This method must not be invoked when the underlying SelectStatement instance is not set.");
		}
	}
}
