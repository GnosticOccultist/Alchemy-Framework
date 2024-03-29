package fr.alchemy.editor.api.editor.layout;

import java.util.List;

import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.event.AlchemyEditorEvent;
import fr.alchemy.editor.core.ui.editor.scene.AlchemyEditorScene;
import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.Array;
import fr.alchemy.utilities.event.EventListener;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Region;

public abstract class EditorLayout<T extends Region, E extends EditorComponent> implements EventListener<AlchemyEditorEvent> {
	
	/**
	 * The name of the editor layout.
	 */
	protected String name;
	/**
	 * The list of optional components present in the pane.
	 */
	protected final Array<EditorComponent> components;
	/**
	 * The content layout.
	 */
	protected final T content;
	/**
	 * The editor main scene.
	 */
	protected final AlchemyEditorScene scene;
	
	/**
	 * Instantiates a new <code>EditorLayout</code> with the provided name and scene.
	 * 
	 * @param name  The name of the editor layout.
	 * @param scene The scene on which to attach the layout.
	 */
	protected EditorLayout(String name, AlchemyEditorScene scene) {
		Validator.nonEmpty(name, "The name can't be empty or null!");
		Validator.nonNull(scene, "The scene can't be null");
		
		this.name = name;
		this.scene = scene;
		this.content = createLayout();
		this.components = Array.ofType(EditorComponent.class);
	}
	
	/**
	 * Constructs the <code>EditorLayout</code> by constructing all
	 * the last opened {@link EditorComponent}.
	 */
	public void construct() {
		List<String> components = EditorConfig.config().getOpenedComponents(name);
		for(int i = 0; i < components.size(); i++) {
			constructComponent(components.get(i));
		}
	}
	
	/**
	 * Constructs the {@link EditorComponent} with the specified class name.
	 * 
	 * @param className The class name of the component to build.
	 */
	protected void constructComponent(String className) {
		
		E component = null;
		try {
			component = Instantiator.fromName(className);
		} catch (Exception e) {
			component = Instantiator.fromNameWith(className, scene);
		}
		
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
	protected abstract T attach(E component);
	
	/**
	 * Detaches the provided {@link EditorComponent} to the <code>EditorLayout</code>.
	 * 
	 * @param component The component to remove from the layout.	
	 * @return			The updated layout.
	 */
	protected abstract T detach(E component);
	
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
		List<String> componentClasses = EditorConfig.config().getOpenedComponents(name);
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
