package fr.alchemy.utilities.collections.pool;

import java.util.function.Supplier;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.task.actions.ModifierAction;

/**
 * <code>ReusablePool</code> is an implementation of {@link ReusablePool} designed for {@link Reusable} elements.
 * 
 * @param <E> The type of reusable element to store into the pool.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @see Pool
 * @see Reusable
 * 
 * @author GnosticOccultist
 */
public interface ReusablePool<E extends Reusable> extends Pool<E> {

	/**
	 * Apply the given {@link ModifierAction} using a retrieved element from the <code>ReusablePool</code>
	 * and releasing it once finished.
	 * 
	 * @param action The action to apply with the retrieved element (not null).
	 * @return		 The result of the modifier action.
	 * 
	 * @see #applyAndRelease(ModifierAction, Supplier)
	 * @see #applyWithAndRelease(ModifierAction, Reusable)
	 */
	default <R> R applyAndRelease(ModifierAction<E, R> action) {
		return applyWithAndRelease(action, null);
	}
	
	/**
	 * Apply the given {@link ModifierAction} using a previously retrieved element or a new one from the 
	 * <code>ReusablePool</code> and releasing it once finished.
	 * 
	 * @param action 	The action to apply with the retrieved element (not null).
	 * @param retrieved The previously retrieved element to use, or null to retrieve one from the pool.
	 * @return		 	The result of the modifier action.
	 * 
	 * @see #applyAndRelease(ModifierAction)
	 * @see #applyAndRelease(ModifierAction, Supplier)
	 */
	default <R> R applyWithAndRelease(ModifierAction<E, R> action, E retrieved) {
		Validator.nonNull(action, "The modifier action can't be null!");
		
		E store = retrieved == null ? retrieve() : retrieved;
		R result = action.apply(store);
		
		store.release();
		return result;
	}
	
	/**
	 * Apply the given {@link ModifierAction} using a retrieved element from the <code>ReusablePool</code>, or
	 * using the given {@link Supplier} to instantiate a new one, and releasing it once finished.
	 * 
	 * @param action  The action to apply with the retrieved element (not null).
	 * @param factory The factory to instantiate a new element.
	 * @return		  The result of the modifier action.
	 * 
	 * @see #applyAndRelease(ModifierAction)
	 * @see #applyWithAndRelease(ModifierAction, Reusable)
	 */
	default <R> R applyAndRelease(ModifierAction<E, R> action, Supplier<E> factory) {
		E element = retrieve(factory);
		R result = action.apply(element);
		
		element.release();
		return result;
	}
}
