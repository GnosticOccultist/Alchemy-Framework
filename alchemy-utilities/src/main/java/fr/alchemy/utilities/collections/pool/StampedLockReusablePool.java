package fr.alchemy.utilities.collections.pool;

import java.util.function.Supplier;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.StampedLockArray;

/**
 * <code>StampedLockReusablePool</code> is an implementation of {@link StampedLockPool} designed for {@link Reusable} elements. 
 * The pool can be grown dynamically by using the {@link #retrieve(Supplier)} method.
 * 
 * @param <E> The type of reusable element to store into the pool.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @see StampedLockArray
 * 
 * @author GnosticOccultist
 */
public class StampedLockReusablePool<E extends Reusable> extends StampedLockPool<E> implements ReusablePool<E> {

	/**
	 * Instantiates a new <code>StampedLockReusablePool</code> for the provided type of {@link Reusable}
	 * element to contain and of the given initial size.
	 * 
	 * @param type The type of element instances to contain (not null).
	 * @param size The size of the pool (&gt;0).
	 */
	public StampedLockReusablePool(Class<? super E> type, int size) {
		super(type, size);
	}

	/**
	 * Inject the given {@link Reusable} element instance at the end of the <code>StampedLockReusablePool</code>.
	 * <p>
	 * The element is cleaned up for later reusability by calling {@link Reusable#free()} before being returned.
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
	 * Retrieves the {@link Reusable} element instance at the end of the <code>StampedLockReusablePool</code>.
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
	 * Retrieves the {@link Reusable} element instance at the end of the <code>StampedLockReusablePool</code> 
	 * or instantiate a new one using the given factory if none.
	 * <p>
	 * The element is prepared for reusability by calling {@link Reusable#reuse()} before being returned.
	 * 
	 * @param factory The factory to instantiate a new element.
	 * @return 		  The element at the end of the pool, or a new instance if none.
	 * 
	 * @see #remove(Object)
	 */
	@Override
	public E retrieve(Supplier<E> factory) {
		Validator.nonNull(factory, "The factory can't be null!");
        E take = retrieve();
        return take != null ? take : factory.get();
	}
}
