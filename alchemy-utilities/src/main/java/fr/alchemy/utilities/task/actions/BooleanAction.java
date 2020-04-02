package fr.alchemy.utilities.task.actions;

/**
 * A functional interface allowing the user to perform a specific action 
 * with the invoked object and return a boolean value.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface BooleanAction<T> {
	
	/**
	 * Performs the action on the given element and return a boolean value.
	 * 
	 * @param element The element to perform the action with (not null).
	 * @return		  A boolean value either true or false.
	 */
	boolean perform(T element);
}
