package fr.alchemy.core.scene.component;

import fr.alchemy.core.annotation.CoreComponent;
import fr.alchemy.core.scene.entity.Entity;
import fr.alchemy.core.scene.entity.EntityView;
import javafx.scene.Node;

/**
 * <code>VisualComponent</code> handles the {@link EntityView visual aspect} of an {@link Entity entity}.
 * It can be used to add visual nodes for the entity.
 * 
 * @author GnosticOccultist
 */
@CoreComponent
public final class VisualComponent extends Component {
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
		unbindView();
		
		super.onDetached(entity);
	}
	
	@Override
	public void cleanup() {
		unbindView();
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
	 * Unbinds the <code>EntityView</code> from the <code>Transform</code> component
	 * of the <code>Entity</code>.
	 */
	private void unbindView() {
		getView().translateXProperty().unbind();
		getView().translateYProperty().unbind();
		
		getView().rotateProperty().unbind();
		
		getView().scaleXProperty().unbind();
		getView().scaleYProperty().unbind();
	}
	
	/**
	 * @return The <code>EntityView</code>.
	 */
	public EntityView getView() {
		return view;
	}
}
