package fr.alchemy.editor.core.ui.editor.layout;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import fr.alchemy.editor.api.control.DraggableTab;
import fr.alchemy.editor.api.control.DraggableTabPane;
import fr.alchemy.editor.api.editor.FileEditor;
import fr.alchemy.editor.api.editor.FileEditorRegistry;
import fr.alchemy.editor.api.editor.layout.EditorLayout;
import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.event.AlchemyEditorEvent;
import fr.alchemy.editor.core.ui.editor.scene.AlchemyEditorScene;
import fr.alchemy.utilities.collections.dictionnary.ObjectDictionary;
import fr.alchemy.utilities.event.EventBus;
import fr.alchemy.utilities.event.EventType;
import fr.alchemy.utilities.file.FileUtils;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class FileEditorTabPane extends EditorLayout<TabPane, FileEditor> {
	
	/**
	 * The table containing the opened editors.
	 */
	private final ObjectDictionary<Path, Tab> openedEditors = ObjectDictionary.ofType(Path.class, Tab.class);

	public FileEditorTabPane(String name, AlchemyEditorScene scene) {
		super(name, scene);
		
		EventBus.addListener(AlchemyEditorEvent.OPEN_FILE, this);
	}

	@Override
	protected TabPane attach(FileEditor editor) {
		DraggableTab tab = new DraggableTab(editor);
		tab.setClosable(true);
		
		tab.setContent(editor.getUIPage());
		tab.setOnCloseRequest(e -> detach(editor));
		getContent().getSelectionModel().select(tab);
		editor.dirtyProperty().addListener((observable, oldValue, newValue) -> {
			String prefix = newValue == Boolean.TRUE ? "*" : "";
			tab.setText(prefix + editor.getName());
		});
		
				
		editor.getUIPage().prefHeightProperty().bind(heightProperty());
		//EditorConfig.config().addOpenedFile(name, editor.getFile().toString());
		openedEditors.put(editor.getFile(), tab);
		
		components.add(editor);
		getContent().getTabs().add(tab);
		
		return content;
	}
	
	@Override
	protected void constructComponent(String path) {
		openFile(Paths.get(path), false); // TODO: Serialize read-only state
	}
	
	@Override
	public void newEvent(EventType<AlchemyEditorEvent> type, AlchemyEditorEvent event) {
		if(type.equals(AlchemyEditorEvent.OPEN_FILE)) {
			openFile(event.getPath("file"), event.getBoolean("readOnly"));
		}
	}
	
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
			 
			// Request the focus of the root.
			editor.getRoot().requestFocus();
		});
	}
	
	@Override
	public void save() {
		EditorConfig config = EditorConfig.config();
		
		List<String> files = config.getOpenedFiles(name);
		files.clear();
		
		List<String> components = config.getOpenedComponents(name);
		components.clear();
		
		openedEditors.keyArray(Path.class).forEach(p -> components.add(p.toString()));
	}

	@Override
	protected TabPane detach(FileEditor component) {
		components.remove(component);
		EditorConfig.config().removeOpenedComponent(name, component.getClass().getName());
		
		Path file = ((FileEditor) component).getFile();
		
		openedEditors.remove(file);
		EditorConfig.config().removeOpenedFile(name, file.toString());
		
		return content;
	}

	@Override
	protected TabPane createLayout() {
		return new DraggableTabPane(FileEditor.class::isInstance);
	}
}
