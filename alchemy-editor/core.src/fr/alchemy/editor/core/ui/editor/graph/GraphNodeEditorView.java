package fr.alchemy.editor.core.ui.editor.graph;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * <code>GraphEditorView</code> is an implementation of {@link Region} where all visual elements
 * from a {@link GraphNodeEditor} are added to.
 * <p>
 * The view currently has two layers - a <b>node</b> layer and a <b>connection</b> layer. The node layer
 * is in front. Graph nodes are added to the node layer, while connections, joints, and tails are added to
 * the connection layer. It means nodes will always be in front of connections.
 * </p><p>
 * Calling {@link #toFront()} or {@link #toBack()} on the associated JavaFX nodes will just reposition them inside
 * their layer. The layers always have the same dimensions as the editor region itself.
 * 
 * @author GnosticOccultist
 */
public class GraphNodeEditorView extends Region {
	
	public static final String STYLESHEET_VIEW = "view.css";

    public static final String STYLESHEET_DEFAULTS = "defaults.css";
	
	private static final String STYLE_CLASS = "graph-editor";
	
	private static final String STYLE_CLASS_NODE_LAYER = "graph-editor-node-layer";

    private static final String STYLE_CLASS_CONNECTION_LAYER = "graph-editor-connection-layer";
	
	/**
	 * The layer for graph nodes.
	 */
	private final Pane nodeLayer = new Pane();
	/**
	 * The layer for connections, joints or tails.
	 */
	private final Pane connectionLayer = new Pane();
	
	/**
	 * Instantiates a new <code>GraphNodeEditorView</code> to which skin instances can be 
	 * added and removed.
	 */
	public GraphNodeEditorView() {
		getStyleClass().addAll(STYLE_CLASS);
		
		setMaxWidth(Double.MAX_VALUE);
		setMaxHeight(Double.MAX_VALUE);
		
		initializeLayers();
	}
	
	/**
	 * Initializes the two layers that the view is composed of.
	 */
	private void initializeLayers() {
		nodeLayer.setPickOnBounds(false);
		connectionLayer.setPickOnBounds(false);
		
		nodeLayer.getStyleClass().add(STYLE_CLASS_NODE_LAYER);
		connectionLayer.getStyleClass().add(STYLE_CLASS_CONNECTION_LAYER);
		
		// Node layer should be on top of connection layer, so we add it second.
		getChildren().addAll(connectionLayer, nodeLayer);
	}
	
	public void add(final GraphNodeSkin nodeSkin) {
		if(nodeSkin != null) {
			nodeLayer.getChildren().add(nodeSkin.getRoot());
		}
	}
	
	
	@Override
	protected void layoutChildren() {
		final double width = getWidth();
		final double height = getHeight();
		
		nodeLayer.resizeRelocate(0, 0, width, height);
		connectionLayer.resizeRelocate(0, 0, width, height);
	}
	
	/**
	 * Clears all the elements from the <code>GraphNodeEditorView</code>.
	 */
	public void clear() {
		nodeLayer.getChildren().clear();
		connectionLayer.getChildren().clear();
	}
}
