package fr.alchemy.utilities;

import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <code>ReusablePoolObject</code> is a container of a certain amount of the same object,
 * which can be reused to limit instantiations.
 * 
 * @param <T> The type of object.
 * 
 * @author GnosticOccultist
 */
public final class ReusablePoolObject<T> {
	
	/**
	 * The array with the contained objects.
	 */
	private final T[] buffer;
	/**
	 * The handler for getting object from the pool.
	 */
	private final Consumer<T> handler;
	/**
	 * The index of the next object.
	 */
	private int next;
	
	/**
	 * Instantiates a new <code>ReusablePoolObject</code> with the provided
	 * type, size and the creator of the object.
	 * 
	 * @param type 	   The type of object contained in the pool.
	 * @param size	   The size of the pool.
	 * @param factory  The factory to instantiates new object.
	 */
	public ReusablePoolObject(Class<?> type, int size, Supplier<T> factory) {
		this(type, size, factory, null);
	}
	
	/**
	 * Instantiates a new <code>ReusablePoolObject</code> with the provided
	 * type, size, the factory and the handler.
	 * <p>
	 * The handler is applied when the pool return a new object ({@link #acquireNext()}), 
	 * so it can reset the object state.
	 * 
	 * @param type 	   The type of object contained in the pool.
	 * @param size	   The size of the pool.
	 * @param factory  The factory to instantiates new object.
	 * @param handler  The function to apply to a returned object from the pool.
	 */
	@SuppressWarnings("unchecked")
	public ReusablePoolObject(Class<?> type, int size, Supplier<T> factory, Consumer<T> handler) {
		if(size < 2) {
			throw new RuntimeException("The size of the pool is less than 2!");
		}
		
		this.buffer = (T[]) Array.newInstance(type, size);
				
		for(int i = 0; i < buffer.length; i++) {
			buffer[i] = factory.get();
		}
		
		this.handler = handler;
	}
	
	/**
	 * Return the next free object to be used.
	 * 
	 * @return The next free object.
	 */
	public T acquireNext() {
		if(next >= buffer.length) {
			next = 0;
		}
		
		T result = buffer[next++];
		if(handler != null) {
			handler.accept(result);
		}
		return result;
	}
}
