package fr.alchemy.utilities.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.logging.FactoryLogger;
import fr.alchemy.utilities.logging.Logger;

/**
 * <code>EventBus</code> dispatches events to listeners and provides a way for listeners to register
 * themselves. This is similar in concept to Guava's EventBus with some notable differences:
 * <ul>
 * <li>Type and event objects are separate which means the same event class can be used 
 * for multiple event types.
 * <li>The event bus is singleton which provides a single central place for event 
 * dispatch and registration.
 * <li>Listeners can be added with an EventListener interface OR by using reflective 
 * methods (similar to Guava's event bus).
 * </ul>
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @see #addEventListener(EventType, EventListener)
 * @see #addListenerMethods(Object, EventType...)
 * 
 * @author GnosticOccultist
 */
public final class EventBus {
	
	/**
	 * The logger for homonculus events.
	 */
	private static final Logger logger = FactoryLogger.getLogger("alchemy.events");
	/**
	 * The single-instance of this event bus.
	 */
	private static final EventBus INSTANCE = new EventBus();
	
	/**
	 * The registered listeners list.
	 */
	private final Listeners all = new Listeners();
	/**
	 * The lock used for threading safety.
	 */
	private final Lock lock = new ReentrantLock();
	/**
	 * The table containing the listeners ranged by their type.
	 */
	private final Map<EventType, Listeners> listenerMap = new ConcurrentHashMap<>(); 
	
	/**
	 * Return the single instance of <code>EventBus</code>.
	 * 
	 * @return The singleton event bus.
	 */
	public static EventBus getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Private constructor to inhibit instantiation of <code>EventBus</code>.
	 */
	private EventBus() {}
	
	/**
	 * Publishes the specified event to the <code>EventBus</code>, delivering it 
	 * to all listeners registered for the particular {@link EventType}.
	 * <p>
	 * This is the same as calling {@link EventBus#publishEvent(EventType, Object)}, 
	 * except it first get the singleton-instance of the event bus.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type	The type of event to be delivered.
	 * @param event The event to publish.
	 */
	public static <E> void publish(EventType<E> type, E event) {
		getInstance().publishEvent(type, event);
	}
	
	/**
	 * Publishes the specified event to the <code>EventBus</code>, delivering it 
	 * to all listeners registered for the particular {@link EventType}.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type  The type of event to be delivered.
	 * @param event The event to publish.
	 */
	public <E> void publishEvent(EventType<E> type, E event) {
		logger.debug("Published event [" + type + ", " + event + "]");
		
		// Deliver to any global listeners first and we don't factor
		// them into the delivery check. The global list is usually used
		// for thing like lifecycle logging and not actual event handling.
		deliver(null, event, all);
		
		boolean delivered = deliver(type, event, getListeners(type));
		
		if(!delivered) {
			logger.debug("Undelivered event type:" + type + " Event:" + event);
		}
	}
	
	/**
	 * Adds an {@link EventListener} object that will be notified about events of the
	 * specified {@link EventType}.
	 * <p>
	 * This is the same as calling {@link #addEventListener(EventType, EventListener)},
	 * except it first get the singleton-instance of the event bus.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to register.
	 */
	public static <E> void addListener(EventType<E> type, EventListener<E> listener) {
		getInstance().addEventListener(type, listener);
	}
	
	/**
	 * Adds an {@link EventListener} object that will be notified about events of the
	 * specified {@link EventType}.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to register.
	 */
	public <E> void addEventListener(EventType<E> type, EventListener<E> listener) {
		getListeners(type).add(listener);
	}
	
	/**
	 * Removes the {@link EventListener} object that will be notified about events of the
	 * specified {@link EventType}.
	 * <p>
	 * This is the same as calling {@link #removeEventListener(EventType, EventListener)},
	 * except it first get the singleton-instance of the event bus.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to unregister.
	 */
	public static <E> void removeListener(EventType<E> type, EventListener<E> listener) {
		getInstance().removeEventListener(type, listener);
	}
	
	/**
	 * Removes the {@link EventListener} object that will be notified about events of the
	 * specified {@link EventType}.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to unregister.
	 */
	public <E> void removeEventListener(EventType<E> type, EventListener<E> listener) {
		getListeners(type).remove(listener);
	}
	
