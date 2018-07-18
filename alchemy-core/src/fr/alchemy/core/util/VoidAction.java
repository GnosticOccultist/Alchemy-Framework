package fr.alchemy.core.util;

/**
 * A functional interface allowing the user to perform a specific action 
 * with the invoked object.
 * 
 * @author Stickxy
 */
@FunctionalInterface
public interface VoidAction<T> {
	
	/**
	 * Perform the action. 
	 * 
	 * @param object The object to perform the action with.
	 */
	void perform(T object);
}
