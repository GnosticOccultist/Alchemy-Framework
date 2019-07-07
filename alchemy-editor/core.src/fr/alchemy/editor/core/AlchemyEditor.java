package fr.alchemy.editor.core;

import fr.alchemy.editor.core.asset.AssetManager;
import fr.alchemy.editor.core.ui.editor.scene.AlchemyEditorScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class AlchemyEditor extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Alchemy Editor - Alpha 0.1");
		primaryStage.setResizable(true);
		primaryStage.setMaximized(true);
		
		AlchemyEditorScene scene = new AlchemyEditorScene();
		scene.initialize(primaryStage.getWidth(), primaryStage.getHeight());
		primaryStage.setScene(scene.getFXScene());
		
		primaryStage.setOnCloseRequest(event -> scene.save());
		
		EditorManager.initialize(new AssetManager());
		
		primaryStage.show();
	}
}
