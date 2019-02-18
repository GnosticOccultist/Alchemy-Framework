package fr.alchemy.editor.core.ui.editor.graph.connections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.alchemy.editor.api.editor.graph.GraphConnectorStyle;
import fr.alchemy.editor.api.editor.graph.GraphConnectorValidator;
import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.event.GraphEventManager;
import fr.alchemy.editor.api.editor.graph.event.GraphInputGesture;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectionSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectorSkin;
import fr.alchemy.editor.core.ui.editor.graph.BaseGraphConnectorValidator;
import fr.alchemy.editor.core.ui.editor.graph.GraphNodeEditorView;
import fr.alchemy.editor.core.ui.editor.graph.GraphSkinManager;
import fr.alchemy.editor.core.ui.editor.graph.element.BaseGraphConnection;
import fr.alchemy.editor.core.ui.editor.graph.element.BaseGraphJoint;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

/**
 * <code>ConnectorDragManager</code> is responsible for what happens when {@link GraphConnector} 
 * are dragged in the {@link GraphNodeEditor}.
 * Mainly used for creation, removal and repositioning of connections.
 * 
 * @author GnosticOccultist
 */
public class ConnectorDragManager {
	
	/**
	 * The graph node editor view.
	 */
	private final GraphNodeEditorView view;
	/**
	 * The skin manager.
	 */
    private final GraphSkinManager skinManager;
    
    //private final ConnectionLayouter connectionLayouter;
    
    private final EventHandler<MouseEvent> mouseExitedHandler = this::handleMouseExited;
    
    private final EventHandler<MouseEvent> mousePressedHandler = Event::consume;
    		
    private final Map<Node, EventHandler<MouseEvent>> mouseEnteredHandlers = new HashMap<>();
    private final Map<Node, EventHandler<MouseEvent>> mouseReleasedHandlers = new HashMap<>();
    private final Map<Node, EventHandler<MouseEvent>> dragDetectedHandlers = new HashMap<>();
    private final Map<Node, EventHandler<MouseEvent>> mouseDraggedHandlers = new HashMap<>();
    private final Map<Node, EventHandler<MouseDragEvent>> mouseDragEnteredHandlers = new HashMap<>();
    private final Map<Node, EventHandler<MouseDragEvent>> mouseDragExitedHandlers = new HashMap<>();
    private final Map<Node, EventHandler<MouseDragEvent>> mouseDragReleasedHandlers = new HashMap<>();
    
    private final List<Node> managedConnectorSkins = new ArrayList<>();
    
    private GraphConnectorValidator validator = new BaseGraphConnectorValidator();
    
    private GraphConnector hoveredConnector;
    private GraphConnector sourceConnector;
    private GraphConnector targetConnector;
    private GraphConnector removalConnector;
	private TailManager tailManager;
	private boolean repositionAllowed;
    
    public ConnectorDragManager(final GraphSkinManager skinManager, final GraphNodeEditorView view) {
		this.view = view;
		this.skinManager = skinManager;
		
		this.tailManager = new TailManager(skinManager, view);
	}
    
    public void initialize() {
    	clearTrackingParameters();
    	
    	for (final GraphNode node : view.getNodes()) {
            for (final GraphConnector connector : node.getConnectors()) {
                addMouseHandlers(connector);
            }
        }
    }
    
