package fr.alchemy.core.input;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * <code>Mouse</code> represents the current mouse state inside the <code>AlchemyApplication</code>.
 * Only one instance of this class should be expected.
 * 
 * @author GnosticOccultist
 */
public class Mouse {
	/**
	 * The mouse X and Y position on screen.
	 */
	public double x, y;
	/**
	 * Whether the mouse buttons are pressed.
	 */
	public boolean leftPressed, rightPressed;
	/**
	 * The mouse event currently invoked.
	 */
	private MouseEvent event;
	
	/**
	 * Update the mouse position and buttons state.
	 * 
	 * @param event The mouse event currently invoked.
	 */
	public void update(MouseEvent event) {
		this.event = event;
		this.x = event.getSceneX();
		this.y = event.getSceneY();
		
		if(leftPressed) {
			if(event.getButton() == MouseButton.PRIMARY && isReleased(event)) {
				leftPressed = false;
			}
		} else {
			leftPressed = event.getButton() == MouseButton.PRIMARY && isPressed(event);
		}
		
		if(rightPressed) {
			if(event.getButton() == MouseButton.SECONDARY && isReleased(event)) {
				rightPressed = false;
			}
		} else {
			rightPressed = event.getButton() == MouseButton.SECONDARY && isPressed(event);
		}
	}
	
	private boolean isPressed(final MouseEvent event) {
		return event.getEventType() == MouseEvent.MOUSE_PRESSED
				|| event.getEventType() == MouseEvent.MOUSE_DRAGGED;
	}
	
	private boolean isReleased(final MouseEvent event) {
		return event.getEventType() == MouseEvent.MOUSE_RELEASED
				|| event.getEventType() == MouseEvent.MOUSE_MOVED;
	}
	
	/**
	 * @return The currently invoked mouse event.
	 */
	public MouseEvent getEvent() {
		return event;
	}
}
