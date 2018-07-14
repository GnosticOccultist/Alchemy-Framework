package fr.alchemy.core.listener;

/**
 * <code>ExitListener</code> is an interface to represent a listener called
 * when the <code>AlchemyApplication</code> is exiting.
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface ExitListener {

	/**
	 * Called when the <code>AlchemyApplication</code> is closing.
	 */
	void exit();
}
