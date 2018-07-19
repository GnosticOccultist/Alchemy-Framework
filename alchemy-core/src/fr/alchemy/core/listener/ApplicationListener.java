package fr.alchemy.core.listener;

/**
 * <code>ApplicationListener</code> is an interface to represent a listener called
 * at different state of the <code>AlchemyApplication</code>. 
 * For example when the application is closing, {@link #exit()} is called.
 * 
 * @author GnosticOccultist
 */
public interface ApplicationListener {

	/**
	 * Called when the <code>AlchemyApplication</code> window is shown.
	 * <p>
	 * This doesn't take into account the first time the window is showing
	 * after the initialization.
	 */
	default void show() {}
	
	/**
	 * Called when the <code>AlchemyApplication</code> window is hidden.
	 */
	default void hide() {}
	
	/**
	 * Called when the <code>AlchemyApplication</code> is paused.
	 */
	default void resume() {}
	
	/**
	 * Called when the <code>AlchemyApplication</code> is paused.
	 */
	default void pause() {}
	
	/**
	 * Called when the <code>AlchemyApplication</code> is closing.
	 */
	default void exit() {}
}
