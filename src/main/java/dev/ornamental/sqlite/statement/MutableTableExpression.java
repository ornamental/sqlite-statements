package dev.ornamental.sqlite.statement;

/**
 * This is a mutable implementation of the {@link TableExpression} interface. It may be
 * used to replace the <code>FROM</code> clause.<br/>
 * All the {@link TableExpression} methods are redirected to the currently wrapped backing instance.
 */
public final class MutableTableExpression implements TableExpression {

	private TableExpression tableExpression;

	/**
	 * Creates a new wrapper containing no backing {@link TableExpression}.
	 */
	public MutableTableExpression() {
		this.tableExpression = null;
	}

	/**
	 * Creates a new wrapper containing a backing {@link TableExpression}.
	 * @param initial the initial {@link TableExpression} to wrap
	 */
	public MutableTableExpression(TableExpression initial) {
		this.tableExpression = initial;
	}

	/**
	 * Replaces the current backing {@link TableExpression} (if any) with a new one.
	 * @param value the new {@link TableExpression} instance to wrap
	 */
	public void set(TableExpression value) {
		this.tableExpression = value;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		checkContent();
		tableExpression.appendTo(receptacle);
	}

	@Override
	public TableExpression copy() {
		checkContent();
		return tableExpression.copy();
	}

	private void checkContent() {
		if (tableExpression == null) {
			throw new IllegalStateException(
				"This method must not be invoked when the underlying TableExpression instance is not set.");
		}
	}
}
