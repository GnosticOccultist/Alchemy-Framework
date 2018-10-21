package fr.alchemy.editor.core.ui.editor.scene;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.scene.AlchemyScene;
import fr.alchemy.core.scene.SceneLayer;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.scene.entity.Entity;
import fr.alchemy.editor.core.ui.editor.bar.AlchemyEditorBar;
import fr.alchemy.editor.core.ui.editor.layout.EditorTabPane;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AlchemyEditorScene extends AlchemyScene {
	
	/**
	 * The layout of the editor.
	 */
	private final EditorTabPane tabPane;
	/**
	 * The container of the scene.
	 */
	private SplitPane container;
	
	public AlchemyEditorScene(AlchemyApplication application) {
		super(application);
		
		this.tabPane = new EditorTabPane();
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
		
		getContentRoot().prefWidthProperty().bind(root.widthProperty());
		getContentRoot().prefHeightProperty().bind(root.heightProperty());
		getContentRoot().setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		
		Entity entity = new Entity();
		Circle circle = new Circle(20, 20, 25);
		circle.setFill(Color.RED);
		entity.perform(VisualComponent.class, v -> v.getView().addNode(circle));
		addEntity(entity);
		
		Entity entity2 = new Entity();
		Circle circle2 = new Circle(500, 500, 500);
		circle2.setFill(Color.BLUE);
		entity2.perform(VisualComponent.class, v -> v.getView().addNode(circle2));
		entity2.getComponent(VisualComponent.class).setSceneLayer(new SceneLayer("Special", 100));
		addEntity(entity2);
		
		tabPane.construct();
		
		tabPane.getContent().prefHeightProperty().bind(root.heightProperty());
		tabPane.getContent().setSide(Side.LEFT);

		container.getItems().addAll(tabPane.getContent(), getContentRoot());
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
