package fr.alchemy.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.alchemy.core.event.AlchemySceneEvent;
import javafx.event.Event;

/**
 * <code>AlchemyEvent</code> is an annotation to mark a method handling
 * an {@link Event} of a specific type provided as an argument.
 * 
 * @author GnosticOccultist
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AlchemyEvent {
	
	/**
	 * Provide the event type of the {@link Event}.
	 * The event type is a static reference within the class where
	 * it's declared.
	 * <p>
	 * For example: {@link AlchemySceneEvent#ALCHEMY_SCENE}.
	 * 
	 * @return The name of the event type.
	 */
	String eventType();
}
