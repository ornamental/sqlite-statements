package dev.ornamental.sqlite.statement;

/**
 * This is a mutable implementation of the {@link ResultElement} interface. It may be
 * used to replace the column list part of <code>SELECT</code> statement.<br>
 * All the {@link ResultElement} methods are redirected to the currently wrapped backing instance.
 */
public final class MutableResultElement implements ResultElement {

	private ResultElement resultElement;

	/**
	 * Creates a new wrapper containing no backing {@link ResultElement}.
	 */
	public MutableResultElement() {
		this.resultElement = null;
	}

	/**
	 * Creates a new wrapper containing a backing {@link ResultElement}.
	 * @param initial the initial {@link ResultElement} to wrap
	 */
	public MutableResultElement(ResultElement initial) {
		this.resultElement = initial;
	}

	/**
	 * Replaces the current backing {@link ResultElement} (if any) with a new one.
	 * @param value the new {@link ResultElement} instance to wrap
	 */
	public void set(ResultElement value) {
		this.resultElement = value;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		checkContent();
		resultElement.appendTo(receptacle);
	}

	@Override
	public ResultElement copy() {
		checkContent();
		return resultElement.copy();
	}

	private void checkContent() {
		if (resultElement == null) {
			throw new IllegalStateException(
				"This method must not be invoked when the underlying ResultElement instance is not set.");
		}
	}
}
