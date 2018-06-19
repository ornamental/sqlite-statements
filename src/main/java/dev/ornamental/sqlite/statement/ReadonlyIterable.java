package dev.ornamental.sqlite.statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class represents a readonly sequence of immutable elements.
 * @param <T> the type of [immutable] elements in the sequence
 */
public final class ReadonlyIterable<T> implements Iterable<T> {

	private final Iterable<T> iterable;

	private ReadonlyIterable(Iterable<T> iterable) {
		this.iterable = iterable;
	}

	/**
	 * If the passed sequence is not already a {@link ReadonlyIterable}, then copies
	 * the sequence of elements which are already immutable.
	 * @param original the original sequence of immutable elements.
	 * @param <Q> the type of [immutable] elements in the sequence
	 * @return the readonly sequence independent from the original one
	 */
	public static <Q> ReadonlyIterable<Q> ofReadonly(Iterable<? extends Q> original) {
		return of(original, Function.identity());
	}

	/**
	 * If the passed sequence is not already a {@link ReadonlyIterable}, then
	 * copies the sequence mapping each element to its immutable analogue;
	 * otherwise, returns the same sequence.
	 * @param original the original sequence
	 * @param elementToReadonly the mapping of elements to their immutable forms
	 * @param <Q> the type of elements in the sequence
	 * @return the readonly sequence of immutable elements
	 */
	public static <Q> ReadonlyIterable<Q> of(
		Iterable<? extends Q> original, Function<Q, Q> elementToReadonly) {

		if (original instanceof ReadonlyIterable) {
			@SuppressWarnings("unchecked")
			ReadonlyIterable<Q> safeCast = (ReadonlyIterable<Q>)original;
			return safeCast;
		} else {
			List<Q> list = new ArrayList<>();
			original.forEach(e -> list.add(e == null ? null : elementToReadonly.apply(e)));
			return new ReadonlyIterable<>(list);
		}
	}

	@Override
	public Iterator<T> iterator() {
		return iterable.iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		iterable.forEach(action);
	}

	@Override
	public Spliterator<T> spliterator() {
		return iterable.spliterator();
	}
}
