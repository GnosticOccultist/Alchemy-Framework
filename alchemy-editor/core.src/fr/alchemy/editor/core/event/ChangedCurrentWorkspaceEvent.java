package fr.alchemy.editor.core.event;

import java.nio.file.Path;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * <code>ChangedCurrentWorkspaceEvent</code> notify about a changed workspace.
 * 
 * @author GnosticOccultist
 */
public class ChangedCurrentWorkspaceEvent extends Event {
	
	private static final long serialVersionUID = 4170342183386341275L;
	
	/**
	 * The type for {@link ChangedCurrentWorkspaceEvent}.
	 */
	public static final EventType<ChangedCurrentWorkspaceEvent> CURRENT_WORKSPACE = new EventType<>(ANY, "CURRENT_WORKSPACE");
	
	/**
	 * The new workspace.
	 */
	private final Path workspace;
	
	/**
	 * Instantiates a new <code>ChangedCurrentWorkspaceEvent</code> with
	 * the specified workspace's path.
	 * 
	 * @param workspace The new workspace's path.
	 */
	public ChangedCurrentWorkspaceEvent(Path workspace) {
		super(CURRENT_WORKSPACE);
		this.workspace = workspace;
	}
	
	/**
	 * Return the new workspace's path.
	 * 
	 * @return The workspace path.
	 */
	public Path getWorkspace() {
		return workspace;
	}
}
