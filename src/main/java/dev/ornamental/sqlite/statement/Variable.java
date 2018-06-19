package dev.ornamental.sqlite.statement;

/**
 * Interface for the classes which take mutable objects upon instance construction
 * and are able to return immutable snapshot copies of their current state.
 * @param <T> the implementing class
 */
public interface Variable<T extends Variable<T>> {

	/**
	 * Returns a deep immutable copy of the object's current state.
	 * @return a deep immutable copy of the object's current state
	 */
	T copy();
}
