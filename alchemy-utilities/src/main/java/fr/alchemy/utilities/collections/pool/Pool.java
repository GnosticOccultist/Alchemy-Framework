package fr.alchemy.utilities.collections.pool;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.Array;

/**
 * <code>Pool</code> is wrapper around an {@link Array} to easily retrieve and re-inject element instances. 
 * The pool can be grown dynamically (lazily) by using the {@link #retrieve(Supplier)} method.
 * 
 * @param <E> The type of element to store into the pool.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @see ReusablePool
 * @see AbstractPool
 * @see FastPool
 * @see StampedLockPool
 * 
 * @author GnosticOccultist
 */
public interface Pool<E> {

	/**
	 * Inject the given element instance at the end of the <code>Pool</code>.
	 * 
	 * @param element The element to inject into the pool (not null).
	 */
	void inject(E element);

	/**
	 * Remove the given element instance from the <code>Pool</code>.
	 * 
	 * @param element The element to remove from the pool (not null).
	 *
	 * @see #retrieve()
	 */
	void remove(E element);

	/**
	 * Retrieves safely the element instance at the end of the <code>Pool</code> as
	 * an {@link Optional} value.
	 * 
	 * @return An optional value of the element at the end at the end of the pool,
	 *         an empty one if no element is present.
	 * 
	 * @see #remove(Object)
	 */
	default Optional<E> retrieveSafe() {
		return Optional.ofNullable(retrieve());
	}

	/**
	 * Retrieves the element instance at the end of the <code>Pool</code>.
	 * 
	 * @return The element at the end of the pool, or null if none.
	 * 
	 * @see #remove(Object)
	 */
	E retrieve();

	/**
	 * Retrieves the element instance at the end of the <code>Pool</code> or
	 * instantiate a new one using the given factory if none.
	 * 
	 * @param factory The factory to instantiate a new element (not null).
	 * @return The element at the end of the pool, or a new instance if none.
	 * 
	 * @see #remove(Object)
	 */
	default E retrieve(Supplier<E> factory) {
		Validator.nonNull(factory, "The factory can't be null!");
		E take = retrieve();
		return take != null ? take : factory.get();
	}

	/**
	 * Performs the given {@link Consumer} for each element instances currently in
	 * the <code>Pool</code>.
	 * 
	 * @param action The action to perform on each pooled element (not null).
	 */
	void forEach(Consumer<E> action);

	/**
	 * Return whether the <code>Pool</code> is empty.
	 * 
	 * @return Whether the pool is empty.
	 */
	boolean isEmpty();

	/**
	 * Return the size of the <code>Pool</code>.
	 * 
	 * @return The count of pooled elements (&ge;0).
	 */
	int size();
}
