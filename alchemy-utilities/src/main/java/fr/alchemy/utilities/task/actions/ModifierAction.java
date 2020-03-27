package fr.alchemy.utilities.task.actions;

/**
 * A functional interface allowing the user to perform a certain type of action to the invoked element
 * and return the modified element.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface ModifierAction<T> {
	
	/**
	 * Performs a modification to the provided element and return it.
	 * 
	 * @param element The element to modify
	 * @return 		  The modified element.
	 */
	T modify(T element);
}
