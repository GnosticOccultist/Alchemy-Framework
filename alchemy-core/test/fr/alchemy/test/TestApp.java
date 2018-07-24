package fr.alchemy.test;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.scene.component.NameComponent;
import fr.alchemy.core.scene.component.Transform;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.scene.entity.Entity;
import javafx.scene.input.KeyCode;

public class TestApp extends AlchemyApplication {

	private Entity entityTest;
	
	@Override
	protected void initializeSettings(AlchemySettings settings) {
		settings.put("Title", "Test App");
		settings.put("ShowFPS", true);
	}

	@Override
	protected void preInitialize() {
		
	}
	
	@Override
	protected void initialize() {
		Texture texture = assetManager.loadTexture("resources/icons/logo_colored_x32.png");
		
		entityTest = new Entity();
		entityTest.perform(VisualComponent.class, v -> v.getView().addNode(texture));
		entityTest.perform(VisualComponent.class, v -> v.grayscale());
	
		entityTest.attach(new NameComponent("Test"));
		scene.addEntity(entityTest);
	
		inputManager.addKeyTypedBinding(KeyCode.N, () -> entityTest.detach(NameComponent.class));
	}

	@Override
	protected void update() {
		entityTest.perform(Transform.class, t -> t.translate(1, 1));
	}

	public static void main(String[] args) {
		launch(args);
	}
}
