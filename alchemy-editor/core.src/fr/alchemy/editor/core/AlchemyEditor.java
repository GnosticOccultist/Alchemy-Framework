package fr.alchemy.editor.core;

import fr.alchemy.editor.core.asset.AssetManager;
import fr.alchemy.editor.core.ui.editor.scene.AlchemyEditorScene;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * <code>AlchemyEditor</code> is an implementation of a JavaFX {@link Application} which starts and configures the editor window. 
 * 
 * @author GnosticOccultist
 */
public class AlchemyEditor extends Application {
	
	/**
	 * The name of the editor displayed in the window's title.
	 */
	private static final String EDITOR_NAME = "Alchemy Editor";
	/**
	 * The version of the editor displayed in the window's title.
	 */
	private static final String EDITOR_VERSION = "pre-alpha 0.1";
	
	/**
	 * The main method to launch the <code>AlchemyEditor</code> using the given arguments.
	 * 
	 * @param args The arguments to use when launching the editor (not used).
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Starts the <code>AlchemyEditor</code> by creating the window.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(EDITOR_NAME + " - " + EDITOR_VERSION);
		primaryStage.setResizable(true);
		primaryStage.setMaximized(true);
		
		AlchemyEditorScene scene = new AlchemyEditorScene();
		scene.initialize(primaryStage.getWidth(), primaryStage.getHeight());
		primaryStage.setScene(scene.getFXScene());
		
		primaryStage.setOnCloseRequest(event -> scene.save());
		
		EditorManager.initialize(new AssetManager());
		
		// Once everything is prepared, show the window.
		primaryStage.show();
	}
}
