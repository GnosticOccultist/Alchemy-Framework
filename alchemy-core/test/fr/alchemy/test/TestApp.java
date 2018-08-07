package fr.alchemy.test;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.core.asset.Music;
import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.executor.AlchemyExecutor;
import fr.alchemy.core.physic.BoundingBox;
import fr.alchemy.core.scene.SceneLayer;
import fr.alchemy.core.scene.component.NameComponent;
import fr.alchemy.core.scene.component.Transform;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.scene.entity.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TestApp extends AlchemyApplication {

	private Entity entityTest;
	private Music musicTest;
	private BoundingBox box;
	
	@Override
	protected void initializeSettings(AlchemySettings settings) {
		settings.put("Title", "Test App");
		settings.put("ShowFPS", true);
	}
	
	@Override
	protected void preInitialize() {	
		musicTest = assetManager.loadMusic("/resources/sounds/ferambie.mp3"); 	
	}

	@Override
	protected void initialize() {
		Texture texture = assetManager.loadTexture("resources/icons/logo_colored_x32.png");
		
		entityTest = new Entity();
		entityTest.perform(VisualComponent.class, v -> v.getView().addNode(texture));
		entityTest.getComponent(VisualComponent.class).setSceneLayer(new SceneLayer("Test", 20));
		
		Entity entity = new Entity();
		Rectangle rect = new Rectangle(20, 20, 25, 25);
		rect.setFill(Color.RED);
		entity.perform(VisualComponent.class, v -> v.getView().addNode(rect));
		entity.getComponent(VisualComponent.class).setSceneLayer(SceneLayer.TOP);
		
		entityTest.attach(new NameComponent("Test"));
		entityTest.getComponent(NameComponent.class).setColor(Color.RED);
		scene.addEntity(entity);
		scene.addEntity(entityTest);
		
		AlchemyExecutor.executor().scheduleAtFixedRate(() -> System.out.println("ok"), 5000);
		
		box = new BoundingBox(20, 20, 25, 25);
	
		inputManager.addKeyTypedBinding(KeyCode.N, () -> scene.removeEntity(entity));
		
		AlchemyExecutor.executor().performOnBackground(() -> {
			musicTest.setCycleCount(Integer.MAX_VALUE);
			musicTest.play();
		});	
	}

	@Override
	protected void update() {
		entityTest.perform(Transform.class, t -> t.translate(0.5, 0.5));
		if(box.contains(inputManager.getMouse().screenX, inputManager.getMouse().screenY)) {
			System.out.println("Contained!");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
