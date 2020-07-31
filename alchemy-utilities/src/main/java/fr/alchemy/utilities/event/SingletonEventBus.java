package fr.alchemy.utilities.event;

/**
 * <code>SingletonEventBus</code> is a singleton implementation of {@link EventBus} which provides static methods to publish 
 * an {@link EventType} or add an {@link EventListener}.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @see EventBus
 * 
 * @author GnosticOccultist
 */
public final class SingletonEventBus extends EventBus {

	/**
	 * The single-instance of this event bus.
	 */
	private static final SingletonEventBus INSTANCE = new SingletonEventBus();
	
	/**
	 * Return the single instance of <code>EventBus</code>.
	 * 
	 * @return The singleton event bus.
	 */
	public static SingletonEventBus getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Private constructor to inhibit instantiation of <code>SingletonEventBus</code>.
	 */
	private SingletonEventBus() {}
	
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
	 * <p>
	 * This is the same as calling {@link EventBus#publishEvent(EventType, Object)}, 
	 * except it first get the singleton-instance of the event bus.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param type	The type of event to be delivered.
	 * @param event The event to publish.
	 */
	public static <E> void publishAsync(EventType<E> type, E event) {
		getInstance().publishAsyncEvent(type, event);
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
	public static <E> void addListener(EventType<E> type, EventListener<? super E> listener) {
		getInstance().addEventListener(type, listener);
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
	public static <E> void removeListener(EventType<E> type, EventListener<? super E> listener) {
		getInstance().removeEventListener(type, listener);
	}
}
