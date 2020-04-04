package fr.alchemy.utilities.task.actions;

import java.util.function.Function;

/**
 * A functional interface allowing the user to perform a certain type of action to the invoked element
 * and return the modified element.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface ModifierAction<T> extends Function<T, T> {
	
	@Override
	default T apply(T element) {
		return modify(element);
	}
	
	/**
	 * Performs a modification to the provided element and return it.
	 * 
	 * @param element The element to modify
	 * @return 		  The modified element.
	 */
	T modify(T element);
}
