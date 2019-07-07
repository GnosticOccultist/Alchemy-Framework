package fr.alchemy.editor.core.ui.editor.scene;

import fr.alchemy.editor.core.ui.editor.bar.AlchemyEditorBar;
import fr.alchemy.editor.core.ui.editor.layout.EditorTabPane;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AlchemyEditorScene {
	
	private final Pane root = new Pane();
	
	private final Scene mainScene = new Scene(root);
	/**
	 * The layout of the editor.
	 */
	private final EditorTabPane tabPane;
	
	private final EditorTabPane scenePane;
	/**
	 * The container of the scene.
	 */
	private SplitPane container;
	
	public AlchemyEditorScene() {
		
		this.tabPane = new EditorTabPane("components.tab.pane", this);
		this.scenePane = new EditorTabPane("editors.tab.pane", this);
		this.container = new SplitPane();
	}

	public void initialize(double width, double height) {
		root.setPrefSize(width, height);
		mainScene.setRoot(root);
		root.requestFocus();
		
		// Set up the editor bar.
		AlchemyEditorBar bar = new AlchemyEditorBar();
		bar.prefWidthProperty().bind(root.widthProperty());
		
		tabPane.construct();
		scenePane.construct();
		
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
	
	public void save() {
		tabPane.save();
		scenePane.save();
	}
	
	public Scene getFXScene() {
		return mainScene;
	}
}
