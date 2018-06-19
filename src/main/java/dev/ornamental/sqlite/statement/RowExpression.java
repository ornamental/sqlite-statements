package dev.ornamental.sqlite.statement;

import java.util.Iterator;

/**
 * Represents an SQL expression designating a non-empty tuple of other expressions:<br/>
 * <code><strong>(<em>expression<sub>0</sub></em>{, <em>expression<sub>i</sub></em>})</strong></code><br/>
 * This is a complete SQL expression.
 */
public abstract class RowExpression implements SqlExpression {

	// TODO [LOW] add possibility of variable-size primitive lists

	abstract static class OfObjects<T> extends RowExpression {

		protected final Iterable<? extends T> elements;

		protected OfObjects(Iterable<? extends T> elements) {
			this.elements = elements;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			Iterator<? extends T> iterator = elements.iterator();
			if (!iterator.hasNext()) {
				throw new IllegalStateException(EMPTY_ITERABLE_MESSAGE);
			}
			receptacle.append('(');
			T last = iterator.next();
			while (iterator.hasNext()) {
				append(receptacle, last);
				receptacle.append(", ");
				last = iterator.next();
			}
			append(receptacle, last);
			receptacle.append(')');
		}

		protected abstract void append(StringBuilder receptacle, T value);
	}

	abstract static class OfPrimitives extends RowExpression {

		protected final boolean isReadonly;

		protected OfPrimitives(boolean isReadonly) {
			this.isReadonly = isReadonly;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			int length = getLength();
			if (length == 0) {
				throw new IllegalStateException(EMPTY_ITERABLE_MESSAGE);
			}
			receptacle.append('(');
			for (int i = 0; i < length - 1; i++) {
				append(receptacle, i);
				receptacle.append(", ");
			}
			append(receptacle, length - 1);
			receptacle.append(')');
		}

		protected abstract int getLength();

		protected abstract void append(StringBuilder receptacle, int index);
	}

	static final class OfIntegers extends OfPrimitives {

		private final int[] values;

		OfIntegers(int[] values, boolean isReadonly) {
			super(isReadonly);
			this.values = values;
		}

		@Override
		public RowExpression copy() {
			return isReadonly ? this : new OfIntegers(values.clone(), true);
		}

		@Override
		protected int getLength() {
			return values.length;
		}

		@Override
		protected void append(StringBuilder receptacle, int index) {
			receptacle.append(values[index]);
		}
	}

	static final class OfLongs extends OfPrimitives {

		private final long[] values;

		OfLongs(long[] values, boolean isReadonly) {
			super(isReadonly);
			this.values = values;
		}

		@Override
		public RowExpression copy() {
			return isReadonly ? this : new OfLongs(values.clone(), true);
		}

		@Override
		protected int getLength() {
			return values.length;
		}

		@Override
		protected void append(StringBuilder receptacle, int index) {
			receptacle.append(values[index]);
		}
	}

	static final class OfFloats extends OfPrimitives {

		private final float[] values;

		OfFloats(float[] values, boolean isReadonly) {
			super(isReadonly);
			this.values = values;
		}

		@Override
		public RowExpression copy() {
			return isReadonly ? this : new OfFloats(values.clone(), true);
		}

		@Override
		protected int getLength() {
			return values.length;
		}

		@Override
		protected void append(StringBuilder receptacle, int index) {
			receptacle.append(values[index]);
		}
	}

	static final class OfDoubles extends OfPrimitives {

		private final double[] values;

		OfDoubles(double[] values, boolean isReadonly) {
			super(isReadonly);
			this.values = values;
		}

		@Override
		public RowExpression copy() {
			return isReadonly ? this : new OfDoubles(values.clone(), true);
		}

		@Override
		protected int getLength() {
			return values.length;
		}

		@Override
		protected void append(StringBuilder receptacle, int index) {
			receptacle.append(values[index]);
		}
	}

	static final class OfStrings extends OfObjects<CharSequence> {

		OfStrings(Iterable<? extends CharSequence> strings) {
			super(strings);
		}

		@Override
		public RowExpression copy() {
			Iterable<CharSequence> elementsCopy =
				ReadonlyIterable.of(elements, CharSequence::toString);

			return elementsCopy == elements ? this : new OfStrings(elementsCopy);
		}

		@Override
		protected void append(StringBuilder receptacle, CharSequence value) {
			Literal.StringLiteral.append(value, receptacle);
		}
	}

	static final class OfNumbers extends OfObjects<Number> {

		protected OfNumbers(Iterable<? extends Number> elements) {
			super(elements);
		}

		@Override
		public RowExpression copy() {
			// supposing that Number instances are immutable
			Iterable<Number> elementsCopy = ReadonlyIterable.ofReadonly(elements);

			return elementsCopy == elements ? this : new OfNumbers(elementsCopy);
		}

		@Override
		protected void append(StringBuilder receptacle, Number value) {
			receptacle.append(value);
		}
	}

	static final class OfBlobs extends OfObjects<byte[]> {

		protected OfBlobs(Iterable<byte[]> elements) {
			super(elements);
		}

		@Override
		public RowExpression copy() {
			Iterable<byte[]> elementsCopy = ReadonlyIterable.of(elements, byte[]::clone);

			return elementsCopy == elements ? this : new OfBlobs(elementsCopy);
		}

		@Override
		protected void append(StringBuilder receptacle, byte[] value) {
			Literal.BlobLiteral.append(value, receptacle);
		}
	}

	static final class OfExpressions extends OfObjects<SqlExpression> {

		OfExpressions(Iterable<? extends SqlExpression> expressions) {
			super(expressions);
		}

		@Override
		public RowExpression copy() {
			Iterable<SqlExpression> elementsCopy = ReadonlyIterable.of(elements, SqlExpression::copy);

			return elementsCopy == elements ? this : new OfExpressions(elementsCopy);
		}

		@Override
		protected void append(StringBuilder receptacle, SqlExpression value) {
			value.appendTo(receptacle);
		}
	}

	private static final String EMPTY_ITERABLE_MESSAGE = "The row expression must have at least one element.";

	private RowExpression() { }

	@Override
	public final int getPrecedence() {
		return Integer.MAX_VALUE;
	}

	@Override
	public abstract RowExpression copy();
}
