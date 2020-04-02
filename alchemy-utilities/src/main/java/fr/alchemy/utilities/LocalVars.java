package fr.alchemy.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import fr.alchemy.utilities.collections.pool.FastReusablePool;
import fr.alchemy.utilities.collections.pool.Reusable;

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
	 * The local thread for defining the local <code>LocalVars</code>.
	 */
	private static final ThreadLocal<LocalVars> THREAD_LOCAL = 
			ThreadLocal.withInitial(LocalVars::new);

	/**
	 * Return a thread-local <code>LocalVars</code> instance.
	 * 
	 * @return The thread specific local vars instance (not null).
	 */
	public static LocalVars get() {
		return THREAD_LOCAL.get();
	}
	
	/**
	 * The table containing the pools for each variables class.
	 */
	private final Map<Class<?>, FastReusablePool<?>> buffers;
	
	/**
	 * Private constructor to inhibit instantiation of <code>LocalVars</code>.
	 * Use {@link #get()} to get a local instance of this class.
	 */
	private LocalVars() {
		this.buffers = new HashMap<>();
	}
	
	/**
	 * Register a new {@link FastReusablePool} with the specified type of variables which is supposed 
	 * to store. The size of the pool is by default to 10 variable instances.
	 * 
	 * @param type The type of variables to store (not null).
	 */
	public <T> void register(Class<T> type) {
		Validator.nonNull(type, "The type of variable can't be null!");
		if(!buffers.containsKey(type)) {
			buffers.put(type, new FastReusablePool<>(type));
		}
	}
	
	/**
	 * Acquire the next variable instance of the specified type or null if no {@link FastReusablePool}
	 * has been registered for this object.
	 * <p>
	 * You can register a new type of object by calling {@link #register(Class)}.
	 * 
	 * @see #register(Class)
	 * @see #acquireNext(Class, Supplier)
	 * 
	 * @param type The type of object to get (not null).
	 * @return	   A new variable instance from the pool, or null if no pool exist.
	 */
	@SuppressWarnings("unchecked")
	public <T> T acquireNext(Class<T> type) {
		Validator.nonNull(type, "The type of variable can't be null!");
		if(buffers.containsKey(type)) {
			FastReusablePool pool = buffers.get(type);
			return (T) pool.retrieve();
		}
		return null;
	}
	
	/**
	 * Acquire the next variable instance of the specified type or null if no {@link FastReusablePool}
	 * has been registered for this object.
	 * If no instances is stored in the pool it will use the given supplier to instantiate a new variable.
	 * <p>
	 * You can register a new type of object by calling {@link #register(Class)}.
	 * 
	 * @see #register(Class)
	 * @see #acquireNext(Class)
	 * 
	 * @param type The type of object to get (not null).
	 * @return	   A new variable instance from the pool, or null if no pool exist.
	 */
	@SuppressWarnings("unchecked")
	public <T> T acquireNext(Class<T> type, Supplier<T> factory) {
		Validator.nonNull(type, "The type of variable can't be null!");
		Validator.nonNull(factory, "The variable factory can't be null!");
		if(buffers.containsKey(type)) {
			FastReusablePool pool = buffers.get(type);
			return (T) pool.retrieve(factory);
		}
		return null;
	}
	
	/**
	 * Release the provided variable instance previously retrieved from the <code>LocalVars</code>, 
	 * by putting it back into its corresponding {@link FastReusablePool} if it exists.
	 * <p>
	 * You can register a new type of object by calling {@link #register(Class)}.
	 * 
	 * @see #register(Class)
	 * 
	 * @param localVar The variable instance to put back to its pool (not null).
	 * @return		   Whether the local var has been successfully released.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Reusable> boolean release(T localVar) {
		Validator.nonNull(localVar, "The local var to releas can't be null!");
		Class<T> type = (Class<T>) localVar.getClass();
		if(buffers.containsKey(type)) {
			FastReusablePool pool = buffers.get(type);
			pool.inject(localVar);
			return true;
		}
		return false;
	}
}
