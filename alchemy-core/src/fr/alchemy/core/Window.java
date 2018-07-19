package fr.alchemy.core;

import fr.alchemy.core.listener.ApplicationListener;
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
		
		// Sets the title and resizable if needed.
		mainStage.setTitle(settings.getTitle() + " " + settings.getVersion());
		mainStage.setResizable(settings.boolValue("Resizable"));
		
		// Enters fullscreen mode if needed.
		if(settings.boolValue("Fullscreen")) {
			mainStage.setFullScreen(true);
			mainStage.setFullScreenExitHint("");
		}
		
		// Loads the icons for the window.
		final String[] iconPaths = settings.getIconPaths();
		for(String iconPath : iconPaths) {
			if(iconPath != null && !iconPath.isEmpty()) {
				mainStage.getIcons().add(application.getAssetManager().loadImage(iconPath));
			}
		}
		
		mainStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.equals(Boolean.TRUE) && application.hasStarted()) {
				application.resume();
				application.getListeners().forEach(ApplicationListener::show);
			} else if (newValue.equals(Boolean.FALSE) && application.hasStarted()) {
				application.pause();
				application.getListeners().forEach(ApplicationListener::hide);
			}
		});
		
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
