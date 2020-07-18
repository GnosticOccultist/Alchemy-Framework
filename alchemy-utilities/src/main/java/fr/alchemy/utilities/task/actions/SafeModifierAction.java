package fr.alchemy.utilities.task.actions;

import java.util.function.Function;

/**
 * A functional interface allowing the user to perform a certain type of action safely to the invoked element
 * and return the modified element.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface SafeModifierAction<T, U> extends Function<T, U> {
	
	/**
	 * Performs safely a modification to the provided element and return it.
	 * 
	 * @param element The element to modify safely.
	 * @return 		  The modified element.
	 */
	U modify(T element) throws Exception;
	
	@Override
	default U apply(T element) {
		try {
			return modify(element);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
