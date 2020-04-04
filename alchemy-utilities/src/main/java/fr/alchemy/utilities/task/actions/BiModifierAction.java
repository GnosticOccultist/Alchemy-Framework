package fr.alchemy.utilities.task.actions;

import java.util.function.BiFunction;

/**
 * A functional interface allowing the user to perform a certain type of action to the first invoked object
 * with the second one and return the modified object.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface BiModifierAction<T, U> extends BiFunction<T, U, T> {
	
	@Override
	default T apply(T t, U u) {
		return modify(t, u);
	}
	
	/**
	 * Performs a modification to the provided first element and return it.
	 * 
	 * @param t The first element to modify with the second.
	 * @param u The second element to modify the first.
	 * @return  The modified element.
	 */
	T modify(T t, U u);
}
