package fr.alchemy.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <code>LocalVars</code> is the container with multiple <code>ReusablePoolObject</code>
 * registered from which the user can retrieve an object specific to a thread.
 * 
 * @author GnosticOccultist
 */
public final class LocalVars {
	
	/**
	 * The local thread for the defining the local <code>LocalVars</code>.
	 */
	private static final ThreadLocal<LocalVars> THREAD_LOCAL = 
			ThreadLocal.withInitial(LocalVars::new);
	
	private static final int DEFAULT_SIZE = 20;

	/**
	 * Return the thread-local vars.
	 * 
	 * @return The local vars.
	 */
	public static LocalVars get() {
		return THREAD_LOCAL.get();
	}
	
	private final Map<Class<?>, ReusablePoolObject<?>> buffers;
	
	public LocalVars() {
		this.buffers = new HashMap<>();
	}
	
	/**
	 * Register a new <code>ReusablePoolObject</code> with the specified
	 * type and factory. The size is 20 objects.
	 * 
	 * @param type	  The type of object.
	 * @param factory The factory to create the object.
	 */
	public <T> void register(Class<T> type, Supplier<T> factory) {
		if(!buffers.containsKey(type)) {
			buffers.put(type, new ReusablePoolObject<>(type, DEFAULT_SIZE, factory));
		}
	}
	
	/**
	 * Register a new <code>ReusablePoolObject</code> with the specified
	 * type, size and factory.
	 * 
	 * @param type	  The type of object.
	 * @param size    The size of the pool.
	 * @param factory The factory to create the object.
	 */
	public <T> void register(Class<T> type, int size, Supplier<T> factory) {
		if(!buffers.containsKey(type)) {
			buffers.put(type, new ReusablePoolObject<>(type, size, factory));
		}
	}
	
	/**
	 * Acquire the next object from the specified type or null if it 
	 * isn't register.
	 * <p>
	 * You can register a new type of object by calling {@link #register(Class, Supplier)}.
	 * 
	 * @param type The type of object to get.
	 * @return	   A new object from the pool.
	 */
	@SuppressWarnings("unchecked")
	public <T> T acquireNext(Class<T> type) {
		if(buffers.containsKey(type)) {
			return (T) buffers.get(type).acquireNext();
		}
		return null;
	}
}
