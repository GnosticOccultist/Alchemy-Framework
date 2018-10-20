package fr.alchemy.editor.core.ui.editor.layout;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.EditorComponent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

/**
 * <code>EditorTabPane</code> is an implementation of {@link TabPane} for
 * the editor.
 * <p>
 * The tab pane is designed so the user can easily add or removes {@link EditorComponent}
 * according to its preferences.
 * 
 * @author GnosticOccultist
 */
public class EditorTabPane extends TabPane {
	
	/**
	 * The list of optional components present in the pane.
	 */
	private final Array<EditorComponent> components;
	
	/**
	 * Instantiates a new <code>EditorTabPane</code>.
	 */
	public EditorTabPane() {
		this.components = Array.ofType(EditorComponent.class);
	}
	
	/**
	 * Attaches the provided {@link EditorComponent} to the <code>EditorTabPane</code>.
	 * 
	 * @param component The component to add to the tab pane.
	 * @return			The updated tab pane.
	 */
	public EditorTabPane attach(EditorComponent component) {
		Tab tab = new Tab(component.getName());
		tab.setClosable(true);
		
		if(component instanceof Pane) {
			Pane pane = (Pane) component;
			tab.setContent(pane);
			tab.setOnCloseRequest(e -> components.remove(component));
			pane.prefHeightProperty().bind(heightProperty());
		}
	
		components.add(component);
		getTabs().add(tab);
		
		return this;
	}
	
	/**
	 * Notify every {@link EditorComponent} that the <code>AlchemyEditorScene</code>
	 * has finish its building.
	 */
	public void notifyFinishBuild() {
		components.forEach(EditorComponent::finish);
	}
}
