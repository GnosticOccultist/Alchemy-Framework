package fr.alchemy.utilities.pool;

import java.util.Optional;

import fr.alchemy.utilities.collections.array.Array;

/**
 * <code>FastPool</code> is wrapper around an {@link Array} to easily retrieve and re-inject element instances.
 * 
 * @param <E> The type of element to store into the pool.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @see FastReusablePool
 * 
 * @author GnosticOccultist
 */
public class FastPool<E> {
	
	/**
	 * The internal pool of objects.
	 */
	private final Array<E> pool;
	
	/**
	 * Instantiates a new <code>FastPool</code> for the provided type of element to contain
	 * and with an initial size of 10 elements.
	 * 
	 * @param type The type of element instances to contain (not null).
	 */
	public FastPool(Class<? super E> type) {
		this.pool = Array.ofType(type);
	}
	
	/**
	 * Instantiates a new <code>FastPool</code> for the provided type of element to contain
	 * and of the given initial size.
	 * 
	 * @param type The type of element instances to contain (not null).
	 * @param size The size of the pool (&gl;0).
	 */
	public FastPool(Class<? super E> type, int size) {
		this.pool = Array.ofType(type, size);
	}
	
	/**
	 * Inject the given element instance at the end of the <code>FastPool</code>.
	 * 
	 * @param element The element to inject into the pool (not null).
	 */
	public void inject(E element) {
		pool.add(element);
	}
	
	/**
	 * Remove the given element instance from the <code>FastPool</code>.
	 * 
	 * @param element The element to remove from the pool (not null).
	 *
	 * @see #retrieve()
	 */
	public void remove(E element) {
		pool.fastRemove(element);
	}
	
	/**
     * Return whether the <code>FastPool</code> is empty.
     * 
     * @return Whether the pool is empty.
     */
	public boolean isEmpty() {
		return pool.isEmpty();
    }
	
	/**
	 * Retrieves the element instance at the end of the <code>FastPool</code>.
	 * 
	 * @return The element at the end of the pool, or null if none.
	 * 
	 * @see #remove(Object)
	 */
	public E retrieve() {
		E object = pool.pop();
		
		if(object == null) {
			return null;
		}
		
		return object;
	}
	
	/**
	 * Retrieves safely the element instance at the end of the <code>FastPool</code> as
	 * an {@link Optional} value.
	 * 
	 * @return An optional value of the element at the end at the end of the pool, 
	 * 		   an empty one if no element is present.
	 * 
	 * @see #remove(Object)
	 */
	public Optional<E> retrieveSafe() {
		return Optional.ofNullable(retrieve());
	}
	
	@Override
	public String toString() {
		return pool.toString();
	}
}
