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
		
		// Sets the title and resizable if needed.
		mainStage.setTitle(settings.getTitle() + " - " + settings.getVersion());
		mainStage.setResizable(settings.boolValue("Resizable"));
		
		// Enters fullscreen mode if needed.
		if(settings.boolValue("Fullscreen")) {
			mainStage.setFullScreen(true);
			mainStage.setFullScreenExitHint("");
		}
		
		if(settings.boolValue("Maximized")) {
			mainStage.setMaximized(true);
		} else {
			mainStage.setMaximized(false);
			mainStage.sizeToScene();
		}
		
		// Loads the icons for the window.
		final String[] iconPaths = settings.getIconPaths();
		for(int i = 0; i < iconPaths.length; i++) {
			if(iconPaths[i] != null && !iconPaths[i].isEmpty()) {
				mainStage.getIcons().add(application.getAssetManager().loadImage(iconPaths[i]));
			}
		}
		
		mainStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.equals(Boolean.TRUE) && application.hasStarted()) {
				application.resume();
				application.getListeners().forEach(ApplicationListener::show);	
			} else if (newValue.equals(Boolean.FALSE) && application.hasStarted()) {
				application.pause();
				for(int i = 0; i < application.getListeners().size(); i++) {
					application.getListeners().forEach(ApplicationListener::hide);	
				}
			}
		});
		
		mainStage.setScene(scene);
		
		mainStage.setOnCloseRequest(event -> application.exit());
	}
	
	/**
	 * Shows the window.
	 */
	public void show() {
		mainStage.show();
	}
}
