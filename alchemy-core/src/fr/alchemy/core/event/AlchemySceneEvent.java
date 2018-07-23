package fr.alchemy.core.event;

import fr.alchemy.core.scene.entity.Entity;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * <code>AlchemySceneEvent</code> contains all the events related to the <code>AlchemyScene</code>,
 * such as adding or removing an {@link Entity}.
 * 
 * @author GnosticOccultist
 */
public class AlchemySceneEvent extends Event {

	private static final long serialVersionUID = -3087842475565369173L;
	/**
	 * The <code>AlchemyScene</code> event type.
	 */
	public static final EventType<AlchemySceneEvent> ALCHEMY_SCENE = new EventType<>(ANY, "ALCHEMY_SCENE");
	/**
	 * The entity added event type.
	 */
	public static final EventType<AlchemySceneEvent> ENTITY_ADDED = new EventType<>(ALCHEMY_SCENE, "ENTITY_ADDED");
	/**
	 * The entity removed event type.
	 */
	public static final EventType<AlchemySceneEvent> ENTITY_REMOVED = new EventType<>(ALCHEMY_SCENE, "ENTITY_REMOVED");
	/**
	 * The entity concerned by the event.
	 */
	private final Entity entity;
	
	private AlchemySceneEvent(final EventType<? extends Event> eventType, final Entity entity) {
		super(eventType);
		this.entity = entity;
	}
	
	/**
	 * @return A newly created {@link #ENTITY_ADDED} <code>AlchemySceneEvent</code> event.
	 */
    public static AlchemySceneEvent entityAdded(final Entity entity) {
        return new AlchemySceneEvent(ENTITY_ADDED, entity);
    }
    
    /**
	 * @return A newly created {@link #ENTITY_REMOVED} <code>AlchemySceneEvent</code> event.
	 */
    public static AlchemySceneEvent entityRemoved(final Entity entity) {
        return new AlchemySceneEvent(ENTITY_REMOVED, entity);
    }
    
    /**
     * @return The <code>Entity</code> concerned by the event.
     */
    public Entity getEntity() {
		return entity;
	}
}
