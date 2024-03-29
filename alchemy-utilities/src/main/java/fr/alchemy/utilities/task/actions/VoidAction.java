package fr.alchemy.utilities.task.actions;

import java.util.function.Consumer;

/**
 * A functional interface allowing the user to perform a specific action 
 * with the invoked element.
 * 
 * @version 0.2.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface VoidAction<T> extends Consumer<T> {
	
	/**
	 * Perform the action on the given element.
	 * 
	 * @param element The element to perform the action with (not null).
	 */
	void perform(T element);
	
	@Override
	default void accept(T t) {
		perform(t);
	}
}
