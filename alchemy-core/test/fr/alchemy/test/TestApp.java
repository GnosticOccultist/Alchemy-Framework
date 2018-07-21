package fr.alchemy.test;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.listener.ApplicationListener;
import fr.alchemy.core.scene.component.Transform;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.scene.entity.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TestApp extends AlchemyApplication {

	private Entity entityTest;
	
	@Override
	protected void initializeSettings(AlchemySettings settings) {
		settings.put("Title", "Test App");
		settings.put("ShowFPS", true);
	}

	@Override
	protected void initialize() {
		Texture texture = assetManager.loadTexture("resources/icons/logo_colored_x32.png");
		texture.grayscale();
		
		entityTest = new Entity();
		entityTest.perform(VisualComponent.class, v -> v.getView().addNode(texture));
		scene.addEntity(entityTest);
		
		registerListener(new ApplicationListener() {
			@Override
			public final void resume() {
				System.out.println("Resuming");
			}
		});
	}

	@Override
	protected void update() {
		entityTest.perform(Transform.class, t -> t.rotate(10));
		entityTest.perform(Transform.class, t -> t.setPosition(inputManager.getMouse().getAppX(), inputManager.getMouse().getAppY()));
		
		inputManager.addKeyPressBinding(KeyCode.T, () -> entityTest.perform(VisualComponent.class, v -> {
			v.getView().clear();
			v.getView().addNode(new Circle(10, Color.RED));
		}));
	
	}

	public static void main(String[] args) {
		launch(args);
	}
}
