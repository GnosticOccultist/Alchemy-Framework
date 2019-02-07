package fr.alchemy.utilities.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	 * Private constructor to inhibit instantiation of this class.
	 */
	private EventBus() {}
	
	/**
	 * Publishes the specified event to the <code>EventBus</code>, delivering it 
	 * to all listeners registered for the particular type.
	 * <p>
	 * This is the same as calling {@link EventBus#publishEvent(EventType, Object)}, 
	 * except it first get the singleton-instance of the event bus.
	 * 
	 * @param type	The type of event to be delivered.
	 * @param event The event to publish.
	 */
	public static <E> void publish(EventType<E> type, E event) {
		getInstance().publishEvent(type, event);
	}
	
	/**
	 * Publishes the specified event to the <code>EventBus</code>, delivering it 
	 * to all listeners registered for the particular type.
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
		
		EventType<? super E> superType = type.getSuperType();
		if(superType != null) {
			deliver(superType, event, getListeners(superType));
		}
		
		boolean delivered = deliver(type, event, getListeners(type));
		
		if(!delivered) {
			logger.debug("Undelivered event type:" + type + " Event:" + event);
		}
	}
	
	/**
	 * Adds an {@link EventListener} object that will be notified about events of the
	 * specified type.
	 * <p>
	 * This is the same as calling {@link #addEventListener(EventType, EventListener)},
	 * except it first get the singleton-instance of the event bus.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to register.
	 */
	public static <E> void addListener(EventType<E> type, EventListener<E> listener) {
		getInstance().addEventListener(type, listener);
	}
	
	/**
	 * Adds an {@link EventListener} object that will be notified about events of the
	 * specified type.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to register.
	 */
	public <E> void addEventListener(EventType<E> type, EventListener<E> listener) {
		getListeners(type).add(listener);
		
		EventType<? super E> superType = type.getSuperType();
		if(superType != null) {
			getListeners(superType).add(listener);
		}
	}
	
	/**
	 * Removes the {@link EventListener} object that will be notified about events of the
	 * specified type.
	 * <p>
	 * This is the same as calling {@link #removeEventListener(EventType, EventListener)},
	 * except it first get the singleton-instance of the event bus.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to unregister.
	 */
	public static <E> void removeListener(EventType<E> type, EventListener<E> listener) {
		getInstance().removeEventListener(type, listener);
	}
	
	/**
	 * Removes the {@link EventListener} object that will be notified about events of the
	 * specified type.
	 * 
	 * @param type	   The type of event.
	 * @param listener The event listener to unregister.
	 */
	public <E> void removeEventListener(EventType<E> type, EventListener<E> listener) {
		getListeners(type).remove(listener);
		
		EventType<? super E> superType = type.getSuperType();
		if(superType != null) {
			getListeners(superType).remove(listener);
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
            }
        }
        return delivered;
	}
	
	protected Listeners getListeners(EventType type) {
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
