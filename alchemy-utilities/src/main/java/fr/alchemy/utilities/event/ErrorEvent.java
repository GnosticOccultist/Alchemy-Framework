package fr.alchemy.utilities.event;

import fr.alchemy.utilities.Validator;

/**
 * <code>ErrorEvent</code> represents a general error event which can be used to signal application
 * events across the {@link EventBus}. Optional information about the "original event" is included for
 * event dispatch errors.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public class ErrorEvent {
	
	/**
	 * Event type signaling an error during event dispatch. Additional information
	 * about the original type and event will be filled in on the passed event.
	 */
	public static final EventType<ErrorEvent> DISPATCH_ERROR = EventType.create("DispatchError", ErrorEvent.class);
	/**
	 * A general fatal application error. It indicates that the application
	 * should shut down immediately.
	 */
	public static final EventType<ErrorEvent> FATAL_ERROR = EventType.create("FatalError", ErrorEvent.class);
	
	/**
	 * The error signaled by the event.
	 */
	private final Throwable error;
	/**
	 * The original type of event which caused the error.
	 */
	private final EventType originalType;
	/**
	 * The original event which caused the error.
	 */
	private final Object originalEvent;
	
	/**
	 * Instantiates a new <code>ErrorEvent</code> with the given {@link Throwable} as the
	 * error.
	 * 
	 * @param error The error signaled by the event (not null).
	 */
	public ErrorEvent(Throwable error) {
		this(error, null, null);
	}
	
	/**
	 * Instantiates a new <code>ErrorEvent</code> with the given {@link Throwable} as the
	 * error and the provided cause event and its typ.
	 * 
	 * @param error			The error signaled by the event (not null).
	 * @param originalType	The type of the event which caused the error.
	 * @param originalEvent The event which caused the error.
	 */
	public ErrorEvent(Throwable error, EventType originalType, Object originalEvent) {
		Validator.nonNull(error, "The throwable error can't be null!");
		
        this.error = error;
        this.originalType = originalType;
        this.originalEvent = originalEvent;
	}
	
	/**
	 * Return the error signaled by the <code>ErrorEvent</code>.
	 * 
	 * @return The error signaled by the event (not null).
	 */
	public Throwable getError() {
		return error;
	}
	
	/**
	 * Return the original {@link EventType} which caused the <code>ErrorEvent</code>.
	 * 
	 * @return The type of the event which caused the error.
	 */
	public EventType getOriginalType() {
		return originalType;
	}
	
	/**
	 * Return the original event which caused the <code>ErrorEvent</code>.
	 * 
	 * @return The event which caused the error.
	 */
	public Object getOriginalEvent() {
		return originalEvent;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": error=" + error + ", originalType=" + 
				originalType + ", originalEvent=" + originalEvent;
	}
}
