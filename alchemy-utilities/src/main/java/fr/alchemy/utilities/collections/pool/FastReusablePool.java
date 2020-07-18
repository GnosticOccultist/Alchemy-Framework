package fr.alchemy.utilities.collections.pool;

import java.util.function.Supplier;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.task.actions.ModifierAction;

/**
 * <code>FastReusablePool</code> is an implementation of {@link FastPool} designed for {@link Reusable} elements.
 * 
 * @param <E> The type of reusable element to store into the pool.
 * 
 * @version 0.1.1
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
	 * Inject the given {@link Reusable} element instance at the end of the <code>FastReusablePool</code>.
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
	 * Retrieves the {@link Reusable} element instance at the end of the <code>FastReusablePool</code>.
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
	 * Retrieves the {@link Reusable} element instance at the end of the <code>FastReusablePool</code> or instantiate
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
	
	/**
	 * Apply the given {@link ModifierAction} using a retrieved element from the <code>FastReusablePool</code>
	 * and releasing it once finished.
	 * 
	 * @param action The action to apply with the retrieved element (not null).
	 * @return		 The result of the modifier action.
	 * 
	 * @see #applyAndRelease(ModifierAction, Supplier)
	 * @see #applyWithAndRelease(ModifierAction, Reusable)
	 */
	public <R> R applyAndRelease(ModifierAction<E, R> action) {
		return applyWithAndRelease(action, null);
	}
	
	/**
	 * Apply the given {@link ModifierAction} using a previously retrieved element or a new one from the 
	 * <code>FastReusablePool</code> and releasing it once finished.
	 * 
	 * @param action 	The action to apply with the retrieved element (not null).
	 * @param retrieved The previously retrieved element to use, or null to retrieve one from the pool.
	 * @return		 	The result of the modifier action.
	 * 
	 * @see #applyAndRelease(ModifierAction)
	 * @see #applyAndRelease(ModifierAction, Supplier)
	 */
	public <R> R applyWithAndRelease(ModifierAction<E, R> action, E retrieved) {
		Validator.nonNull(action, "The modifier action can't be null!");
		
		E store = retrieved == null ? retrieve() : retrieved;
		R result = action.apply(store);
		
		store.release();
		return result;
	}
	
	/**
	 * Apply the given {@link ModifierAction} using a retrieved element from the <code>FastReusablePool</code>, or
	 * using the given {@link Supplier} to instantiate a new one, and releasing it once finished.
	 * 
	 * @param action  The action to apply with the retrieved element (not null).
	 * @param factory The factory to instantiate a new element.
	 * @return		  The result of the modifier action.
	 * 
	 * @see #applyAndRelease(ModifierAction)
	 * @see #applyWithAndRelease(ModifierAction, Reusable)
	 */
	public <R> R applyAndRelease(ModifierAction<E, R> action, Supplier<E> factory) {
		E element = retrieve(factory);
		R result = action.apply(element);
		
		element.release();
		return result;
	}
}
