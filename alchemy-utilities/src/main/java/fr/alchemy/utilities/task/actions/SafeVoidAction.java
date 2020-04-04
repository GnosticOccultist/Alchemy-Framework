package fr.alchemy.utilities.task.actions;

import java.util.function.Consumer;

/**
 * A functional interface allowing the user to perform a specific action safely 
 * with the invoked element by throwing potential {@link Exception}.
 * 
 * @version 0.1.1
 * @since 0.1.1
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface SafeVoidAction<T> extends Consumer<T> {

	/**
	 * Perform safely an action on the given element by throwing an {@link Exception}. 
	 * 
	 * @param element The element to perform safely the action with (not null).
	 */
	void perform(T element) throws Exception;
	
	@Override
	default void accept(T t) {
		try {
			perform(t);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
