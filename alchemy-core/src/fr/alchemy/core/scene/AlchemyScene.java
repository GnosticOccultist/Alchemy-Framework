package fr.alchemy.core.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.event.AlchemyEventManager;
import fr.alchemy.core.event.AlchemySceneEvent;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.scene.entity.Entity;
import fr.alchemy.core.scene.entity.EntityView;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 * <code>AlchemyScene</code> represents a basic scene in the <code>AlchemyApplication</code>.
 * It can be used to add/remove/update {@link Entity entities} or UI components.
 * 
 * @author Stickxy
 */
public class AlchemyScene extends AbstractScene {

	/**
	 * The root for all the entities.
	 */
	protected Group sceneRoot = new Group(); 
	/**
	 * The overlay root above {@link #sceneRoot}. Contains all the UI elements, native JavaFX nodes.
	 * May also contain some entities. The UI root isn't affected by viewport movement.
	 */
	protected Group uiRoot = new Group();
	
	public AlchemyScene(final AlchemyApplication application) {
		super(application);
	}
	
	@Override
	public void initialize(final double width, final double height) {
		super.initialize(width, height);
		
		setBackgroundColor(Color.BLACK);
	
		getContentRoot().getChildren().addAll(sceneRoot, uiRoot);
	}
	
	/**
	 * Adds the specified <code>Entity</code> to the <code>AlchemyScene</code>
	 * and attach its <code>VisualComponent</code>.
	 * <p>
	 * Notify also about the {@link AlchemySceneEvent#ENTITY_ADDED} event.
	 * 
	 * @param entity The entity to add.
	 */
	@Override
	public void addEntity(final Entity entity) {
		super.addEntity(entity);
		
		final SceneLayer layer = entity.getComponent(VisualComponent.class).getSceneLayer();
		final EntityView view = entity.getComponent(VisualComponent.class).getView();
		getRenderBatch(layer).getChildren().add(view);
		
		AlchemyEventManager.events().notify(AlchemySceneEvent.entityAdded(entity));
	}
	
	/**
	 * Removes the specified <code>Entity</code> from the <code>AlchemyScene</code>
	 * and detach its <code>VisualComponent</code>.
	 * <p>
	 * Notify also about the {@link AlchemySceneEvent#ENTITY_REMOVED} event.
	 * 
	 * @param entity The entity to remove.
	 */
	public void removeEntity(final Entity entity) {
		this.entities.remove(entity);
		
		final SceneLayer layer = entity.getComponent(VisualComponent.class).getSceneLayer();
		final EntityView view = entity.getComponent(VisualComponent.class).getView();
		getRenderBatch(layer).getChildren().remove(view);
		
		AlchemyEventManager.events().notify(AlchemySceneEvent.entityRemoved(entity));
	}
	
	/**
	 * Adds the specified UI element to the <code>AlchemyScene</code>.
	 */
	@Override
	protected void addUINode(final Node uiNode) {
		super.addUINode(uiNode);
		this.uiRoot.getChildren().add(uiNode);
		
		AlchemyEventManager.events().notify(AlchemySceneEvent.UINodeAdded(uiNode));
	}
	
	/**
	 * Removes the specified UI element from the <code>AlchemyScene</code>.
	 */
	protected void removeUINode(final Node uiNode) {
		this.uiRoot.getChildren().remove(uiNode);
		
		AlchemyEventManager.events().notify(AlchemySceneEvent.UINodeRemoved(uiNode));
	}
	
	/**
	 * Converts a point on the screen into a point in the <code>AlchemyScene</code>.
	 * 
	 * @param screenPoint The point in UI coordinates.
	 * @return			  The point in scene coordinates.
	 */
	public Point2D screenToGame(final Point2D screenPoint) {
        return screenPoint.multiply(1.0 / getSizeRatio()).add(viewport.getOrigin());
    }
	
	/**
	 * Sets the background color to the specified color.
	 * 
	 * @param color The background color.
	 */
	public void setBackgroundColor(final Color color) {
		getFXScene().setFill(color);
		getRoot().setBackground(new Background(new BackgroundFill(color, null, null)));
	}
	
	private Group getRenderBatch(final SceneLayer layer) {
		Integer sceneLayer = layer.index();
		
		Group batch = null;
		
		for(Node node : sceneRoot.getChildren()) {
			if((int) node.getUserData() == sceneLayer) {
				batch = (Group) node;
				break;
			}
		}
		
		if(batch == null) {
			batch = new Group();
			batch.setUserData(sceneLayer);
			sceneRoot.getChildren().add(batch);
		}
		
		List<Node> tmpBatches = new ArrayList<>(sceneRoot.getChildren());
		Collections.sort(tmpBatches, (b1, b2) -> Integer.compare((int) b1.getUserData(), (int) b2.getUserData()));
		
		sceneRoot.getChildren().setAll(tmpBatches);
		
		return batch;
	}
	
	/**
	 * Updates the scene-graph by updating each entities.
	 * 
	 * @param now The current time.
	 */
	public void update(final long now) {
		entities.forEach(entity -> entity.update(now));
	}

	@Override
	protected Group getSceneRoot() {
		return sceneRoot;
	}

	@Override
	protected Group getUIRoot() {
		return uiRoot;
	}
}
