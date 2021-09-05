package fr.alchemy.utilities.collections.pool;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.FastArray;

/**
 * <code>FastPool</code> is an implementation of {@link AbstractPool} which uses a {@link FastArray} as its
 * internal pool, the pool is therefore <b>NOT</b> thread safe.
 * 
 * @param <E> The type of element to store into the pool.
 * 
 * @version 0.2.0
 * @since 0.1.0
 * 
 * @see FastArray
 * @see FastReusablePool
 * 
 * @author GnosticOccultist
 */
public class FastPool<E> extends AbstractPool<E, FastArray<E>> {
	
	/**
	 * Instantiates a new <code>FastPool</code> for the provided type of element to contain
	 * and with an initial size of 10 elements.
	 * 
	 * @param type The type of element instances to contain (not null).
	 */
	public FastPool(Class<? super E> type) {
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
	public FastPool(Class<? super E> type, Supplier<? super E> factory) {
		this(type, factory, DEFAULT_SIZE);
	}
	
	/**
	 * Instantiates a new <code>FastPool</code> for the provided type of element to contain
	 * and of the given initial size.
	 * 
	 * @param type The type of element instances to contain (not null).
	 * @param size The size of the pool in elements (&gt;0).
	 */
	@SuppressWarnings("unchecked")
	public FastPool(Class<? super E> type, int size) {
		super(type, size);
		
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
	@SuppressWarnings("unchecked")
	public FastPool(Class<? super E> type, Supplier<? super E> factory, int size) {
		super(type, size);
		
		for(int i = 0; i < size; i++) {
			inject((E) factory.get());
		}
	}
	
	/**
	 * Creates a new {@link FastArray} to use internally by the <code>FastPool</code>.
	 * 
	 * @return A new array implementation for internal pooling (not null).
	 */
	@Override
	protected FastArray<E> createPool(Class<? super E> type, int size) {
		return new FastArray<>(type, size);
	}
	
	/**
	 * Inject the given element instance at the end of the <code>FastPool</code>.
	 * 
	 * @param element The element to inject into the pool (not null).
	 */
	@Override
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
	@Override
	public void remove(E element) {
		pool.fastRemove(element);
	}
	
	/**
	 * Retrieves the element instance at the end of the <code>FastPool</code>.
	 * 
	 * @return The element at the end of the pool, or null if none.
	 * 
	 * @see #remove(Object)
	 */
	@Override
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
	 * Performs the given {@link Consumer} for each element instances currently in the 
	 * <code>FastPool</code>.
	 * 
	 * @param action The action to perform on each element (not null).
	 */
	public void forEach(Consumer<E> action) {
		Validator.nonNull(action, "The action can't be null!");
		pool.forEach(action);
	}
}
