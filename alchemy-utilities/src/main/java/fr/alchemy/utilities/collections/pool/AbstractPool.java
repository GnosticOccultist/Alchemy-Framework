package fr.alchemy.utilities.collections.pool;

import fr.alchemy.utilities.collections.array.Array;

/**
 * <code>AbstractPool</code> is the base abstract implementation of {@link Pool} which contains common methods to 
 * all pool implementations.
 * 
 * @param <E> The type of element to store into the pool.
 * @param <A> The type of array to use as an internal pool.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @see Pool
 * @see FastPool
 * @see StampedLockPool
 * 
 * @author GnosticOccultist
 */
public abstract class AbstractPool<E, A extends Array<E>> implements Pool<E> {

	/**
	 * The default count of elements in a FastPool.
	 */
	protected static final int DEFAULT_SIZE = 10;

	/**
	 * The internal pool of objects.
	 */
	protected final A pool;

	/**
	 * Instantiates a new <code>AbstractPool</code> for the provided type of element
	 * to contain and of the given initial size.
	 * <p>
	 * The internal pool is created automatically using the {@link #createPool(Class, int)} method.
	 * 
	 * @param type The type of element instances to contain (not null).
	 * @param size The size of the pool in elements (&gt;0).
	 */
	protected AbstractPool(Class<? super E> type, int size) {
		this.pool = createPool(type, size);
	}

	/**
	 * Creates a new {@link Array} implementation to use internally by the
	 * <code>FastPool</code>.
	 * 
	 * @return A new array implementation for internal pooling (not null).
	 */
	protected abstract A createPool(Class<? super E> type, int size);

	/**
	 * Return whether the <code>AbstractPool</code> is empty.
	 * 
	 * @return Whether the pool is empty.
	 */
	@Override
	public boolean isEmpty() {
		return pool.isEmpty();
	}

	/**
	 * Return the size of the <code>AbstractPool</code>.
	 * 
	 * @return The count of pooled elements (&ge;0).
	 */
	@Override
	public int size() {
		return pool.size();
	}

	@Override
	public String toString() {
		return pool.toString();
	}
}
