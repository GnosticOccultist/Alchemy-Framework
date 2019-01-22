package fr.alchemy.editor.api.editor.graph.skin;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.region.DraggableBox;
import fr.alchemy.editor.api.editor.region.ResizableBox;

/**
 * <code>GraphNodeSkin</code> is an implementation of {@link GraphSkin} for {@link GraphNode}, which
 * is reponsible for visualizing nodes inside the {@link GraphNodeEditor}.
 * <p>
 * Any custom node skin must extend this class. It <b>must</b> also provides the super-implementation
 * constructor type in order to be applied for a specific <code>GraphNode</code>.
 * <p>
 * The node skin is responsible for adding its connectors to the scene-graph and laying them out using
 * a {@link ResizableBox} as its root node.
 * 
 * @author GnosticOccultist
 */
public abstract class GraphNodeSkin extends GraphSkin<GraphNode> {

	/**
	 * The draggable box used as the root of this skin.
	 */
	private final DraggableBox root;
	
	/**
	 * Instantiates a new <code>GraphNodeSkin</code> for the specified {@link GraphNode}.
	 * 
	 * @param node The graph node using the graph skin.
	 */
	public GraphNodeSkin(final GraphNode node) {
		super(node);
		this.root = createContainer();
	}
	
	/**
	 * Initializes the <code>GraphNodeSkin</code>, by loading the skin's layout values from the
	 * contained {@link GraphNode}.
	 */
	public void initialize() {
		getRoot().setLayoutX(getElement().getX());
		getRoot().setLayoutY(getElement().getY());
		
		getRoot().resize(getElement().getWidth(), getElement().getHeight());
	}
	
	/**
	 * Lays out the {@link GraphNode} connectors.
	 */
	public abstract void layoutConnectors();

	/**
	 * Creates a new {@link DraggableBox} which serves as the root for the <code>GraphNodeSkin</code>.
	 * By default a {@link ResizableBox} will be created and return as most nodes will be both 
	 * draggable and resizable.
	 * 
	 * @return A new draggable box instance, in this case a resizable box.
	 */
	protected DraggableBox createContainer() {
		return new ResizableBox() {
			
			@Override
			protected void layoutChildren() {
				super.layoutChildren();
				layoutConnectors();
			}
			
			@Override
			protected void positionMoved() {
				super.positionMoved();
				GraphNodeSkin.this.positionMoved();
			}
		};
	}
	
	@Override
	public DraggableBox getRoot() {
		return root;
	}
}
