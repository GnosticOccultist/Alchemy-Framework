package fr.alchemy.editor.core;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.core.ApplicationListener;
import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.ui.editor.scene.AlchemyEditorScene;

public class AlchemyEditor extends AlchemyApplication  {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	protected void initializeSettings(AlchemySettings settings) {
		settings.put("Title", "Alchemy Editor");
		settings.put("Version", "[alpha-0.1.0]");
		settings.put("Resizable", true);
		settings.put("Maximized", true);
	}
	
	@Override
	protected void preInitialize() {
		EditorManager.initialize(assetManager);
		
		scene = new AlchemyEditorScene(this);
	}
	
	@Override
	protected void initialize() {
		registerListener(setupListener());
	}

	@Override
	protected void update() {}
	
	/**
	 * Setup the <code>ApplicationListener</code> for the <code>AlchemyEditor</code>
	 * 
	 * @return The application listener to register.
	 */
	private ApplicationListener setupListener() {
		return new ApplicationListener() {
		
			@Override
			public void exit() {
				EditorConfig.config().save();
			}
		};
	}
}
