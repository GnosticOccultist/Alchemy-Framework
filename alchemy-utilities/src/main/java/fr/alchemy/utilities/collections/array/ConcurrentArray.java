package fr.alchemy.utilities.collections.array;

import java.util.function.Consumer;
import java.util.function.Function;

import fr.alchemy.utilities.Validator;

/**
 * <code>ConcurrentArray</code> is an interface to implement an {@link Array} with thread-safe access.
 * 
 * @param <E> The type of element contained in the array.
 * 
 * @author GnosticOccultist
 */
public interface ConcurrentArray<E> extends Array<E> {

	/**
	 * Acquires the lock to perform a reading action with the <code>ConcurrentArray</code>.
	 * 
	 * @return The stamp to use for unlocking the lock.
	 */
	long readLock();
	
	/**
	 * Release the previously acquired lock for reading with the <code>ConcurrentArray</code>.
	 * The lock will only be release if the provided stamp matches the current lock's state.
	 * 
	 * @param stamp The stamp to release the lock.
	 * 
	 * @throws IllegalMonitorStateException Thrown if the stamp doesn't match the current lock's state.
	 */
	void readUnlock(long stamp);
	
	/**
	 * Acquires the lock to perform a writing action with the <code>ConcurrentArray</code>.
	 * 
	 * @return The stamp to use for unlocking the lock.
	 */
	long writeLock();
	
	/**
	 * Release the previously acquired lock for writing with the <code>ConcurrentArray</code>.
	 * The lock will only be release if the provided stamp matches the current lock's state.
	 * 
	 * @param stamp The stamp to release the lock.
	 * 
	 * @throws IllegalMonitorStateException Thrown if the stamp doesn't match the current lock's state.
	 */
	void writeUnlock(long stamp);
	
	/**
	 * Performs the given {@link Consumer} under a read-lock block for each element in the <code>ConcurrentArray</code>.
	 * 
	 * @param consumer The consumer to perform (not null)
	 * @return		   The concurrent array for chaining purposes (not null).
	 */
	default ConcurrentArray<E> forEachInReadLock(Consumer<? super E> consumer) {
		Validator.nonNull(consumer, "The consumer can't be null!");
		
		long stamp = readLock();
		try {
			forEach(consumer);
		} finally {
			readUnlock(stamp);
		}
		
		return this;
	}
	
	/**
	 * Applies the given {@link Function} under a read-lock block using the <code>ConcurrentArray</code>.
	 * 
	 * @param function The function to apply on the array (not null)
	 * @return		   The result of the function.
	 */
	default <R> R applyInReadLock(Function<ConcurrentArray<E>, R> function) {
		Validator.nonNull(function, "The function can't be null!");
		
		long stamp = readLock();
		try {
			return function.apply(this);
		} finally {
			readUnlock(stamp);
		}
	}
	
	/**
	 * Applies the given {@link Function} under a write-lock block using the <code>ConcurrentArray</code>.
	 * 
	 * @param function The function to apply on the array (not null)
	 * @return		   The result of the function.
	 */
	default <R> R applyInWriteLock(Function<ConcurrentArray<E>, R> function) {
		Validator.nonNull(function, "The function can't be null!");
		
		long stamp = readLock();
		try {
			return function.apply(this);
		} finally {
			readUnlock(stamp);
		}
	}
}
