package fr.alchemy.core.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;

/**
 * <code>AlchemyEventManager</code> manages all the {@link Event events} by registering an handler for the specified 
 * {@link EventType type} and firing it when the specific event is notified.
 * 
 * @author GnosticOccultist
 */
public class AlchemyEventManager {
	
	private static final AlchemyEventManager EVENT_MANAGER = new AlchemyEventManager();
	
	/**
	 * @return The <code>AlchemyEventManager</code> instance.
	 */
	public static AlchemyEventManager events() {
		return EVENT_MANAGER;
	}
	
	/**
	 * The group managing the events.
	 */
	private final Group eventHandlers = new Group();
	
	private AlchemyEventManager() {}
	
	/**
	 * Registers a JavaFX {@link EventHandler} for the specified {@link EventType type}.
	 * 
	 * @param eventType	   The event type.
	 * @param eventHandler The event handler.
	 */
	public final <T extends Event> void registerEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler) {
		eventHandlers.addEventHandler(eventType, eventHandler);
	}
	
	/**
	 * Registers a JavaFX {@link EventHandler} for the specified {@link EventType type}.
	 * 
	 * @param eventType	   The event type.
	 * @param eventHandler The event handler.
	 */
	public final <T extends Event> void unregisterEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler) {
		eventHandlers.removeEventHandler(eventType, eventHandler);
	}
	
	/**
	 * Notify about the specified event.
	 * 
	 * @param event The event to notify about.
	 */
	public void notify(final Event event) {
		eventHandlers.fireEvent(event);
	}
}
