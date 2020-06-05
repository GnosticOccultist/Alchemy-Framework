package fr.alchemy.editor.core.ui.editor.layout;

import java.util.List;
import java.util.Optional;

import fr.alchemy.editor.api.control.DraggableTab;
import fr.alchemy.editor.api.control.DraggableTabPane;
import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.api.editor.EditorTool;
import fr.alchemy.editor.api.editor.layout.EditorLayout;
import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.event.AlchemyEditorEvent;
import fr.alchemy.editor.core.ui.component.WorkspaceComponent;
import fr.alchemy.editor.core.ui.editor.scene.AlchemyEditorScene;
import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.event.EventBus;
import fr.alchemy.utilities.event.EventType;
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
public class EditorTabPane extends EditorLayout<TabPane, EditorTool> {
	
	/**
	 * Instantiates a new <code>EditorTabPane</code> with the provided name and scene.
	 * 
	 * @param name  The name of the editor layout.
	 * @param scene The scene on which to attach the layout.
	 */
	public EditorTabPane(String name, AlchemyEditorScene scene) {
		super(name, scene);
		
		EventBus.addListener(AlchemyEditorEvent.CHANGED_CURRENT_WORKSPACE, this);
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
	 * Attaches the provided {@link EditorComponent} to the <code>EditorTabPane</code>.
	 * 
	 * @param component The component to add to the tab pane.
	 * @return			The updated tab pane.
	 */
	public TabPane attach(EditorTool component) {
		DraggableTab tab = new DraggableTab(component);
		tab.setClosable(true);
			
		if(component instanceof Pane) {
			Pane pane = (Pane) component;
			tab.setContent(pane);
			tab.setOnCloseRequest(e -> detach(component));
			pane.prefHeightProperty().bind(heightProperty());
//			EditorConfig.config().addOpenedComponent(name, component.getClass().getName());
		}
		
		components.add(component);
		getContent().getTabs().add(tab);
		
		return content;
	}
	
	@Override
	protected void constructComponent(String className) {
		EditorTool component = null;
		try {
			component = Instantiator.fromName(className);
		} catch (Exception e) {
			component = Instantiator.fromNameWith(className, scene);
		}
		
		((WorkspaceComponent) component).createContent();
		attach(component);
		component.finish();
	}
	
	@Override
	public void newEvent(EventType<AlchemyEditorEvent> type, AlchemyEditorEvent event) {
		if(type.equals(AlchemyEditorEvent.CHANGED_CURRENT_WORKSPACE)) {
			handleSwitchWorkspace();
		}
	}
	
	@Override
	protected TabPane detach(EditorTool component) {
		components.remove(component);
		EditorConfig.config().removeOpenedComponent(name, component.getClass().getName());
		
		return content;
	}
	
	@Override
	public void save() {
		EditorConfig config = EditorConfig.config();
		
		List<String> componentClasses = config.getOpenedComponents(name);
		componentClasses.clear();
		
		components.forEach(c -> {
			componentClasses.add(c.getClass().getName());
		});
	}

	/**
	 * Creates a {@link TabPane} layout for the <code>EditorTabPane</code>.
	 * 
	 * @return A new tab pane instance.
	 */
	@Override
	protected TabPane createLayout() {
		return new DraggableTabPane(EditorTool.class::isInstance);
	}
}
