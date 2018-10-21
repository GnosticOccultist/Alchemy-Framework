package fr.alchemy.editor.api.editor.layout;

import java.util.List;
import java.util.Optional;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.core.event.AlchemyEventManager;
import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.event.ChangedCurrentWorkspaceEvent;
import fr.alchemy.editor.core.ui.component.WorkspaceComponent;
import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.Validator;
import javafx.application.Platform;
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
		
		// TODO: Maybe add an event for adding or removing components from the layout
		AlchemyEventManager.events().registerEventHandler(
				ChangedCurrentWorkspaceEvent.CURRENT_WORKSPACE, event -> handleSwitchWorkspace());
	}
	
	/**
	 * Constructs the <code>EditorLayout</code> by constructing all
	 * the last opened {@link EditorComponent}.
	 */
	public void construct() {
		EditorConfig.config().getOpenedComponents()
				.forEach(t -> Platform.runLater(() -> constructComponent(t)));
	}
	
	/**
	 * Handles switching the workspace using {@link ChangedCurrentWorkspaceEvent}.
	 */
	protected void handleSwitchWorkspace() {
		// Check if the workspace component is attached.
		Optional<WorkspaceComponent> component = components.stream()
				.filter(WorkspaceComponent.class::isInstance).map(WorkspaceComponent.class::cast).findFirst();
		
		// If the component is present just refresh it.
		if(component.isPresent()) {
			component.get().switchWorkspace();
			return;
		}
		
		// Else, construct the component from base.
		constructComponent(WorkspaceComponent.class.getName());
	}
	
	/**
	 * Constructs the {@link EditorComponent} with the specified class.
	 * 
	 * @param clazz The class of the component to build.
	 */
	protected <E extends EditorComponent> void constructComponent(Class<E> clazz) {
		EditorComponent component = Instantiator.fromClass(clazz);
		attach(component);
		component.finish();
	}
	
	/**
	 * Constructs the {@link EditorComponent} with the specified class name.
	 * 
	 * @param className The class name of the component to build.
	 */
	protected void constructComponent(String className) {
		EditorComponent component = Instantiator.fromName(className);
		attach(component);
		component.finish();
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
	 * Detaches the provided {@link EditorComponent} to the <code>EditorLayout</code>.
	 * 
	 * @param component The component to remove from the layout.	
	 */
	protected void detach(EditorComponent component) {
		components.remove(component);
		EditorConfig.config().removeOpenedComponent(component.getClass().getName());
	}
	
	/**
	 * Return the action to create the layout for the editor.
	 * 
	 * @return The editor layout.
	 */
	protected abstract T createLayout();
	
	/**
	 * Save the <code>EditorLayout</code> by storing the opened {@link EditorComponent},
	 * so each of them can be reload during next startup time.
	 */
	public void save() {
		List<String> componentClasses = EditorConfig.config().getOpenedComponents();
		componentClasses.clear();
		
		components.forEach(c -> componentClasses.add(c.getClass().getName()));
	}
	
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
