package fr.alchemy.utilities.functions;

/**
 * A functional interface allowing the user to perform a specific action 
 * with the invoked object.
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