	/**
	 * Removes the {@link EventListener} object that will be notified about events of any
	 * {@link EventType}.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param listener The event listener to unregister from every types.
	 */
	public <E> void clearEventListener(EventListener<E> listener) {
		for(EventType type : listenerMap.keySet()) {
			listenerMap.get(type).remove(listener);
		}
	}
	
	/**
	 * Adds a generic listener which will have its events delivered through reflection based
	 * on the {@link EventType} names. For example, if the expected event type is {@link ErrorEvent#FATAL_ERROR},
	 * then the event type name is "FatalError" and the expected method name will be either "onFatalError" 
	 * or "fatalError" with a single argument which is the concerned event.
	 * <p>
	 * Note that it is ok if the method isn't public if the current security settings allow overriding method
	 * accessibility.
	 * 
	 * @param listener The listener to register methods from.
	 * @param types	   The events type to create listener for.
	 * 
	 * @throws IllegalArgumentException Thrown if any of the dispatch methods is missing.
	 */
	public void addListenerMethods(Object listener, EventType...types) {
		Class clazz = listener.getClass();
		for(EventType type : types) {
			try {
				Method method = findMethod(clazz, type);
				if(!method.isAccessible()) {
					method.setAccessible(true);
				}
				getListeners(type).add(new MethodDispatcher(listener, method));
			} catch (NoSuchMethodException ex) {
				throw new IllegalArgumentException("Event method not found for: " + 
						type + " on object: " + listener, ex);
			}
		}
	}
	
	/**
	 * Removes a generic listener which have been registered using {@link #addListenerMethods(Object, EventType...)}
	 * from any of the specified registered {@link EventType}.
	 * 
	 * @param listener The event listener to unregister.
	 * @param types	   The types of event.
	 */
	@SuppressWarnings("unchecked")
	public void removeListenerMethods(Object listener, EventType...types) {
		for(EventType type : types) {
			Listeners listeners = getListeners(type);
			for(EventListener l : listeners.getArray()) {
				if(!(l instanceof MethodDispatcher)) {
					continue;
				}
				MethodDispatcher md = (MethodDispatcher) l;
				if(md.delegate == listener || md.delegate.equals(listener)) {
					removeEventListener(type, md);
				}
			}
		}
	}
	
