package fr.alchemy.core.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * <code>NonConsumableEvent</code> represent an abstract implementation of a JavaFX
 * {@link Event} which can't be consumed.
 * <p>
 * {@link #isConsumed()} will always return false and {@link #consume()} will throw an
 * {@link UnsupportedOperationException} when invoked.
 * 
 * @author GnosticOccultist
 */
public abstract class NonConsumableEvent extends Event {

	private static final long serialVersionUID = 1718632670829898748L;

	/**
	 * Instantiates a new <code>NonConsumableEvent</code> with the provided
	 * {@link EventType}.
	 * 
	 * @param type The type of the non-consumable event.
	 */
	protected NonConsumableEvent(EventType<? extends Event> type) {
		super(type);
	}
	
	/**
	 * Return <code>false</code> because an <code>NonConsumableEvent</code>
	 * should never be consumed.
	 */
	@Override
	public boolean isConsumed() {
		return false;
	}
	
	/**
	 * The function is disable because an <code>NonConsumableEvent</code>
	 * should never be consumed.
	 * 
	 * @throws UnsupportedOperationException Thrown because a non-consumable event can't be consumed.
	 */
	@Override
	public void consume() {
		throw new UnsupportedOperationException("Can't consume a non-consumable event!");
	}
}
