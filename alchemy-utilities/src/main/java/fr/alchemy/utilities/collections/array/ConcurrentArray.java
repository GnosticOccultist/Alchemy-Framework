package fr.alchemy.utilities.collections.array;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import fr.alchemy.utilities.Validator;

/**
 * <code>ConcurrentArray</code> is an interface to implement an {@link Array} with thread-safe access.
 * <p>
 * In order to perform read or write safe actions, methods requiring functional interfaces or lambdas expressions can be used, because
 * their execution occurs in a locked block.
 * For example, to remove an element in a thread-safe manner:
 * <pre>
 * var toRemove = ...
 * var result = applyInWriteLock(toRemove, ConcurrentArray::remove);
 * </pre>
 * 
 * @param <E> The type of element contained in the array.
 * 
 * @version 0.2.0
 * @since 0.2.0
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
	 * @param consumer The consumer to perform (not null).
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
	 * Performs the given {@link BiConsumer} under a read-lock block using the provided argument and 
	 * the <code>ConcurrentArray</code>.
	 * 
	 * @param arg	   The argument to use in the consumer.
	 * @param consumer The consumer to perform (not null).
	 * @return		   The concurrent array for chaining purposes (not null).
	 */
	default <F> ConcurrentArray<E> performInReadLock(F arg, BiConsumer<ConcurrentArray<E>, F> consumer) {
		Validator.nonNull(consumer, "The consumer can't be null!");
		
		long stamp = readLock();
		try {
			consumer.accept(this, arg);
		} finally {
			readUnlock(stamp);
		}
		
		return this;
	}
	
	/**
	 * Performs the given {@link BiConsumer} under a write-lock block using the provided argument and 
	 * the <code>ConcurrentArray</code>.
	 * 
	 * @param arg	   The argument to use in the consumer.
	 * @param consumer The consumer to perform (not null).
	 * @return		   The concurrent array for chaining purposes (not null).
	 */
	default <F> ConcurrentArray<E> performInWriteLock(F arg, BiConsumer<ConcurrentArray<E>, F> consumer) {
		Validator.nonNull(consumer, "The consumer can't be null!");
		
		long stamp = writeLock();
		try {
			consumer.accept(this, arg);
		} finally {
			writeUnlock(stamp);
		}
		
		return this;
	}
	
	/**
	 * Applies the given {@link Function} under a read-lock block using the <code>ConcurrentArray</code>.
	 * 
	 * @param function The function to apply on the array (not null).
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
	 * @param function The function to apply on the array (not null).
	 * @return		   The result of the function.
	 */
	default <R> R applyInWriteLock(Function<ConcurrentArray<E>, R> function) {
		Validator.nonNull(function, "The function can't be null!");
		
		long stamp = writeLock();
		try {
			return function.apply(this);
		} finally {
			writeUnlock(stamp);
		}
	}
	
	/**
	 * Applies the given {@link BiFunction} under a read-lock block using the <code>ConcurrentArray</code>.
	 * 
	 * @param arg	   The argument to use in the function.
	 * @param function The function to apply on the array (not null).
	 * @return		   The result of the bi-function.
	 */
	default <F, R> R applyInReadLock(F arg, BiFunction<ConcurrentArray<E>, F, R> function) {
		Validator.nonNull(function, "The function can't be null!");
		
		long stamp = readLock();
		try {
			return function.apply(this, arg);
		} finally {
			readUnlock(stamp);
		}
	}
	
	/**
	 * Applies the given {@link BiFunction} under a write-lock block using the <code>ConcurrentArray</code>.
	 * 
	 * @param arg	   The argument to use in the function.
	 * @param function The function to apply on the array (not null).
	 * @return		   The result of the bi-function.
	 */
	default <F, R> R applyInWriteLock(F arg, BiFunction<ConcurrentArray<E>, F, R> function) {
		Validator.nonNull(function, "The function can't be null!");
		
		long stamp = writeLock();
		try {
			return function.apply(this, arg);
		} finally {
			writeUnlock(stamp);
		}
	}
}
