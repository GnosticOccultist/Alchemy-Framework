package fr.alchemy.utilities.collections.pool;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.Array;

/**
 * <code>FastPool</code> is wrapper around an {@link Array} to easily retrieve and re-inject element instances. The
 * pool can be grown dynamically by using the {@link #retrieve(Supplier)} method.
 * 
 * @param <E> The type of element to store into the pool.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @see FastReusablePool
 * 
 * @author GnosticOccultist
 */
public class FastPool<E> {
	
	/**
	 * The default count of elements in a FastPool.
	 */
	protected static final int DEFAULT_SIZE = 10;
	
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
	public FastPool(Class<E> type) {
		this(type, DEFAULT_SIZE);
	}
	
	/**
	 * Instantiates a new <code>FastPool</code> for the provided type of element to contain
	 * and with an initial size of 10 elements.
	 * <p>
	 * The given {@link Supplier} will be used to fill the pool with the requested 10 elements.
	 * 
	 * @param type    The type of element instances to contain (not null).
	 * @param factory The factory to instantiate the starting elements (not null).
	 */
	public FastPool(Class<E> type, Supplier<E> factory) {
		this(type, factory, DEFAULT_SIZE);
	}
	
	/**
	 * Instantiates a new <code>FastPool</code> for the provided type of element to contain
	 * and of the given initial size.
	 * 
	 * @param type The type of element instances to contain (not null).
	 * @param size The size of the pool in elements (&gt;0).
	 */
	public FastPool(Class<E> type, int size) {
		this.pool = Array.ofType(type, size);
		
		for(int i = 0; i < size; i++) {
			inject((E) Instantiator.fromClass(type));
		}
	}
	
	/**
	 * Instantiates a new <code>FastPool</code> for the provided type of element to contain
	 * and of the given initial size.
	 * <p>
	 * The given {@link Supplier} will be used to fill the pool with the requested count of elements.
	 * 
	 * @param type 	  The type of element instances to contain (not null).
	 * @param factory The factory to instantiate the starting elements (not null).
	 * @param size 	  The size of the pool in elements (&gt;0).
	 */
	public FastPool(Class<E> type, Supplier<E> factory, int size) {
		this.pool = Array.ofType(type, size);
		
		for(int i = 0; i < size; i++) {
			inject(factory.get());
		}
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
	 * Return the size of the <code>FastPool</code>.
	 * 
	 * @return The count of pooled elements (&ge;0).
	 */
	public int size() {
		return pool.size();
	}
	
	/**
	 * Retrieves the element instance at the end of the <code>FastPool</code>.
	 * 
	 * @return The element at the end of the pool, or null if none.
	 * 
	 * @see #remove(Object)
	 */
	public E retrieve() {
		E element = pool.pop();
		
		if(element == null) {
			return null;
		}
		
		return element;
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
	
	/**
	 * Retrieves the element instance at the end of the <code>FastPool</code> or instantiate
	 * a new one using the given factory if none.
	 * 
	 * @param factory The factory to instantiate a new element (not null).
	 * @return 		  The element at the end of the pool, or a new instance if none.
	 * 
	 * @see #remove(Object)
	 */
	public E retrieve(Supplier<E> factory) {
		Validator.nonNull(factory, "The factory can't be null!");
        E take = retrieve();
        return take != null ? take : factory.get();
	}
	
	/**
	 * Performs the given {@link Consumer} for each element instances currently in the 
	 * <code>FastPool</code>.
	 * 
	 * @param action The action to perform on each element (not null).
	 */
	public void forEach(Consumer<E> action) {
		Validator.nonNull(action, "The action can't be null!");
		pool.forEach(action);
	}
	
	@Override
	public String toString() {
		return pool.toString();
	}
}
