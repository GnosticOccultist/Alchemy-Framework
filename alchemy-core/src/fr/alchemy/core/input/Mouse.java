package fr.alchemy.core.input;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * <code>Mouse</code> represents the current mouse state inside the <code>AlchemyApplication</code>.
 * Only one instance of this class should be expected.
 * 
 * @author GnosticOccultist
 */
public final class Mouse {
	
	/**
	 * The input manager managing the mouse.
	 */
	private InputManager inputManager;
	/**
	 * The mouse X and Y position within the screen coordinate system.
	 */
	public double screenX, screenY;
	/**
	 * Whether the mouse buttons are pressed.
	 */
	public boolean leftPressed, rightPressed;
	/**
	 * The last mouse event invoked.
	 */
	private MouseEvent event;
	
	public Mouse(final InputManager inputManager) {
		this.inputManager = inputManager;
	}
	
	/**
	 * Update the mouse position and buttons state.
	 * 
	 * @param event The mouse event currently invoked.
	 */
	public void update(final MouseEvent event) {
		this.event = event;
		this.screenX = event.getSceneX();
		this.screenY = event.getSceneY();
		
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
	 * @return The mouse X in the application coordinate system.
	 */
	public double getAppX() {
		return inputManager.getApplication().getScene().screenToGame(new Point2D(screenX, screenY)).getX();
	}

	/**
	 * @return The mouse Y in the application coordinate system.
	 */
	public double getAppY() {
        return inputManager.getApplication().getScene().screenToGame(new Point2D(screenX, screenY)).getY();
    }
	
	/**
	 * @return The currently invoked mouse event.
	 */
	public MouseEvent getEvent() {
		return event;
	}
}
