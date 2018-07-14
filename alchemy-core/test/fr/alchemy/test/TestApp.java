package fr.alchemy.test;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.core.entity.Entity;
import javafx.scene.layout.Pane;

public class TestApp extends AlchemyApplication {

	@Override
	protected void initializeSettings(AlchemySettings settings) {
		settings.put("Title", "Test App");
	}

	@Override
	protected void initialize(Pane appRoot, Pane uiRoot) {
		Entity entity = new Entity();
		appRoot.getChildren().add(entity);
	}

	@Override
	protected void update(long now) {}

	public static void main(String[] args) {
		launch(args);
	}
}
