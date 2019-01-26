package fr.alchemy.utilities.functions;

/**
 * A functional interface allowing the user to perform a certain type of action to the first invoked object
 * with the second one and return the modified object.
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface BiModifierAction<T, U> {
	
	/**
	 * Performs a modification to the provided first element and
	 * return it.
	 * 
	 * @return The modified object.
	 */
	T modify(T t, U u);
}
