package fr.alchemy.utilities.actions;

/**
 * A functional interface allowing the user to perform a specific action 
 * with the invoked object.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface VoidAction<T> {
	
	/**
	 * Perform the action. 
	 * 
	 * @param object The object to perform the action with.
	 */
	void perform(final T object);
}
