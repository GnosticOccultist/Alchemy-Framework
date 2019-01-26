package fr.alchemy.editor.core.ui.editor.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.GraphSkinDictionary;
import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectionSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectorSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphJointSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphTailSkin;

/**
 * <code>GraphSkinManager</code> is an implementation of {@link GraphSkinDictionary} which handles the creation of {@link GraphSkin}
 * to render a {@link GraphElement} in the {@link GraphNodeEditor}'s view.
 * It caches all previously generated skin in order to be accessed by providing their respective element.
 * 
 * @author GnosticOccultist
 */
public final class GraphSkinManager implements GraphSkinDictionary {
	
    /**
     * The table with the cached node skin for each graph node.
     */
	private final Map<GraphNode, GraphNodeSkin> nodeSkins = new HashMap<GraphNode, GraphNodeSkin>();
	/**
     * The table with the cached connector skin for each graph connector.
     */
	private final Map<GraphConnector, GraphConnectorSkin> connectorSkins = new HashMap<GraphConnector, GraphConnectorSkin>();
	/**
     * The table with the cached connections skin for each graph connections.
     */
    private final Map<GraphConnection, GraphConnectionSkin> connectionSkins = new HashMap<GraphConnection, GraphConnectionSkin>();
    /**
     * The table with the cached joint skin for each graph joint.
     */
    private final Map<GraphJoint, GraphJointSkin> jointSkins = new HashMap<GraphJoint, GraphJointSkin>();
    /**
     * The table with the cached tail skin for each graph connector.
     */
    private final Map<GraphConnector, GraphTailSkin> tailSkins = new HashMap<GraphConnector, GraphTailSkin>();
    
    /**
     * The callback method when a graph skin has moved in the editor view.
     */
	public final Consumer<GraphSkin<?>> onPositionMoved = this::positionMoved;
	/**
	 * The graph node editor.
	 */
	private final GraphNodeEditor editor;

	/**
	 * Instantiates a new <code>GraphSkinManager</code> for the specified {@link GraphNodeEditor}.
	 * 
	 * @param editor The editor for which to create the skin manager.
	 */
	public GraphSkinManager(final GraphNodeEditor editor) {
		this.editor = editor;
	}
	
	/**
	 * Initializes the {@link GraphNodeSkin} and {@link GraphNodeSkin} which were created by the 
	 * <code>GraphSkinManager</code>, by calling the <code>initialize()</code> method.
	 */
    public void initialize() {
        nodeSkins.values().forEach(GraphNodeSkin::initialize);
        jointSkins.values().forEach(GraphJointSkin::initialize);
    }
	
    /**
     * Creates the skin of the given {@link GraphNode} and its {@link GraphConnector} if none were already cached.
     * 
     * @param node The graph node to which create a skin.
     */
	public void addNode(GraphNode node) {
		nodeSkins.computeIfAbsent(node, this::createNodeSkin);
		addConnectors(node);
	}
	
	/**
	 * Creates the skin for the {@link GraphConnector} present in the specified {@link GraphNode}. The skin are
	 * seperated between {@link GraphConnectorSkin} and {@link GraphTailSkin} based on their state.
	 * The new instantiated skin are then passed to the node to be attached to its root node.
	 * 
	 * @param node The graph node to get the connectors from.
	 */
    private void addConnectors(final GraphNode node) {
        final List<GraphConnectorSkin> nodeConnectorSkins = new ArrayList<>();

        for (final GraphConnector connector : node.getConnectors()) {
            nodeConnectorSkins.add(connectorSkins.computeIfAbsent(connector, this::createConnectorSkin));
            tailSkins.computeIfAbsent(connector, this::createTailSkin);
        }
        
        nodeSkins.get(node).setConnectorSkins(nodeConnectorSkins);
    }
	