	/**
	 * 
	 * TODO: Testing.
	 */
	@SuppressWarnings("unused")
	private void addListenerAnnotatedMethods(Object listener, EventType...types) {
		for(Method method : listener.getClass().getDeclaredMethods()) {
			
			// The compiler sometimes creates synthetic bridge methods as part of the
			// type erasure process. As of JDK8, these methods now include the same 
			// annotations as the original declarations. They should be ingnored from 
			// the registering process.
			if(method.isBridge()) {
				continue;
			}
			
			if(method.isAnnotationPresent(EventSubscriber.class)) {
				Class<?>[] parametersTypes = method.getParameterTypes();
				if(parametersTypes.length == 0) {
					throw new IllegalArgumentException("Method " + method + " has @EventSubscriber "
							+ "annotation but has no arguments. Such methods must require at "
							+ "least one argument.");
				}
				
				EventSubscriber annotation = method.getAnnotation(EventSubscriber.class);
				String[] typeNames = annotation.types();
				if(typeNames.length != types.length) {
					throw new IllegalArgumentException("Method " + method + " has @EventSubscriber "
							+ "annotation to handle " + typeNames.length + " event types!");
				}
				
				for(String name : typeNames) {
					for(int i = 0; i < types.length; i++) {
						if(name == types[i].getName()) {
							getListeners(types[i]).add(new MethodDispatcher(listener, method));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Adds a global listener that will notified about all {@link EventType}. This can be used
	 * to do logging or debugging on a global level and is generally not useful for normal event delivery.
	 * 
	 * @param listener The global listener to register.
	 */
	public void listenAll(EventListener listener) {
		all.add(listener);
	}
	
	/**
	 * Removes a global listener previously registered with the {@link #listenAll(EventListener)}
	 * method.
	 * 
	 * @param listener The global listener to unregister.
	 */
	public void unlistenAll(EventListener listener) {
		all.remove(listener);
	}
	
	@SuppressWarnings("unchecked")
	protected <E> boolean deliver(EventType<E> type, E event, Listeners listeners) {
        if(listeners.isEmpty()) {
            return false;
        }
    
        boolean delivered = false;
        for(EventListener listener : listeners.getArray()) {
            try {
                listener.newEvent(type, event);
                delivered = true;
            } catch(Throwable t) {
                logger.error("Error handling event:" + event + " for type:" + type + "  in handler:" + listener, t);
                if(type != ErrorEvent.DISPATCH_ERROR) {
                	publishEvent(ErrorEvent.DISPATCH_ERROR, new ErrorEvent(t, type, event));
                }
            }
        }
        return delivered;
	}
	
	/**
	 * Return the list of {@link EventListener} for the specified {@link EventType},
	 * creating the list if necessary.
	 * 
	 * @param type The event type to get all listeners for (not null).
	 * @return	   The pre-existing list of listeners or a new filled one if not 
	 * 			   already present.
	 */
	protected Listeners getListeners(EventType type) {
		Validator.nonNull(type, "The event type can't be null!");
		
		Listeners list = listenerMap.get(type);
		if(list == null) {
			// Now we need to get the lock so we are the only one
			// writing.
			lock.lock();
			try {
				// Check again to see if the list was created while 
				// we waited to get the lock (safe double-checked locking).
				list = listenerMap.get(type);
				if(list != null) {
					return list;
				}
				list = new Listeners();
				listenerMap.put(type, list);
			} finally {
				lock.unlock();
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	protected Method findMethod(Class clazz, EventType type) throws NoSuchMethodException {
		
		// First try the 'one' + name version.
		String name = "on" + type.getName();
		try {
			return clazz.getDeclaredMethod(name, type.getEventClass());
		} catch (NoSuchMethodException ex) {
			// Handle the miss later.
		}
		
		// Else try with the direct name, lower-cased appropriately.
		name = type.getName();
		if(Character.isUpperCase(name.charAt(0)) && Character.isLowerCase(name.charAt(1))) {
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
		}
		
		try {
			return clazz.getDeclaredMethod(name, type.getEventClass());
		} catch (NoSuchMethodException ex) {
			// Handle the miss later.
		}
		
		if(clazz.getSuperclass() != null) {
			// Try within the superclass methods.
			return findMethod(clazz.getSuperclass(), type);
		}
		
		throw new NoSuchMethodException("No methods matching the syntax 'on" + type.getName() 
				+ " or " + name + " within " + clazz + " and its subclasses.");
	}
	
	private class MethodDispatcher implements EventListener {
		
		private final Object delegate;
		private final Method method;
		
		public MethodDispatcher(Object delegate, Method method) {
			Validator.nonNull(method, "The listening method can't be null!");
            this.delegate = delegate;
            this.method = method;
		}

		@Override
		public void newEvent(EventType type, Object event) {
			try {
				method.invoke(delegate, event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				throw new RuntimeException("Error while calling: " + method + " for event: " + event, ex);
			}
		}
	}
	
	private class Listeners {
		
		private final List<EventListener> list = new ArrayList<>();
		private volatile EventListener[] array = null;
		
		public Listeners() {
			resetArray();
		}
		
		public void add(EventListener listener) {
			// Go ahead and grab the central lock.
			lock.lock();
			try {
				list.add(listener);
				
				// Implement 'copy on write'.
				resetArray();
			} finally {
				lock.unlock();
			}
		}
		
		public void remove(EventListener listener) {
			// Go ahead and grab the central lock.
			lock.lock();
			try {
				list.remove(listener);
				
				// Implement 'copy on write'.
				resetArray();
			} finally {
				lock.unlock();
			}
		}
		
		protected boolean isEmpty() {
			// Relatively safe here because we know a little about how
			// ArrayList is implemented and our constant usage of a volatile
			// variable means this threads view of the data structure is likely
			// to be up to date.
			return list.isEmpty();
		}
		
		protected final void resetArray() {
			// Presumes we already have the lock.
			// Can't reuse the array or we create the exact threading problem
			// we were trying to avoid.
			array = list.toArray(new EventListener[list.size()]);
		}
		
		protected final EventListener[] getArray() {
			return array;
		}
	}
}
