package fr.alchemy.editor.core.ui.editor.scene;

import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.ui.FXUtils;
import fr.alchemy.editor.core.ui.editor.bar.AlchemyEditorBar;
import fr.alchemy.editor.core.ui.editor.layout.EditorTabPane;
import fr.alchemy.editor.core.ui.editor.layout.FileEditorTabPane;
import fr.alchemy.utilities.ByteUtils;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AlchemyEditorScene {
	
	/**
	 * The root pane of the scene.
	 */
	private final Pane root = new Pane();
	/**
	 * The internal scene.
	 */
	private final Scene mainScene = new Scene(root);
	/**
	 * The layout of the editor containing the workspace hierarchy.
	 */
	private final EditorTabPane tabPane;
	/**
	 * The layout of the editor containing the file editors.
	 */
	private final FileEditorTabPane editorsPane;
	/**
	 * The container of the scene.
	 */
	private SplitPane container;
	
	public AlchemyEditorScene() {
		this.tabPane = new EditorTabPane("components.tab.pane", this);
		this.editorsPane = new FileEditorTabPane("editors.tab.pane", this);
		this.container = new SplitPane();
	}

	public void initialize(double width, double height) {
		mainScene.getStylesheets().addAll("/resources/css/custom.css");
		
		root.setPrefSize(width, height);
		mainScene.setRoot(root);
		root.requestFocus();
		
		// Set up the editor bar.
		AlchemyEditorBar bar = new AlchemyEditorBar();
		bar.prefWidthProperty().bind(root.widthProperty());
		
//		EditorConfig config = EditorConfig.config();
//		
//		config.getOpenedComponents("components.tab.pane").clear();
//		config.getOpenedFiles("editors.tab.pane").clear();
//		config.getOpenedComponents("editors.tab.pane").clear();
		
		tabPane.construct();
		editorsPane.construct();
		
		editorsPane.getContent().prefHeightProperty().bind(root.heightProperty());
		editorsPane.getContent().setSide(Side.TOP);
		
		tabPane.getContent().prefHeightProperty().bind(root.heightProperty());
		tabPane.getContent().setSide(Side.LEFT);

		container.getItems().addAll(tabPane.getContent(), editorsPane.getContent());
		
		FXUtils.performFXThread(() -> container.setDividerPositions(getDividerPositions()), true);
		
		container.prefHeightProperty().bind(root.heightProperty());
		container.prefWidthProperty().bind(root.widthProperty());
		
		root.getChildren().addAll(new VBox(bar, container));
		
		tabPane.notifyFinishBuild();
	}
	
	/**
	 * Return the divider positions for the {@link SplitPane}, either the saved ones in the {@link EditorConfig} or
	 * the default ones: 0.2D and 0.8D.
	 * 
	 * @return The divider positions for the editor split pane.
	 */
	private double[] getDividerPositions() {
		byte[] array = EditorConfig.preferences().getByteArray(EditorConfig.PREF_DIVIDER_POS, null);
		if(array != null) {
			return ByteUtils.deserialize(array);
		}
		return new double[] { 0.2D, 0.8D };
	}
	
	/**
	 * Save the <code>AlchemyEditorScene</code> and the {@link EditorConfig}.
	 */
	public void save() {
		tabPane.save();
		editorsPane.save();
		
		EditorConfig.preferences().putByteArray(EditorConfig.PREF_DIVIDER_POS, 
				ByteUtils.serialize(container.getDividerPositions()));
		
		EditorConfig.config().save();
	}
	
	/**
	 * Return the internal JavaFX {@link Scene} of the <code>AlchemyEditorScene</code>.
	 * 
	 * @return The internal scene of the editor.
	 */
	public Scene getFXScene() {
		return mainScene;
	}
}
