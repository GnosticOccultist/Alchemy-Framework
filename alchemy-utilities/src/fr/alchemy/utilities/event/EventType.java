package fr.alchemy.utilities.event;

/**
 * <code>EventType</code> represents the event type for an event. This helps the
 * listeners to only catch the event of their interest.
 * 
 * @param <E> The class for the event.
 * 
 * @author GnosticOccultist
 */
public class EventType<E> {
	/**
	 * The name of the event type.
	 */
	private String name;
	/**
	 * The class for the event.
	 */
	private Class<E> eventClass;
	/**
	 * The super type of this event.
	 */
	private EventType<? super E> superType;
	
	/**
	 * Protected constructor to inhibit instantiation of this class.
	 * Use {@link #create(String, Class)} instead.
	 */
	protected EventType(String name, Class<E> eventClass) {
		this.name = name;
		this.eventClass = eventClass;
	}
	
	/**
	 * Protected constructor to inhibit instantiation of this class.
	 * Use {@link #create(String, Class)} instead.
	 */
	protected EventType(String name, Class<E> eventClass, EventType<? super E> superType) {
		this.name = name;
		this.eventClass = eventClass;
		this.superType = superType;
	}
	
	/**
	 * Creates a new <code>EventType</code> with the given name and for the 
	 * specified event class.
	 * 
	 * @param name		 The name of the event type.
	 * @param eventClass The class of the event.
	 * @return			 A new event type instance.
	 */
	public static <E> EventType<E> create(String name, Class<E> eventClass) {
		return new EventType<>(name, eventClass);
	}
	
	/**
	 * Creates a new <code>EventType</code> with the given name and for the 
	 * specified event class.
	 * 
	 * @param name		 The name of the event type.
	 * @param eventClass The class of the event.
	 * @param superType	 The super-type of the event.
	 * @return			 A new event type instance.
	 */
	public static <E> EventType<E> create(String name, Class<E> eventClass, EventType<? super E> superType) {
		return new EventType<>(name, eventClass, superType);
	}
	
	/**
	 * Return the name of the <code>EventType</code>.
	 * 
	 * @return The name of the event type.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return the class of event for the <code>EventType</code>.
	 * 
	 * @return The class of event.
	 */
	public Class<E> getEventClass() {
		return eventClass;
	}
	
	/**
	 * Return the super-type for the <code>EventType</code>.
	 * 
	 * @return The super-type of the event, if any.
	 */
	public EventType<? super E> getSuperType() {
		return superType;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
