package fr.alchemy.editor.core.ui.editor.layout;

import java.nio.file.Path;
import java.util.List;

import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.api.editor.FileEditorRegistry;
import fr.alchemy.editor.api.editor.FileEditor;
import fr.alchemy.editor.api.editor.layout.EditorLayout;
import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.ui.editor.scene.AlchemyEditorScene;
import fr.alchemy.utilities.dictionnary.ObjectDictionary;
import fr.alchemy.utilities.file.FileUtils;
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
	 * The table containing the opened editors.
	 */
	private final ObjectDictionary<Path, Tab> openedEditors = ObjectDictionary.ofType(Path.class, Tab.class);
	
	/**
	 * Instantiates a new <code>EditorTabPane</code> with the provided name and scene.
	 * 
	 * @param name  The name of the editor layout.
	 * @param scene The scene on which to attach the layout.
	 */
	public EditorTabPane(String name, AlchemyEditorScene scene) {
		super(name, scene);
	}

	/**
	 * Attaches the provided {@link EditorComponent} to the <code>EditorTabPane</code>.
	 * 
	 * @param component The component to add to the tab pane.
	 * @return			The updated tab pane.
	 */
	public TabPane attach(EditorComponent component) {
		
		
		Tab tab = new Tab(component.getName());
		tab.setClosable(true);
			
		if(component instanceof Pane) {
			Pane pane = (Pane) component;
			tab.setContent(pane);
			tab.setOnCloseRequest(e -> detach(component));
			pane.prefHeightProperty().bind(heightProperty());
//			EditorConfig.config().addOpenedComponent(name, component.getClass().getName());
		} else if(component instanceof FileEditor) {
			FileEditor editor = (FileEditor) component;
			tab.setContent(editor.getUIPage());
			tab.setOnCloseRequest(e -> detach(component));
			getContent().getSelectionModel().select(tab);
			editor.dirtyProperty().addListener((observable, oldValue, newValue) ->
			tab.setText(newValue == Boolean.TRUE ? "*" + editor.getName() : editor.getName()));
				
			editor.getUIPage().prefHeightProperty().bind(heightProperty());
			//EditorConfig.config().addOpenedFile(name, editor.getFile().toString());
			//openedEditors.put(editor.getFile(), tab);
		}
		
		components.add(component);
		getContent().getTabs().add(tab);
		return content;
	}
	
	@Override
	protected TabPane detach(EditorComponent component) {
		components.remove(component);
		EditorConfig.config().removeOpenedComponent(name, component.getClass().getName());
		
		if(component instanceof FileEditor) {
			Path file = ((FileEditor) component).getFile();
			
			openedEditors.remove(file);
			EditorConfig.config().removeOpenedFile(name, file.toString());
		}
		
		return content;
	}
	
	@Override
	public void save() {
		EditorConfig config = EditorConfig.config();
		
		List<String> componentClasses = config.getOpenedComponents(name);
		componentClasses.clear();
		
		List<String> files = config.getOpenedFiles(name);
		files.clear();
		
		components.forEach(c -> {
			if(!(c instanceof FileEditor)) {
				componentClasses.add(c.getClass().getName());
			}
		});
		
		openedEditors.keyArray(Path.class).forEach(p -> files.add(p.toString()));
	}
	
	/**
	 * Opens the provided file on this <code>EditorTabPane</code>.
	 * 
	 * @param file 	   The file which was requested for opening.
	 * @param readOnly Whether the file to open should be readable only.
	 */
	@Override
	protected void openFile(Path file, boolean readOnly) {
		Tab tab = openedEditors.get(file);
			
		// If the file is already opened in this pane, select it.
		if(tab != null) {
			getContent().getSelectionModel().select(tab);
			return;
		}
			
		String ext = FileUtils.getExtension(file);
		FileEditorRegistry.get().createFor(ext).ifPresent(editor -> {
			editor.setReadOnly(readOnly);
			editor.open(file);
			attach(editor);
		});
	}

	/**
	 * Creates a {@link TabPane} layout for the <code>EditorTabPane</code>.
	 * 
	 * @return A new tab pane instance.
	 */
	@Override
	protected TabPane createLayout() {
		return new TabPane();
	}
}
