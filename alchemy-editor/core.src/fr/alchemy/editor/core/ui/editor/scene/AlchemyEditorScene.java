package fr.alchemy.editor.core.ui.editor.scene;

import java.nio.file.Path;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.event.AlchemyEventManager;
import fr.alchemy.core.scene.AlchemyScene;
import fr.alchemy.core.scene.SceneLayer;
import fr.alchemy.editor.core.event.AlchemyEditorEvent;
import fr.alchemy.editor.core.ui.editor.bar.AlchemyEditorBar;
import fr.alchemy.editor.core.ui.editor.layout.EditorTabPane;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor;
import fr.alchemy.utilities.file.FileUtils;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AlchemyEditorScene extends AlchemyScene {
	
	/**
	 * The layout of the editor.
	 */
	private final EditorTabPane tabPane;
	
	private final EditorTabPane scenePane;
	/**
	 * The container of the scene.
	 */
	private SplitPane container;
	
	public AlchemyEditorScene(AlchemyApplication application) {
		super(application);
		
		this.tabPane = new EditorTabPane(this);
		this.scenePane = new EditorTabPane(this);
		this.container = new SplitPane();
		AlchemyEventManager.events().registerEventHandler(AlchemyEditorEvent.OPEN_FILE, this::openFile);
	}
	
	private void openFile(AlchemyEditorEvent event) {
		Path file = event.getPath("file");
		String ext = FileUtils.getExtension(file);
		
		if(ext.equals("properties")) {
			
			PropertiesEditor editor = new PropertiesEditor();
			editor.open(file);
			
			Tab tab = new Tab(editor.getName());
			scenePane.getContent().getTabs().add(tab);
			tab.setContent(editor.getRoot());
			
			scenePane.getContent().getSelectionModel().select(tab);
			editor.dirtyProperty().addListener((observable, oldValue, newValue) ->
            tab.setText(newValue == Boolean.TRUE ? "*" + editor.getName() : editor.getName()));
		}
	}

	@Override
	public void initialize(double width, double height) {
		super.initialize(width, height);
		
		this.root = new Pane();
		setPrefSize(width, height);
		mainScene.setRoot(root);
		root.requestFocus();
		
		// Set up the editor bar.
		AlchemyEditorBar bar = new AlchemyEditorBar();
		bar.prefWidthProperty().bind(root.widthProperty());
		
		tabPane.construct();
		
		scenePane.getContent().prefHeightProperty().bind(root.heightProperty());
		scenePane.getContent().setSide(Side.TOP);
		
		tabPane.getContent().prefHeightProperty().bind(root.heightProperty());
		tabPane.getContent().setSide(Side.LEFT);

		container.getItems().addAll(tabPane.getContent(), scenePane.getContent());
		container.prefHeightProperty().bind(root.heightProperty());
		container.prefWidthProperty().bind(root.widthProperty());
		
		root.getChildren().addAll(new VBox(bar, container));
		
		tabPane.notifyFinishBuild();
	}
	
	public Group layer(SceneLayer layer) {
		return getRenderBatch(layer);
	}
	
	public EditorTabPane getTabPane() {
		return tabPane;
	}
}
