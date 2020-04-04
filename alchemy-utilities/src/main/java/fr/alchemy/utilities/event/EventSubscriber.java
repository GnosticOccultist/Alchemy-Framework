package fr.alchemy.utilities.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <code>EventSubscriber</code> marks a method as an {@link EventListener} which can be registered
 * by the {@link EventBus} to catch published events with interesting {@link EventType}.
 * <p>
 * A method using the annotation must have at least one parameter representing the event to be listening to,
 * but it can also provides the specific event type if desired so.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventSubscriber {
	
	String[] types() default {};
}
