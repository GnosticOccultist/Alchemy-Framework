package fr.alchemy.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import fr.alchemy.utilities.pool.FastReusablePool;

/**
 * <code>LocalVars</code> is the container with multiple <code>ReusablePoolObject</code>
 * registered from which the user can retrieve an object specific to a thread.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class LocalVars {
	
	/**
	 * The local thread for the defining the local <code>LocalVars</code>.
	 */
	private static final ThreadLocal<LocalVars> THREAD_LOCAL = 
			ThreadLocal.withInitial(LocalVars::new);

	/**
	 * Return the thread-local vars.
	 * 
	 * @return The local vars.
	 */
	public static LocalVars get() {
		return THREAD_LOCAL.get();
	}
	
	private final Map<Class<?>, FastReusablePool<?>> buffers;
	
	public LocalVars() {
		this.buffers = new HashMap<>();
	}
	
	/**
	 * Register a new <code>ReusablePoolObject</code> with the specified
	 * type and factory. The size is 10 objects.
	 * 
	 * @param type The type of object to store.
	 */
	public <T> void register(Class<T> type) {
		if(!buffers.containsKey(type)) {
			buffers.put(type, new FastReusablePool<>(type));
		}
	}
	
	/**
	 * Acquire the next object from the specified type or null if it isn't register.
	 * <p>
	 * You can register a new type of object by calling {@link #register(Class)}.
	 * 
	 * @see #acquireNext(Class, Supplier)
	 * 
	 * @param type The type of object to get.
	 * @return	   A new object from the pool.
	 */
	@SuppressWarnings("unchecked")
	public <T> T acquireNext(Class<T> type) {
		if(buffers.containsKey(type)) {
			FastReusablePool pool = buffers.get(type);
			return (T) pool.take();
		}
		return null;
	}
	
	/**
	 * Acquire the next object from the specified type or null if it isn't register. If no object 
	 * is stored in the pool it will use the given supplier to instantiate a new object.
	 * <p>
	 * You can register a new type of object by calling {@link #register(Class)}.
	 * 
	 * @param type The type of object to get.
	 * @return	   A new object from the pool.
	 */
	@SuppressWarnings("unchecked")
	public <T> T acquireNext(Class<T> type, Supplier<T> factory) {
		if(buffers.containsKey(type)) {
			FastReusablePool pool = buffers.get(type);
			return (T) pool.take(factory);
		}
		return null;
	}
}
