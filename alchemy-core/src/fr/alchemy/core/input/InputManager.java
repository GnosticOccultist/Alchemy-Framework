package fr.alchemy.core.input;

import java.util.HashMap;
import java.util.Map;

import fr.alchemy.core.AlchemyApplication;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * <code>InputManager</code> manages all the inputs inside the <code>AlchemyApplication</code> during 
 * the one loop cycle duration (tick).
 * <p>
 * You can invoke action when certain buttons or keys are pressed with the input manager.
 * 
 * @author GnosticOccultist
 */
public final class InputManager {
	
	/**
	 * The application.
	 */
	private AlchemyApplication application;
	/**
	 * The main scene.
	 */
	private Scene mainScene;
	/**
	 * Holds the mouse informations.
	 */
	protected Mouse mouse; 
	/**
	 * Whether the input manager is enabled.
	 */
	private boolean enabled;
	
	private Map<KeyCode, Boolean> keys = new HashMap<>(); 
	private Map<KeyCode, Runnable> keyPressActions = new HashMap<>(); 
	private Map<KeyCode, Runnable> keyTypedActions = new HashMap<>(); 
	private Map<KeyCode, Runnable> keyReleasedActions = new HashMap<>(); 
	
	public InputManager(final AlchemyApplication application) {
		this.mouse = new Mouse(this);
		this.application = application;
		this.enabled = true;
	}
	
	public void initialize(final Scene mainScene) {
		this.mainScene = mainScene;
		this.mainScene.setOnKeyPressed(event -> {
			if(!isPressed(event.getCode()) && keyTypedActions.containsKey(event.getCode())) {
				keys.put(event.getCode(), true);
				keyTypedActions.get(event.getCode()).run();
			} else {
				keys.put(event.getCode(), true);
			}
		});
		
		mainScene.setOnKeyReleased(event -> {
			keys.put(event.getCode(), false);
			if(keyReleasedActions.containsKey(event.getCode())) {
				keyReleasedActions.get(event.getCode()).run();
			}
		});
		
		mainScene.setOnMousePressed(mouse::update);
		mainScene.setOnMouseReleased(mouse::update);
		mainScene.setOnMouseDragged(mouse::update);
		mainScene.setOnMouseMoved(mouse::update);
	}
	
	/**
	 * This method is automatically called inside the <code>AlchemyApplication</code>
	 * to update the input manager.
	 * 
	 * @param now The current time.
	 */
	public void update(final long now) { 
		if(enabled) {
			keyPressActions.forEach((key, action) -> { if (isPressed(key)) action.run(); }); 
		}
	} 
	
	/**
	 * Adds an action which is executed constantly <strong>WHILE</strong> the key
	 * is physically pressed.
	 * 
	 * @param key	 The key to be pressed.
	 * @param action The action to be executed.
	 */
	public void addKeyPressBinding(final KeyCode key, final Runnable action) {
		keyPressActions.put(key, action);
	}
	
	/**
	 * Removes an action bound to the given key.
	 * 
	 * @param key The key to be pressed.
	 */
	public void removeKeyPressBinding(final KeyCode key) {
		keyPressActions.remove(key);
	}
	
	/**
	 * Adds an action which is executed only <strong>ONCE</strong> per single
	 * physical key press.
	 * 
	 * @param key	 The key to be pressed.
	 * @param action The action to be executed.
	 */
	public void addKeyTypedBinding(final KeyCode key, final Runnable action) {
		keyTypedActions.put(key, action);
	}
	
	/**
	 * Removes an action bound to the given key.
	 * 
	 * @param key The key to be pressed.
	 */
	public void removeKeyTypedBinding(final KeyCode key) {
		keyTypedActions.remove(key);
	}
	
	/**
	 * Adds an action that is executed <strong>ONCE</strong>, when the key
	 * is released.
	 * 
	 * @param key	 The key to be released.
	 * @param action The action to be executed.
	 */
	public void addKeyReleasedBinding(final KeyCode key, final Runnable action) {
		keyReleasedActions.put(key, action);
	}
	
	/**
	 * Removes an action bound to the given key.
	 * 
	 * @param key The key to be released.
	 */
	public void removeKeyReleasedBinding(final KeyCode key) {
		keyReleasedActions.remove(key);
	}
	
	/**
	 * Adds an action which is executed only <strong>ONCE</strong> per single click of
	 * the provided mouse button.
	 * 
	 * @param button The mouse button to be pressed.
	 * @param action The action to be executed.
	 */
	public void addMouseClickedBinding(final MouseButton button, final Runnable action) {
		mainScene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			if(event.getButton() == button && enabled) {
				action.run();
			}
		});
	}
	
	/**
	 * @return Whether the specified key is currently pressed.
	 */
	private boolean isPressed(final KeyCode key) {
		return keys.getOrDefault(key, false);
	}
	
	/**
	 * Clears all the inputs by releasing all pressed keys or buttons for 
	 * the current frame.
	 */
	public void clear() {
		keys.keySet().forEach(key -> keys.put(key, false));
		mouse.leftPressed = false;
		mouse.rightPressed = false;
	}
	
	/**
	 * @return The current mouse state.
	 */
	public Mouse getMouse() {
		return mouse;
	}
	
	/**
	 * @return The application bound to this input manager.
	 */
	public AlchemyApplication getApplication() {
		return application;
	}
	
	/**
	 * Sets whether the key/mouse actions can run, however the input
	 * events will still continue to be registered.
	 * 
	 * @param enabled Whether to enable key/mouse action processing.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
