package fr.alchemy.test;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.core.asset.Music;
import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.executor.AlchemyExecutor;
import fr.alchemy.core.physic.BoundingCircle;
import fr.alchemy.core.scene.SceneLayer;
import fr.alchemy.core.scene.component.NameComponent;
import fr.alchemy.core.scene.component.SimpleObjectComponent;
import fr.alchemy.core.scene.component.Transform;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.scene.entity.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TestApp extends AlchemyApplication {

	private Entity entityTest;
	private Music musicTest;
	private BoundingCircle circ;
	
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
		
		entityTest.forcePerform(VisualComponent.class, v -> v.getView().addNode(texture));
		entityTest.getComponent(VisualComponent.class).setSceneLayer(new SceneLayer("Test", 20));
		
		entityTest.attach(new NameComponent("Test"));
		entityTest.getComponent(NameComponent.class).setColor(Color.RED);
		
		Entity entity = new Entity();
		Circle circle = new Circle(20, 20, 25);
		circle.setFill(Color.RED);
		entity.perform(VisualComponent.class, v -> v.getView().addNode(circle));
		entity.getComponent(VisualComponent.class).setSceneLayer(SceneLayer.TOP);
		
		scene.addEntity(entityTest);
		
		AlchemyExecutor.executor().scheduleAtFixedRate(() -> System.out.println("ok"), 5000);
		
		circ = new BoundingCircle(20, 20, 25);
		entityTest.attach(new SimpleObjectComponent<BoundingCircle>(circ));
		
		inputManager.addKeyTypedBinding(KeyCode.N, () -> entityTest.setEnabled(true));
		
		AlchemyExecutor.executor().performOnBackground(() -> {
			musicTest.setCycleCount(Integer.MAX_VALUE);
			musicTest.play();
		});	
		
		assetManager.saveAsset(entityTest, "resources/entity/entity.ecs");
		Entity clone = (Entity) assetManager.loadAsset("resources/entity/entity.ecs");

		clone.getComponent(Transform.class).setPosition(0, 50);
		scene.addEntity(clone);
	}

	@Override
	protected void update() {
		entityTest.getComponent(Transform.class).setPosition(inputManager.getMouse().screenX, inputManager.getMouse().screenY);
		if(circ.contains(inputManager.getMouse().screenX, inputManager.getMouse().screenY)) {
			System.out.println("Contained!");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
