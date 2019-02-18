package fr.alchemy.utilities.actions;

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
	 * Performs the action and return a boolean value.
	 * 
	 * @param object The object to perform the action with.
	 * @return		 Either true or false.
	 */
	boolean perform(final T object);
}
