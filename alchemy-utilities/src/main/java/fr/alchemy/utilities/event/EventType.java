package fr.alchemy.utilities.event;

/**
 * <code>EventType</code> represents the event type for an event. This helps the
 * listeners to only catch the event of their interest.
 * 
 * @param <E> The class for the event.
 * 
 * @version 0.2.0
 * @since 0.1.0
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
	 * Protected constructor to inhibit instantiation of this class. Use
	 * {@link #create(String, Class)} instead.
	 */
	protected EventType(String name, Class<E> eventClass) {
		this.name = name;
		this.eventClass = eventClass;
	}

	/**
	 * Protected constructor to inhibit instantiation of this class. Use
	 * {@link #create(String, Class)} instead.
	 */
	protected EventType(String name, Class<E> eventClass, EventType<? super E> superType) {
		this.name = name;
		this.eventClass = eventClass;
	}

	/**
	 * Creates a new <code>EventType</code> with the given name and for the
	 * specified event class.
	 * 
	 * @param <E> The event's type.
	 * 
	 * @param name       The name of the event type.
	 * @param eventClass The class of the event.
	 * @return 			 A new event type instance.
	 */
	public static <E> EventType<E> create(String name, Class<E> eventClass) {
		return new EventType<>(name, eventClass);
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
	 * Return whether the <code>EventType</code>'s name is matching the provided name.
	 * 
	 * @param name The name to check equality (not null).
	 * @return	   Whether the two names are equal.
	 */
	public boolean equals(String name) {
		return this.name.equals(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			/*
			 * Since the event type are made to be singleton static instances, this should
			 * handle most cases.
			 */
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (obj instanceof String) {
			return name.equals(obj);
		}

		if (getClass() == obj.getClass()) {
			EventType<?> other = (EventType<?>) obj;
			return name.equals(other.name) && eventClass.equals(other.eventClass);
		}

		return super.equals(obj);
	}

	@Override
	public String toString() {
		return name;
	}
}