    /**
     * Creates the skin of the given {@link GraphConnection} and its {@link GraphJoints} if none were already cached.
     * 
     * @param connection The graph connectio to which create a skin.
     */
	public void addConnection(GraphConnection connection) {
		GraphConnectionSkin connectionSkin = connectionSkins.computeIfAbsent(connection, this::createConnectionSkin);
		final List<GraphJointSkin> connectionJointSkins = connection.getJoints().stream()
				.map(joint -> jointSkins.computeIfAbsent(joint, this::createJointSkin)).collect(Collectors.toList());
		connectionSkin.setJointSkins(connectionJointSkins);
	}
	
	@Override
	public GraphNodeSkin retrieveNode(final GraphNode node) {
		return nodeSkins.get(node);
	}
	
	@Override
	public GraphConnectorSkin retrieveConnector(final GraphConnector connector) {
		return connectorSkins.get(connector);
	}
	
	@Override
	public GraphConnectionSkin retrieveConnection(final GraphConnection connection) {
        return connectionSkins.get(connection);
    }
	
	@Override
	public GraphJointSkin retrieveJoint(final GraphJoint joint) {
		return jointSkins.get(joint);
	}
	
	@Override
	public GraphTailSkin retrieveTail(final GraphConnector connector) {
		return tailSkins.get(connector);
	}

    private GraphConnectorSkin createConnectorSkin(final GraphConnector connector) {
    	GraphConnectorSkin skin = GraphSkinRegistry.skin().newSkin("titled.connector", connector);
        
        skin.setGraphEditor(editor);
        return skin;
    }
    
    private GraphJointSkin createJointSkin(final GraphJoint joint) {
    	GraphJointSkin skin = GraphSkinRegistry.skin().newSkin("titled.joint", joint);
        
        skin.setGraphEditor(editor);
        skin.setOnPositionMoved(onPositionMoved);
        skin.initialize();
        return skin;
    }
    
    private GraphConnectionSkin createConnectionSkin(final GraphConnection connection) {
    	GraphConnectionSkin skin = GraphSkinRegistry.skin().newSkin("titled.connection", connection);
        
        skin.setGraphEditor(editor);
        return skin;
    }
	
    private GraphNodeSkin createNodeSkin(final GraphNode node) {
    	GraphNodeSkin skin = GraphSkinRegistry.skin().newSkin("titled.node", node);
        
        skin.setGraphEditor(editor);
        skin.setOnPositionMoved(onPositionMoved);
        skin.initialize();
        return skin;
    }
    
    private GraphTailSkin createTailSkin(final GraphConnector connector) {
    	GraphTailSkin skin = GraphSkinRegistry.skin().newSkin("titled.tail", connector);
        
        skin.setGraphEditor(editor);
        return skin;
    }
    
    /**
     * Called when a specified {@link GraphSkin} has moved in the {@link GraphNodeEditor}'s view.
     * To register the callback you need to call the {@link GraphSkin#setOnPositionMoved(Consumer)} method.
     * <p>
     * This method can be invoked with the accessible consumer {@link #onPositionMoved}.
     * 
     * @param movedSkin The moved graph skin.
     */
    private void positionMoved(final GraphSkin<?> movedSkin) {
    	
    	if(movedSkin instanceof GraphNodeSkin) {
    		// Redraw all connections attached to each connector of the graph node.
    		for(final GraphConnector connector : ((GraphNodeSkin) movedSkin).getElement().getConnectors()) {
    			for(final GraphConnection connection : connector.getConnections()) {
    				GraphConnectionSkin skin = retrieveConnection(connection);
    				editor.redraw(skin);
    			}
    		}
    	} else if(movedSkin instanceof GraphJointSkin) {
    		// Redraw the graph connection of the graph joint.
    		GraphConnection connection = ((GraphJointSkin) movedSkin).getElement().getConnection();
    		GraphConnectionSkin skin = retrieveConnection(connection);
    		editor.redraw(skin);
    	}
    }
}
