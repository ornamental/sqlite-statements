package dev.ornamental.sqlite.statement;

import java.util.stream.IntStream;

/**
 * This is a mutable implementation of the {@link CharSequence} interface. It may be
 * used to replace the symbolic parts of statements while keeping the statements
 * structurally immutable.<br/>
 * All the {@link CharSequence} methods are redirected to the currently wrapped backing instance.
 */
public final class MutableCharSequence implements CharSequence {

	private CharSequence charSequence;

	/**
	 * Creates a new wrapper containing no backing {@link CharSequence}.
	 */
	public MutableCharSequence() {
		this.charSequence = null;
	}

	/**
	 * Creates a new wrapper containing a backing {@link CharSequence}.
	 * @param initial the initial {@link CharSequence} to wrap
	 */
	public MutableCharSequence(CharSequence initial) {
		this.charSequence = initial;
	}

	/**
	 * Replaces the current backing {@link CharSequence} (if any) with a new one.
	 * @param value the new {@link CharSequence} instance to wrap
	 */
	public void set(CharSequence value) {
		this.charSequence = value;
	}

	@Override
	public int length() {
		checkContent();
		return charSequence.length();
	}

	@Override
	public char charAt(int index) {
		checkContent();
		return charSequence.charAt(index);
	}

	@Override
	public CharSequence subSequence(int beginIndex, int endIndex) {
		checkContent();
		return charSequence.subSequence(beginIndex, endIndex);
	}

	@Override
	public String toString() {
		checkContent();
		return charSequence.toString();
	}

	@Override
	public IntStream chars() {
		checkContent();
		return charSequence.chars();
	}

	@Override
	public IntStream codePoints() {
		checkContent();
		return charSequence.codePoints();
	}

	private void checkContent() {
		if (charSequence == null) {
			throw new IllegalStateException(
				"This method must not be invoked when the underlying String instance is not set.");
		}
	}
}
