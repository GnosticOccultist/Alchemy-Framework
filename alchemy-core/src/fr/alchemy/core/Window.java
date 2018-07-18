package fr.alchemy.core;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window {
	
	/**
	 * The application which handle the window.
	 */
	private AlchemyApplication application;
	/**
	 * The application window.
	 */
	private Stage mainStage;
	
	public Window(final AlchemyApplication application) {
		this.application = application;
	}
	
	/**
	 * Initializes the window using the <code>AlchemySettings</code>.
	 * 
	 * @param stage		The stage representing the window.
	 * @param mainScene The main scene.
	 */
	public void initialize(final Stage stage, final Scene scene) {
		this.mainStage = stage;
		
		final AlchemySettings settings = AlchemySettings.settings();
		
		mainStage.setTitle(settings.getTitle() + " " + settings.getVersion());
		mainStage.setResizable(settings.value("Resizable"));
		
		if((boolean) settings.value("Fullscreen")) {
			mainStage.setFullScreen(true);
			mainStage.setFullScreenExitHint("");
		}
		
		mainStage.setScene(scene);
		mainStage.sizeToScene();
		
		mainStage.setOnCloseRequest(event -> application.exit());
	}
	
	/**
	 * Shows the window.
	 */
	public void show() {
		mainStage.show();
	}
}
