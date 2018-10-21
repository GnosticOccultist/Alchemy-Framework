package fr.alchemy.editor.api.editor.layout;

import java.util.Optional;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.core.event.AlchemyEventManager;
import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.core.event.ChangedCurrentWorkspaceEvent;
import fr.alchemy.editor.core.ui.component.WorkspaceComponent;
import fr.alchemy.utilities.Validator;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Region;

public abstract class EditorLayout<T extends Region> {
	
	/**
	 * The list of optional components present in the pane.
	 */
	protected final Array<EditorComponent> components;
	/**
	 * The content layout.
	 */
	protected final T content;
	
	protected EditorLayout() {
		this.content = createLayout();
		this.components = Array.ofType(EditorComponent.class);
		
		AlchemyEventManager.events().registerEventHandler(
				ChangedCurrentWorkspaceEvent.CURRENT_WORKSPACE, event -> handleSwitchWorkspace());
	}
	
	/**
	 * Handles switching the workspace using {@link ChangedCurrentWorkspaceEvent}.
	 */
	protected void handleSwitchWorkspace() {
		Optional<WorkspaceComponent> component = components.stream()
				.filter(WorkspaceComponent.class::isInstance).map(WorkspaceComponent.class::cast).findFirst();
		
		if(component.isPresent()) {
			component.get().switchWorkspace();
			return;
		}
		
		WorkspaceComponent workspace = new WorkspaceComponent();
		attach(workspace);
		workspace.switchWorkspace();
	}

	/**
	 * Notify every {@link EditorComponent} that the <code>AlchemyEditorScene</code>
	 * has finish its building.
	 */
	public void notifyFinishBuild() {
		components.forEach(EditorComponent::finish);
	}
	
	/**
	 * Attaches the provided {@link EditorComponent} to the <code>EditorLayout</code>.
	 * 
	 * @param component The component to add to the layout.
	 * @return			The updated layout.
	 */
	protected abstract EditorLayout<T> attach(EditorComponent component);
	
	/**
	 * Return the action to create the layout for the editor.
	 * 
	 * @return The editor layout.
	 */
	protected abstract T createLayout();
	
	/**
	 * Return the width property of the <code>EditorLayout</code>.
	 * 
	 * @return The width property.
	 */
	protected ReadOnlyDoubleProperty widthProperty() {
		return getContent().widthProperty();
	}
	
	/**
	 * Return the height property of the <code>EditorLayout</code>.
	 * 
	 * @return The height property.
	 */
	protected ReadOnlyDoubleProperty heightProperty() {
		return getContent().heightProperty();
	}
	
	/**
	 * Return the content layout for the editor.
	 * 
	 * @return The content layout.
	 */
	public T getContent() {
		return Validator.nonNull(content);
	}
}
