package fr.alchemy.utilities.actions;

/**
 * A functional interface allowing the user to perform a certain type of action to the invoked object
 * and return the modified object.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface ModifierAction<T> {
	
	/**
	 * Performs a modification to the provided element and
	 * return it.
	 * 
	 * @return The modified object.
	 */
	T modify(final T object);
}
