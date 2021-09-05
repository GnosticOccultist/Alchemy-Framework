package fr.alchemy.utilities.collections.pool;

import java.util.function.Consumer;

import fr.alchemy.utilities.collections.array.Array;
import fr.alchemy.utilities.collections.array.StampedLockArray;

/**
 * <code>StampedLockPool</code> is an implementation of {@link AbstractPool} which uses a {@link StampedLockArray} as its
 * internal pool for thread-safety.
 * 
 * @param <E> The type of element to store into the pool.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @see StampedLockArray
 * 
 * @author GnosticOccultist
 */
public class StampedLockPool<E> extends AbstractPool<E, StampedLockArray<E>> {

	/**
	 * Instantiates a new <code>StampedLockPool</code> for the provided type of element to contain
	 * and of the given initial size.
	 * 
	 * @param type The type of element instances to contain (not null).
	 * @param size The size of the pool in elements (&gt;0).
	 */
	public StampedLockPool(Class<? super E> type, int size) {
		super(type, size);
	}

	/**
	 * Creates a new {@link StampedLockArray} to use internally by the <code>StampedLockPool</code>.
	 * 
	 * @return A new array implementation for internal pooling (not null).
	 */
	@Override
	protected StampedLockArray<E> createPool(Class<? super E> type, int size) {
		return new StampedLockArray<>(type, size);
	}

	/**
	 * Inject the given element instance at the end of the <code>StampedLockPool</code>,
	 * in a write-lock block for thread-safety.
	 * 
	 * @param element The element to inject into the pool (not null).
	 */
	@Override
	public void inject(E element) {
		pool.applyInWriteLock(element, Array::add);
	}

	/**
	 * Remove the given element instance from the <code>StampedLockPool</code>,
	 * in a write-lock block for thread-safety.
	 * 
	 * @param element The element to remove from the pool (not null).
	 *
	 * @see #retrieve()
	 */
	@Override
	public void remove(E element) {
		pool.applyInWriteLock(element, Array::remove);
	}

	/**
	 * Retrieves the element instance at the end of the <code>StampedLockPool</code>,
	 * in a write-lock block for thread-safety.
	 * 
	 * @return The element at the end of the pool, or null if none.
	 * 
	 * @see #remove(Object)
	 */
	@Override
	public E retrieve() {
		if(isEmpty()) {
			return null;
		}
		
		E element = pool.applyInWriteLock(Array::pop);
		return element;
	}

	/**
	 * Performs the given {@link Consumer} for each element instances currently in the 
	 * <code>StampedLockPool</code> in a read-lock block for thread-safety.
	 * 
	 * @param action The action to perform on each element (not null).
	 */
	@Override
	public void forEach(Consumer<E> action) {
		pool.forEachInReadLock(action);
	}
}
