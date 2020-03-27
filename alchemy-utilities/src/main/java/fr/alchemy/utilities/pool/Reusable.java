package fr.alchemy.utilities.pool;

/**
 * <code>Reusable</code> is an extension interface of {@link AutoCloseable} which is meant to implement a reusable object.
 * Such object is capable of "resetting" its state to its original one to be safely reused by its invoker. 
 * <p>
 * The main usage of this interface reside in a pool where it can be retrieved and re-injected when needed for some temporary
 * computations.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface Reusable extends AutoCloseable {
	
	/**
	 * Cleanup the <code>Reusable</code> before for example injecting it back to a pool.
	 */
	default void free() {}
	
	/**
	 * Prepares the <code>Reusable</code> to be reused when for example retrieved from a pool.
	 */
	default void reuse() {}
	
	/**
	 * Release the <code>Reusable</code> by injecting it back to its original pool.
	 * 
	 * @see #close()
	 */
	default void release() {}
	
	/**
	 * Close and release the <code>Reusable</code> by injecting it back to its original pool.
	 * 
	 * @see #release()
	 */
	@Override
	default void close() {
		release();
	}
}
