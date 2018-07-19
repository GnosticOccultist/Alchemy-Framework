package fr.alchemy.core.scene.component;

import fr.alchemy.core.scene.entity.Entity;
import fr.alchemy.core.scene.entity.EntityView;
import javafx.scene.Node;

public class VisualComponent extends Component {
	/**
	 * The view bounded to the entity.
	 */
	private final EntityView view = new EntityView();
	
	/**
	 * Instantiates a new empty <code>VisualComponent</code>.
	 */
	public VisualComponent() {}
	
	/**
	 * Instantiates a new <code>VisualComponent</code> with the specified
	 * graphic node for the <code>EntityView</code>.
	 * 
	 * @param graphic The graphic node to add.
	 */
	public VisualComponent(final Node graphic) {
		getView().addNode(graphic);
	}
	
	@Override
	public void onAttached(final Entity entity) {
		super.onAttached(entity);
		
		bindView();
	}
	
	@Override
	public void onDetached(final Entity entity) {
		getView().clear();
		
		super.onDetached(entity);
	}
	
	@Override
	public void cleanup() {
		getView().clear();
		
		super.cleanup();
	}
	
	/**
	 * Binds the <code>EntityView</code> to the <code>Transform</code> component
	 * of the <code>Entity</code>.
	 */
	private void bindView() {
		final Transform transform = getOwner().getComponent(Transform.class);
		
		getView().translateXProperty().bind(transform.posXProperty());
		getView().translateYProperty().bind(transform.posYProperty());
		
		getView().rotateProperty().bind(transform.rotationProperty());
		
		getView().scaleXProperty().bind(transform.scaleXProperty());
		getView().scaleYProperty().bind(transform.scaleYProperty());
	}
	
	/**
	 * @return The <code>EntityView</code>.
	 */
	public EntityView getView() {
		return view;
	}
}
