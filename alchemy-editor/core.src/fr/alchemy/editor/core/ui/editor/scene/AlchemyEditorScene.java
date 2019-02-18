package fr.alchemy.editor.core.ui.editor.scene;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.scene.AlchemyScene;
import fr.alchemy.core.scene.SceneLayer;
import fr.alchemy.editor.core.ui.editor.bar.AlchemyEditorBar;
import fr.alchemy.editor.core.ui.editor.graph.SimpleGraphNodeEditor;
import fr.alchemy.editor.core.ui.editor.layout.EditorTabPane;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.SplitPane;
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
		
		this.tabPane = new EditorTabPane("components.tab.pane", this);
		this.scenePane = new EditorTabPane("editors.tab.pane", this);
		this.container = new SplitPane();
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
		scenePane.construct();
		
		SimpleGraphNodeEditor editor = new SimpleGraphNodeEditor();
		editor.newNode();
		editor.newNode();
		editor.reload();
		scenePane.attach(editor);
		
		scenePane.getContent().prefHeightProperty().bind(root.heightProperty());
		scenePane.getContent().setSide(Side.TOP);
		
		tabPane.getContent().prefHeightProperty().bind(root.heightProperty());
		tabPane.getContent().setSide(Side.LEFT);

		container.setDividerPosition(0, 0);
		container.getItems().addAll(tabPane.getContent(), scenePane.getContent());
		container.prefHeightProperty().bind(root.heightProperty());
		container.prefWidthProperty().bind(root.widthProperty());
		
		root.getChildren().addAll(new VBox(bar, container));
		
		tabPane.notifyFinishBuild();
	}
	
	public Group layer(SceneLayer layer) {
		return getRenderBatch(layer);
	}
	
	public void save() {
		tabPane.save();
		scenePane.save();
	}
}
