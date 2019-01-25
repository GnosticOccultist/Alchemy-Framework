package fr.alchemy.editor.api.editor.graph.skin;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.api.editor.region.DraggableBox;

/**
 * <code>GraphJointSkin</code> is an implementation of {@link GraphSkin} for {@link GraphJoint}, which
 * is reponsible for visualizing joints inside the {@link GraphNodeEditor}.
 * <p>
 * Any custom joint skin must extend this class. It <b>must</b> also provides the super-implementation
 * constructor type in order to be applied for a specific <code>GraphConnection</code>.
 * <p>
 * The root node of this skin is a {@link DraggableBox}.
 * 
 * @author GnosticOccultist
 */
public abstract class GraphJointSkin extends GraphSkin<GraphJoint> {

	/**
	 * The root draggable box used by the joint skin.
	 */
	private final DraggableBox root = new DraggableBox() {
		@Override
		public final void positionMoved() {
			super.positionMoved();
			GraphJointSkin.this.positionMoved();
		}
	};
	
	/**
	 * Instantiates a new <code>GraphJointSkin</code> for the specified {@link GraphJoint}.
	 * 
	 * @param joint The graph joint using the graph skin.
	 */
	public GraphJointSkin(GraphJoint joint) {
		super(joint);
	}
	
	/**
	 * Initializes the <code>GraphJointSkin</code>.
	 * The skin's layout values are loaded from the {@link GraphJoint} at this point.
	 */
	public void initialize() {
		getRoot().setLayoutX(getElement().getX() - getWidth() / 2);
		getRoot().setLayoutY(getElement().getY() - getHeight() / 2);
	}
	
	/**
	 * Return the width of the <code>GraphJointSkin</code>.
	 * <p>
	 * For robust behaviour this should return the correct height at all times. Note that
	 * <code>getRoot().getWidth()</code> doesn't meet this condition as it will return 0 until
	 * the {@link GraphJoint}'s node is added to the scene-graph and layed out.
	 * 
	 * @return The width of the joint.
	 */
	public abstract double getWidth();
	
	/**
	 * Return the height of the <code>GraphJointSkin</code>.
	 * <p>
	 * For robust behaviour this should return the correct height at all times. Note that
	 * <code>getRoot().getHeight()</code> doesn't meet this condition as it will return 0 until
	 * the {@link GraphJoint}'s node is added to the scene-graph and layed out.
	 * 
	 * @return The height of the joint.
	 */
	public abstract double getHeight();

	/**
	 * Return the {@link DraggableBox} used as the root of this <code>GraphJointSkin</code>.
	 * 
	 * @return The draggable box containing the skin's root node.
	 */
	@Override
	public DraggableBox getRoot() {
		return root;
	}
}
