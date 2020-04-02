package fr.alchemy.utilities.collections.pool;

import java.util.function.Supplier;

import fr.alchemy.utilities.Validator;

/**
 * <code>FastReusablePool</code> is an implementation of {@link FastPool} designed for {@link Reusable} elements.
 * 
 * @param <E> The type of reusable element to store into the pool.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public class FastReusablePool<E extends Reusable> extends FastPool<E> {
	
	/**
	 * Instantiates a new <code>FastReusablePool</code> for the provided type of {@link Reusable} element 
	 * to contain and with an initial size of 10 elements.
	 * 
	 * @param type The type of element instances to contain (not null).
	 */
	public FastReusablePool(Class<? super E> type) {
		super(type);
	}
	
	/**
	 * Instantiates a new <code>FastReusablePool</code> for the provided type of {@link Reusable} element 
	 * to contain and of the given initial size.
	 * 
	 * @param type The type of element instances to contain (not null).
	 * @param size The size of the pool (&gt;0).
	 */
	public FastReusablePool(Class<? super E> type, int size) {
		super(type, size);
	}
	
	/**
	 * Inject the given {@link Reusable} element instance at the end of the <code>FastPool</code>.
	 * 
	 * @param element The element to inject into the pool (not null).
	 */
	@Override
	public void inject(E element) {
		Validator.nonNull(element, "The element to inject can't be null!");
		element.free();
		super.inject(element);
	}
	
	/**
	 * Retrieves the {@link Reusable} element instance at the end of the <code>FastPool</code>.
	 * <p>
	 * The element is prepared for reusability by calling {@link Reusable#reuse()} before being returned.
	 * 
	 * @return The element at the end at the end of the pool, or null if none.
	 * 
	 * @see #remove(Object)
	 */
	@Override
	public E retrieve() {
		E element = super.retrieve();
		
		if(element != null) {
			element.reuse();
		}
		
		return element;
	}

	/**
	 * Retrieves the {@link Reusable} element instance at the end of the <code>FastPool</code> or instantiate
	 * a new one using the given factory if none.
	 * <p>
	 * The element is prepared for reusability by calling {@link Reusable#reuse()} before being returned.
	 * 
	 * @param factory The factory to instantiate a new element.
	 * @return 		  The element at the end of the pool, or a new instance if none.
	 * 
	 * @see #remove(Object)
	 */
	public E retrieve(Supplier<E> factory) {
		Validator.nonNull(factory, "The factory can't be null!");
        E take = retrieve();
        return take != null ? take : factory.get();
	}
}
