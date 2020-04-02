package fr.alchemy.utilities.task.actions;

/**
 * A functional interface allowing the user to perform a specific action safely with the 
 * invoked element by throwing potential {@link Exception} and returning a boolean value.
 * 
 * @version 0.1.1
 * @since 0.1.1
 * 
 * @author GnosticOccultist
 */
public interface SafeBooleanAction<T> {

	/**
	 * Perform safely an action on the given element by throwing an {@link Exception} and return a boolean value.
	 * 
	 * @param element The element to perform safely the action with (not null).
	 * @return		  A boolean value either true or false.
	 */
	boolean perform(T element) throws Exception;
}
