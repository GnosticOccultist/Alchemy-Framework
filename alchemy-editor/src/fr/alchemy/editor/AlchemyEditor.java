package fr.alchemy.editor;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.editor.scene.AlchemyEditorScene;

public class AlchemyEditor extends AlchemyApplication {
	
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
		scene = new AlchemyEditorScene(this);
	}
	
	@Override
	protected void initialize() {}

	@Override
	protected void update() {}
}
