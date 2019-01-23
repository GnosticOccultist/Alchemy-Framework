package fr.alchemy.editor.api.editor.graph.skin;

import java.util.List;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.region.DraggableBox;
import fr.alchemy.editor.api.editor.region.ResizableBox;
import javafx.geometry.Point2D;

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
	 * Sets the {@link GraphNode}'s {@link GraphConnectorSkin}.
	 * <p>
	 * This will be called as the node is created, or if a connector is added or removed.
	 * The connector skin's should be added to the scene-graph.
	 * 
	 * @param connectorSkins The list of connector's skins for each of the node's connector.
	 */
	public abstract void setConnectorSkins(List<GraphConnectorSkin> connectorSkins);

	/**
	 * Return the position of the <b>center</b> of a {@link GraphConnector} relative to the <code>GraphNode</code>r
	 * region, the point being where a connection will attach to.
	 * 
	 * @param connectorSkin The skin of the connector to retrieve the position.
	 * @return				The X and Y coordinates of the connector's center.
	 */
	public abstract Point2D getConnectorPosition(GraphConnectorSkin connectorSkin);
	 
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
