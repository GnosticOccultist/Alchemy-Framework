package fr.alchemy.test;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.executor.AlchemyExecutor;
import fr.alchemy.core.scene.SceneLayer;
import fr.alchemy.core.scene.component.NameComponent;
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
	protected void preInitialize() {
		
	}
	
	@Override
	protected void initialize() {
		Texture texture = assetManager.loadTexture("resources/icons/logo_colored_x32.png");
		
		entityTest = new Entity();
		entityTest.perform(VisualComponent.class, v -> v.getView().addNode(texture));
		entityTest.getComponent(VisualComponent.class).setSceneLayer(new SceneLayer("Test", 20));
		
		Entity entity = new Entity();
		entity.perform(VisualComponent.class, v -> v.getView().addNode(new Circle(20, Color.RED)));
		entity.getComponent(VisualComponent.class).setSceneLayer(SceneLayer.TOP);
		
		entityTest.attach(new NameComponent("Test"));
		entityTest.getComponent(NameComponent.class).setColor(Color.RED);
		scene.addEntity(entity);
		scene.addEntity(entityTest);
		
		AlchemyExecutor.executor().scheduleAtFixedRate(() -> System.out.println("ok"), 5000);
	
		inputManager.addKeyTypedBinding(KeyCode.N, () -> scene.removeEntity(entity));
	}

	@Override
	protected void update() {
		entityTest.perform(Transform.class, t -> t.translate(0.5, 0.5));
	}

	public static void main(String[] args) {
		launch(args);
	}
}