    private void addMouseHandlers(final GraphConnector connector) {

        final GraphConnectorSkin connectorSkin = skinManager.retrieveConnector(connector);
        if (connectorSkin != null) {

            final Node root = skinManager.retrieveConnector(connector).getRoot();
            if(root == null || mouseEnteredHandlers.containsKey(root)) {
                return;
            }

            final EventHandler<MouseEvent> newMouseEnteredHandler = event -> handleMouseEntered(event, connector);
            final EventHandler<MouseEvent> newMouseReleasedHandler = event -> handleMouseReleased(event);

            final EventHandler<MouseEvent> newDragDetectedHandler = event -> handleDragDetected(event, connectorSkin);
            final EventHandler<MouseEvent> newMouseDraggedHandler = event -> handleMouseDragged(event, connector);
            final EventHandler<MouseDragEvent> newMouseDragEnteredHandler = event -> handleDragEntered(event, connectorSkin);
            final EventHandler<MouseDragEvent> newMouseDragExitedHandler = event -> handleDragExited(event, connectorSkin);
            final EventHandler<MouseDragEvent> newMouseDragReleasedHandler = event -> handleDragReleased(event, connectorSkin);

            root.addEventHandler(MouseEvent.MOUSE_ENTERED, newMouseEnteredHandler);
            root.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedHandler);
            root.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
            root.addEventHandler(MouseEvent.MOUSE_RELEASED, newMouseReleasedHandler);

            root.addEventHandler(MouseEvent.DRAG_DETECTED, newDragDetectedHandler);
            root.addEventHandler(MouseEvent.MOUSE_DRAGGED, newMouseDraggedHandler);
            root.addEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, newMouseDragEnteredHandler);
            root.addEventHandler(MouseDragEvent.MOUSE_DRAG_EXITED, newMouseDragExitedHandler);
            root.addEventHandler(MouseDragEvent.MOUSE_DRAG_RELEASED, newMouseDragReleasedHandler);

            managedConnectorSkins.add(root);

            mouseEnteredHandlers.put(root, newMouseEnteredHandler);
            mouseReleasedHandlers.put(root, newMouseReleasedHandler);

