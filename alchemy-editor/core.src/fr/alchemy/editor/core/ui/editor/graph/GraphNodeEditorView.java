package fr.alchemy.editor.core.ui.editor.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ss.rlib.common.util.dictionary.ObjectDictionary;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectionSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphTailSkin;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
	
	private static final String STYLE_CLASS = "graph-editor";
	
	private static final String STYLE_CLASS_NODE_LAYER = "graph-editor-node-layer";

    private static final String STYLE_CLASS_CONNECTION_LAYER = "graph-editor-connection-layer";
	
    
    private final ObjectDictionary<Integer, GraphNode> nodes = ObjectDictionary.ofType(Integer.class, GraphNode.class);
    
    private final List<GraphConnection> connections = new ArrayList<>();
	/**
	 * The layer for graph nodes.
	 */
	private final Pane nodeLayer = new Pane();
	/**
	 * The layer for connections, joints or tails.
	 */
	private final Pane connectionLayer = new Pane();
	
	private final GraphNodeEditor editor;
	
	private GraphNode root;
	
	private final ContextMenu menu = new ContextMenu();
	
	private final Map<GraphConnectionSkin, Point2D[]> connectionPoints = new HashMap<>();
	
	/**
	 * Instantiates a new <code>GraphNodeEditorView</code> to which skin instances can be 
	 * added and removed.
	 */
	public GraphNodeEditorView(GraphNodeEditor editor) {
		this.editor = editor;
		
		setFocusTraversable(true);
		
		getStyleClass().addAll(STYLE_CLASS);
		
		setMaxWidth(Double.MAX_VALUE);
		setMaxHeight(Double.MAX_VALUE);
		
		addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMouseClickEvent);
		
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
	
	private void handleMouseClickEvent(final MouseEvent event) {
		editor.save();
		
		if (event.getButton() == MouseButton.SECONDARY && !menu.isShowing() && !event.isConsumed()) {
			menu.show(this, event.getSceneX(), event.getSceneY());
			return;
		}
		
		menu.hide();
	}
	
	public void add(final GraphNodeSkin nodeSkin) {
		if(nodeSkin != null) {
			nodeLayer.getChildren().add(nodeSkin.getRoot());
			if(nodes.isEmpty()) {
				root = nodeSkin.getElement();
			}
			nodes.put(nodeSkin.getElement().id(), nodeSkin.getElement());
		}
	}
	
	public void remove(final GraphNodeSkin nodeSkin) {
		if(nodeSkin != null) {
			nodeLayer.getChildren().remove(nodeSkin.getRoot());
			if(nodeSkin.getElement().equals(root)) {
				throw new IllegalStateException();
			}
			nodes.remove(nodeSkin.getElement().id());
		}
	}
	
	public void add(final GraphConnectionSkin connectionSkin) {
		if(connectionSkin != null) {
			connectionLayer.getChildren().add(0, connectionSkin.getRoot());
			connections.add(connectionSkin.getElement());
		}
	}
	
	public void add(final GraphTailSkin tailSkin) {
		if(tailSkin != null) {
			connectionLayer.getChildren().add(tailSkin.getRoot());
		}
	}
	
	public void remove(final GraphTailSkin tailSkin) {
		if(tailSkin != null) {
			connectionLayer.getChildren().remove(tailSkin.getRoot());
		}
	}
	
	public void redrawConnection(final GraphConnectionSkin connectionSkin) {
		if(connectionPoints.isEmpty()) {
			// We need all points of all connection first.
			redrawAll();
		}
		
		try {
			redrawSingleConnection(connectionSkin);
		} catch (Exception ex) {
			System.err.println("Couldn't redraw dirty connection skin: " + connectionSkin + ". " + ex);
		}
	}

	private void redrawSingleConnection(GraphConnectionSkin connectionSkin) {
	
        if (connectionSkin != null) {

            final Point2D[] points = connectionSkin.update();
            if (points != null) {
                connectionPoints.put(connectionSkin, points);
            }

            connectionSkin.draw(connectionPoints);
        }
	}

	private void redrawAll() {
		connectionPoints.clear();
		
		for(final GraphConnection connection : getConnections()) {
			final GraphConnectionSkin connectionSkin = editor.getSkinDictionary().retrieveConnection(connection);
			if(connectionSkin != null) {
				final Point2D[] points = connectionSkin.update();
				if(points != null) {
					connectionPoints.put(connectionSkin, points);
				}
			}
		}
		
		for(final GraphConnectionSkin skin : connectionPoints.keySet()) {
			skin.draw(connectionPoints);
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
	
	public GraphNode getRootNode() {
		return root;
	}
	
	public ObjectDictionary<Integer, GraphNode> getNodes() {
		return nodes;
	}
	
	public List<GraphConnection> getConnections() {
		return connections;
	}
}
