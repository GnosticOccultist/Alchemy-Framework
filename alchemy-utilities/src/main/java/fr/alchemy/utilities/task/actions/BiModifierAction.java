package fr.alchemy.utilities.task.actions;

/**
 * A functional interface allowing the user to perform a certain type of action to the first invoked object
 * with the second one and return the modified object.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface BiModifierAction<T, U> {
	
	/**
	 * Performs a modification to the provided first element and return it.
	 * 
	 * @param t The first element to modify with the second.
	 * @param u The second element to modify the first.
	 * @return  The modified element.
	 */
	T modify(T t, U u);
}
