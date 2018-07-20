package fr.alchemy.core.util;

/**
 * A functional interface allowing the user to perform a certain type of action to the invoked object
 * and return the modified object.
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
	T modify(T object);
}
