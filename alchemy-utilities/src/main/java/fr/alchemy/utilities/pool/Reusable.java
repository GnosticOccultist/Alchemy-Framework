package fr.alchemy.utilities.pool;

public interface Reusable extends AutoCloseable {
	
	/**
	 * Cleanup this object before storing to a pool.
	 */
	default void free() {}
	
	/**
	 * Prepares to reuse this object before taking from a pool.
	 */
	default void reuse() {}
	
	/**
	 * Stores this object to a pool.
	 */
	default void release() {}
	
	@Override
	default void close() {
		release();
	}
}
