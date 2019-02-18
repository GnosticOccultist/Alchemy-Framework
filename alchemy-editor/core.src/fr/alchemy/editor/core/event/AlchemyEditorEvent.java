package fr.alchemy.editor.core.event;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * <code>AlchemyEditorEvent</code> is an implementation of the JavaFX {@link Event}
 * to store every events concerning the <code>Alchemy-Editor</code>.
 * 
 * @author GnosticOccultist
 */
public class AlchemyEditorEvent extends Event {

	private static final long serialVersionUID = -8501913059692434197L;

	/**
	 * The root event type for all related editor events.
	 */
	public static final EventType<AlchemyEditorEvent> EDITOR = new EventType<>(ANY, "EDITOR");
	/**
	 * The event type to notify about a changed workspace, use {@link #newChangedCurrentWorkspaceEvent(Path)}
	 * to create an event of this type. The new workspace path is stored with the key <b>"workspace"</b>.
	 */
	public static final EventType<AlchemyEditorEvent> CHANGED_CURRENT_WORKSPACE = new EventType<>(EDITOR, "CHANGED_CURRENT_WORKSPACE");
	/**
	 * The event type to notify about a requested scene's creation, use {@link #newSceneCreationEvent}
	 * to create an event of this type.
	 */
	public static final EventType<AlchemyEditorEvent> OPEN_FILE = new EventType<>(EDITOR, "OPEN_FILE");
	
	/**
	 * The table mapping the objects concerned by an event.
	 */
	private final Map<String, Object> objects = new HashMap<>();
	
	/**
	 * Internal use only. Please use the <code>static</code> constructor
	 * instead.
	 * <p>
	 * For example: {@link #newChangedCurrentWorkspaceEvent(Path)}.
	 * 
	 * @param type The type of the event.
	 */
	private AlchemyEditorEvent(EventType<? extends Event> type) {
		super(type);
	}
	
	/**
	 * Instantiates a new <code>AlchemyEditorEvent</code> of {@link #CHANGED_CURRENT_WORKSPACE}
	 * type. It stores the provided workspace path with the key <b>"workspace"</b>.
	 * 
	 * @param newWorkspace The new workspace path.
	 * @return			   A new instance of an editor event to notify about a changed current workspace.
	 */
	public static AlchemyEditorEvent newChangedCurrentWorkspaceEvent(Path newWorkspace) {
		return new AlchemyEditorEvent(CHANGED_CURRENT_WORKSPACE).setPath("workspace", newWorkspace);
	}
	
	/**
	 * Instantiates a new <code>AlchemyEditorEvent</code> of {@link #OPEN_FILE} type.
	 * 
	 * @return A new instance of an editor event to notify about a requested open file.
	 */
	public static AlchemyEditorEvent newOpenFileEvent(Path path) {
		return new AlchemyEditorEvent(OPEN_FILE).setPath("file", path);
	}
	
	/**
	 * Return the stored {@link Path} in the <code>AlchemyEditorEvent</code> 
	 * with the specified name.
	 * 
	 * @param name The name of the path to retrieve.
	 * @return	   The path with the specified name? or null if none is found.
	 */
	public Path getPath(String name) {
		return get(Path.class, name);
	}
	
	/**
	 * Stores the provided {@link Path} in the <code>AlchemyEditorEvent</code> 
	 * with the specified name.
	 * 
	 * @param name The name of the path to store.
	 * @param path The path to store with the key.
	 */
	public AlchemyEditorEvent setPath(String name, Path path) {
		return set(name, path);
	}
	
	/**
	 * Return the stored boolean value in the <code>AlchemyEditorEvent</code> 
	 * with the specified name.
	 * 
	 * @param name The name of the boolean value to retrieve.
	 * @return	   The boolean value with the specified name? or null if none is found.
	 */
	public boolean getBoolean(String name) {
		return get(boolean.class, name);
	}
	
	/**
	 * Stores the provided boolean value in the <code>AlchemyEditorEvent</code> 
	 * with the specified name.
	 * 
	 * @param name The name of the boolean value to store.
	 * @param path The boolean value to store with the key.
	 */
	public AlchemyEditorEvent setBoolean(String name, boolean path) {
		return set(name, path);
	}
	
	/**
	 * Return the stored int value in the <code>AlchemyEditorEvent</code> 
	 * with the specified name.
	 * 
	 * @param name The name of the int value to retrieve.
	 * @return	   The int value with the specified name? or null if none is found.
	 */
	public int getInteger(String name) {
		return get(int.class, name);
	}
	
	/**
	 * Stores the provided int value in the <code>AlchemyEditorEvent</code> 
	 * with the specified name.
	 * 
	 * @param name The name of the int value to store.
	 * @param path The int value to store with the key.
	 * @return 
	 */
	public AlchemyEditorEvent setInteger(String name, int path) {
		return set(name, path);
	}
	
	/**
	 * Return the stored object of the specified type and name in the 
	 * <code>AlchemyEditorEvent</code>.
	 * 
	 * @param type The type of the object to retrieve.
	 * @param name The name of the object to retrieve.
	 * @return	   The object with the specified name matching the type, or null if none is found.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, String name) {
		return (T) objects.get(name);
	}
	
	/**
	 * Store the provided object in the <code>AlchemyEditorEvent</code>, ordered
	 * by its name.
	 * 
	 * @param name   The name of the object to store.
	 * @param object The object to store.
	 */
	public AlchemyEditorEvent set(String name, Object object) {
		objects.put(name, object);
		return this;
	}
}
