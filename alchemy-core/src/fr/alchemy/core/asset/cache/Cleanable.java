package fr.alchemy.core.asset.cache;

/**
 * <code>Cleanable</code> is an interface to represent an object which
 * can be cleaned-up when no longer needed.
 * <p>
 * It can also be called to reuse said object with new values.
 * 
 * @author GnosticOccultist
 */
public interface Cleanable {
	
	/**
	 * Clean the object when no longer needed or to be
	 * resetted.
	 */
	void cleanup();
}
