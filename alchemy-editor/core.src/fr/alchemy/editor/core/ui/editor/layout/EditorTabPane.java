package fr.alchemy.editor.core.ui.editor.layout;

import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.api.editor.layout.EditorLayout;
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
public class EditorTabPane extends EditorLayout<TabPane> {
	
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
		getContent().getTabs().add(tab);
		
		return this;
	}
	
	@Override
	protected TabPane createLayout() {
		return new TabPane();
	}
}
