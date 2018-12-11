package fr.alchemy.core.event;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import fr.alchemy.core.annotation.AlchemyEvent;
import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.logging.FactoryLogger;
import fr.alchemy.utilities.logging.Logger;
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
	
	/**
	 * The logger for events.
	 */
	private static final Logger logger = FactoryLogger.getLogger("alchemy.event");
	
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
	 * Registers all the {@link Method}, inside the provided class instance, marked by the 
	 * {@link AlchemyEvent} annotation as an {@link EventHandler} for the specified {@link EventType}.
	 * <p>
	 * In order to work the function marked by the annotation must have, as an argument, the class
	 * containing the concerned event's type <code>public</code> and <code>static</code> instance.
	 * <p>
	 * The handled {@link Event} should also not be {@link Event#isConsumed() consumed}, otherwise the marked
	 * method won't be invoked.
	 * 
	 * @param instance The class instance to look for handler.
	 */
	public final void handleClass(Object instance) {
		for(Method method : instance.getClass().getDeclaredMethods()) {
			
			AlchemyEvent annotation = method.getAnnotation(AlchemyEvent.class);
			if(annotation != null) {
				
				// Annotation has been found, check that the event is passed as argument in the function.
				if(method.getParameterTypes().length == 0 || method.getParameterTypes().length > 1) {
					throw new IllegalArgumentException("The method " + method.getName() + " must have an Event argument only!");
				}
				
				Class<?> eventClass = method.getParameterTypes()[0];
				// Try accessing the event type from the event class specified in the argument of the marked function.
				EventType<?> eventTypeObject = null;
				for(Field field : eventClass.getDeclaredFields()) {
					// Look for the matching event type static instance
					if(field.getName().equals(annotation.eventType()) && Modifier.isStatic(field.getModifiers())) {
						if(field.getType().equals(EventType.class)) {
							try {
								// Try to access the event type.
								eventTypeObject = (EventType<?>) field.get(null);
								registerEventHandler(eventTypeObject, (event) -> {
									if(!event.isConsumed()) {
										Instantiator.invokeMethod(method, instance, event);
									}
								});
								logger.info("Successfully found and register an handler for the event type: " + eventTypeObject.getName());
							} catch (IllegalAccessException | IllegalArgumentException ex) {
								logger.error("Exception occured while trying to access the event type...", ex);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Notify about the specified {@link Event}, if it isn't yet 
	 * {@link Event#isConsumed() consumed}.
	 * 
	 * @param event The non-consumed event to notify about.
	 */
	public final void notify(final Event event) {
		if(!event.isConsumed()) {
			eventHandlers.fireEvent(event);
		}
	}
}