            dragDetectedHandlers.put(root, newDragDetectedHandler);
            mouseDraggedHandlers.put(root, newMouseDraggedHandler);
            mouseDragEnteredHandlers.put(root, newMouseDragEnteredHandler);
            mouseDragExitedHandlers.put(root, newMouseDragExitedHandler);
            mouseDragReleasedHandlers.put(root, newMouseDragReleasedHandler);
        }
    }
    
    private void handleMouseEntered(final MouseEvent event, final GraphConnector connector) {
    	hoveredConnector = connector;
    	event.consume();
    }
    
    private void handleMouseExited(final MouseEvent event) {
    	hoveredConnector = null;
    	event.consume();
    }
    
    private void handleMouseReleased(final MouseEvent event) {
    	final GraphConnectorSkin targetConnectorSkin;
    	if(targetConnector != null && (targetConnectorSkin = skinManager.retrieveConnector(targetConnector)) != null) {
    		targetConnectorSkin.applyStyle(GraphConnectorStyle.DEFAULT);
    	}
    	
    	sourceConnector = null;
    	removalConnector = null;
    	repositionAllowed = true;
    	
    	tailManager.cleanup();
    	finishGesture();
    	
    	event.consume();
    }
   
    private void handleDragDetected(final MouseEvent event, final GraphConnectorSkin connectorSkin) {
    	if(!event.getButton().equals(MouseButton.PRIMARY) || !canActivateGesture(event)) {
    		return;
    	}
    	
    	final GraphConnector connector = connectorSkin.getElement();
    	if(checkCreatable(connector)) {
    		sourceConnector = connector;
    		connectorSkin.getRoot().startFullDrag();
    		tailManager.cleanup();
    		tailManager.create(connector, event);
    		activateGesture();
    	} else if(checkRemovable(connector)) {
    		removalConnector = connector;
    		connectorSkin.getRoot().startFullDrag();
    		activateGesture();
    	}
    	
    	event.consume();
    }
    
    private boolean checkRemovable(GraphConnector connector) {
		return false;
	}

	private void handleMouseDragged(final MouseEvent event, final GraphConnector connector) {
    	if(!canActivateGesture(event)) {
    		return;
    	}
    	
    	if(repositionAllowed) {
    		
    		activateGesture();
    		
    		// Case for when the mouse first exists a connector during a drag gesture.
    		if(removalConnector != null && !removalConnector.equals(hoveredConnector)) {
    			detachConnection(event, connector);
    		} else {
    			tailManager.updatePosition(event);
    		}
    	}
    }
    
    private void handleDragEntered(final MouseEvent event, final GraphConnectorSkin connectorSkin) {
    	if(!canActivateGesture(event)) {
    		return;
    	}
    	
    	final GraphConnector connector =  connectorSkin.getElement();
    	if (validator.prevalidate(sourceConnector, connector)) {
    		
    		final boolean valid = validator.validate(sourceConnector, connector);
    		tailManager.snapPosition(sourceConnector, connector, valid);
    		repositionAllowed = false;
        	
        	if(valid) {
        		connectorSkin.applyStyle(GraphConnectorStyle.DRAG_OVER_ALLOWED);
        	} else {
        		connectorSkin.applyStyle(GraphConnectorStyle.DRAG_OVER_FORBIDDEN);
        	}
    	}
    	event.consume();
    }
    
    private void handleDragExited(final MouseEvent event, final GraphConnectorSkin connectorSkin) {

        connectorSkin.applyStyle(GraphConnectorStyle.DEFAULT);
        repositionAllowed = true;

        tailManager.updatePosition(event);

        event.consume();
    }
    
    private void handleDragReleased(final MouseEvent event, final GraphConnectorSkin connectorSkin) {
    	if(event.isConsumed()) {
    		return;
    	}
    	
    	// Consume the event so it doesn't fire repeadtly after re-initialization.
    	event.consume();
    	
    	final GraphConnector connector = connectorSkin.getElement();
    	if (validator.prevalidate(sourceConnector, connector) && validator.validate(sourceConnector, connector)) {
            addConnection(sourceConnector, connector);
        }

        connectorSkin.applyStyle(GraphConnectorStyle.DEFAULT);
        tailManager.cleanup();
        finishGesture();
    }

	private void addConnection(final GraphConnector source, final GraphConnector target) {
		
		final String connectionType = validator.createConnectionType(source, target);
		final String jointType = validator.createJointType(source, target);
		final List<Point2D> jointPositions = skinManager.retrieveTail(source).allocateJointPositions();
		
		final List<GraphJoint> joints = new ArrayList<>();
		
		for(final Point2D position : jointPositions) {
			
			final GraphJoint joint = new BaseGraphJoint();
			joint.setX(position.getX());
			joint.setY(position.getY());
			joint.setType(jointType);
			
			joints.add(joint);
		}
		
		final GraphConnection connection = new BaseGraphConnection();
		connection.setType(connectionType);
		connection.setSource(source);
		connection.setTarget(target);
		connection.getJoints().addAll(joints);
		
		skinManager.addConnection(connection);
		view.redrawConnection(skinManager.retrieveConnection(connection));
		
		GraphConnectionSkin connectionSkin = skinManager.retrieveConnection(connection);
		view.add(connectionSkin);
	}

	private void detachConnection(MouseEvent event, GraphConnector connector) {
		// TODO Auto-generated method stub
		
	}

	private boolean canActivateGesture(MouseEvent event) {
		final GraphEventManager manager = GraphEventManager.instance();
		return manager.canActivate(GraphInputGesture.CONNECT, event);
	}
	
	private void activateGesture() {
		GraphEventManager.instance().activateInputGesture(GraphInputGesture.CONNECT);
	}
	
	private void finishGesture() {
		GraphEventManager.instance().finishInputGesture(GraphInputGesture.CONNECT);
	}

	private boolean checkCreatable(final GraphConnector connector) {
		return connector != null && connector.getParent() instanceof GraphNode;
	}
	 
	private void clearTrackingParameters() {
        tailManager.cleanup();
        hoveredConnector = null;
        removalConnector = null;
        repositionAllowed = true;
    }
}
