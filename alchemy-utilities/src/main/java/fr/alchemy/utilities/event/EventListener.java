package fr.alchemy.utilities.event;

/**
 * <code>EventListener</code> is an interface to implement a way of catching published
 * event ordered by its {@link EventType}. Use the {@link EventBus} to register this listener,
 * so events can be dispatched to it.
 * 
 * @param <E> The class for the event.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface EventListener<E> {
	
	/**
	 * Override this method to implement the code for catching a dispatched
	 * event and its {@link EventType} by the {@link EventBus}.
	 * 
	 * @param type  The type of the event.
	 * @param event The event which was dispatched by an event bus.
	 */
	void newEvent(EventType<E> type, E event);
}
