package fr.alchemy.editor.core.ui.editor.graph.selection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.GraphSkinDictionary;
import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectorSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphSkin;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.ContextMenuFillerRegistry;
import fr.alchemy.editor.core.ui.editor.graph.GraphNodeEditorView;
import fr.alchemy.utilities.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * <code>SelectionManager</code> manages the selected {@link GraphElement} inside the {@link GraphNodeEditor}, by updating the
 * selection state of their {@link GraphSkin}.
 * 
 * @author GnosticOccultist
 */
public class SelectionManager {
	
	/**
	 * The selected graph elements retained by the selection manager.
	 */
	private final ObservableSet<GraphElement> selectedElements = FXCollections.observableSet(new HashSet<>());
	/**
	 * The graph node editor.
	 */
	private final GraphNodeEditor editor;
	/**
	 * The graph editor's view.
	 */
	private GraphNodeEditorView view;
	/**
	 * The event handler to handle the graph editor's view pressing.
	 */
	private final EventHandler<MouseEvent> viewPressedHandler = this::handleViewPressed;
	/**
	 * The registered mouse pressed handlers.
	 */
    private final Map<Node, EventHandler<MouseEvent>> mousePressedHandlers = new HashMap<>();
    /**
	 * The registered mouse clicked handlers.
	 */
    private final Map<Node, EventHandler<MouseEvent>> mouseClickedHandlers = new HashMap<>();
	
    /**
     * Instantiates a new <code>SelectionManager</code> handling the specified {@link GraphNodeEditor}
     * and {@link GraphNodeEditorView}.
     * 
     * @param editor The graph node editor.
     * @param view	 The view of the graph node editor.
     */
	public SelectionManager(final GraphNodeEditor editor, final GraphNodeEditorView view) {
		Validator.nonNull(editor, "The graph node editor can't be null!");
		Validator.nonNull(view, "The graph node editor's view can't be null!");
		
		this.view = view;
		this.editor = editor;
		this.selectedElements.addListener(this::selectedElementsChanged);
		
		view.addEventHandler(MouseEvent.MOUSE_PRESSED, new WeakEventHandler<>(viewPressedHandler));
	}
	
	/**
	 * Initalizes the <code>SelectionManager</code> by adding {@link MouseEvent} handlers to all the 
	 * {@link GraphNode} already present in the {@link GraphNodeEditorView}.
	 */
	public void initialize() {
		selectedElements.clear();
		
		for (final GraphNode node : view.getNodes()) {
            prepareNode(node);
        }
	}

	/**
	 * Prepares the specified {@link GraphNode} to be selected by the <code>SelectionManager</code> by
	 * adding {@link MouseEvent} handler to it and its {@link GraphConnector}.
	 * 
	 * @param node The node and its connectors to prepare for selection.
	 */
	private void prepareNode(final GraphNode node) {
        final GraphNodeSkin skin = editor.getSkinDictionary().retrieveNode(node);
        if (skin != null) {
            final Region nodeRegion = skin.getRoot();

            if(!mousePressedHandlers.containsKey(nodeRegion)) {
                final EventHandler<MouseEvent> newNodePressedHandler = event -> handleNodePressed(event, skin);
                nodeRegion.addEventHandler(MouseEvent.MOUSE_PRESSED, newNodePressedHandler);
                mousePressedHandlers.put(nodeRegion, newNodePressedHandler);
            }

            for (final GraphConnector connector : node.getConnectors()) {
                prepareConnector(connector);
            }
        }
	}

	/**
	 * Prepares the specified {@link GraphConnector} to be selected by the <code>SelectionManager</code> by
	 * adding {@link MouseEvent} handler to it.
	 * 
	 * @param node The connector to prepare for selection.
	 */
	private void prepareConnector(GraphConnector connector) {
        final GraphConnectorSkin connectorSkin = editor.getSkinDictionary().retrieveConnector(connector);
        if (connectorSkin != null) {
            final Node connectorRoot = connectorSkin.getRoot();

            if (!mouseClickedHandlers.containsKey(connectorRoot)) {
                final EventHandler<MouseEvent> connectorClickedHandler = event -> handleSelectionClick(event, connectorSkin);
                connectorRoot.addEventHandler(MouseEvent.MOUSE_CLICKED, connectorClickedHandler);
                mouseClickedHandlers.put(connectorRoot, connectorClickedHandler);
            }
        }
	}
    
	/**
	 * Handle the specified selection press {@link MouseEvent} which occured on the given {@link GraphNodeSkin}.
	 * 
	 * @param event	   The selection press event.
	 * @param nodeSkin The node skin which received the press event.
	 */
    private void handleNodePressed(final MouseEvent event, final GraphNodeSkin nodeSkin) {

    	if (event.getButton() == MouseButton.SECONDARY) {
    		ContextMenu menu = new ContextMenu();
    		ContextMenuFillerRegistry.filler().fill(nodeSkin, menu.getItems());
			
    		// TODO: We should keep track of the context menus to actually hide them at some points.
			menu.show(nodeSkin.getRoot(), Side.BOTTOM, 0, 0);	
		}

        // First update the selection:
        handleSelectionClick(event, nodeSkin);

        // Consume this event so it's not passed up to the parent (i.e. the view).
        event.consume();
    }
    
	/**
	 * Handle the specified selection click {@link MouseEvent} which occured on the given {@link GraphSkin}.
	 * 
	 * @param event The selection click event.
	 * @param skin  The graph skin which received the click event.
	 */
    private void handleSelectionClick(final MouseEvent event, final GraphSkin<?> skin) {

        if (!MouseButton.PRIMARY.equals(event.getButton()) && !MouseButton.SECONDARY.equals(event.getButton())) {
            return;
        }
        
        if (!skin.isSelected()) {
        	// If CTRL is down we clear all selected elements before selecting it.
            if (!event.isShortcutDown()) {
            	clearSelection();
            }	
            
            select(skin.getElement());
            
        } else {
        	// If CTRL is down we clear this skin from selection.
            if (event.isShortcutDown()) {
            	removeFromSelection(skin.getElement());
            }
        }

        // Consume this event so it's not passed up to the parent (i.e. the view).
        event.consume();
    }
    
    /**
     * Handle the specified pressed {@link MouseEvent} which occured on the {@link GraphNodeEditor}'s view.
     * 
     * @param event The pressed mouse event.
     */
    private void handleViewPressed(final MouseEvent event) {
    	if(editor.getView() == null || event.isConsumed()) {
    		return;
    	}
    	
    	if(!event.isShortcutDown()) {
    		clearSelection();
    	}
    }
	
    /**
     * Selects the specified {@link GraphElement}. It will update the selection state of the 
     * element's skin by calling {@link GraphSkin#updateSelection()} in order to change the style
     * to the selected one.
     * 
     * @param element The graph element to select.
     */
	public void select(final GraphElement element) {
		getSelectedElements().add(element);
	}
	
	/**
	 * Called when the {@link #selectedElements} set has changed. It will notify the skin of the removed
	 * or added {@link GraphElement} of its selection state in order to alter the style of the skin.
	 * 
	 * @param change The change listener to notify the set has changed.
	 */
    private void selectedElementsChanged(final SetChangeListener.Change<? extends GraphElement> change) {
        if (change.wasRemoved()) {
            update(change.getElementRemoved());
        }
        if (change.wasAdded()) {
            update(change.getElementAdded());
        }
    }
    
    /**
     * Update the selection state of the {@link GraphElement}'s skin in order to alter the style of the skin.
     * 
     * @param element The graph element to update.
     */
    private void update(final GraphElement element) {

        GraphSkin<?> skin = null;
        GraphSkinDictionary dictionary = editor.getSkinDictionary();
        
        if (element instanceof GraphNode) {
            skin = dictionary.retrieveNode((GraphNode) element);
        } else if (element instanceof GraphJoint) {
            skin = dictionary.retrieveJoint((GraphJoint) element);
        } else if (element instanceof GraphConnection) {
            skin = dictionary.retrieveConnection((GraphConnection) element);
        } else if (element instanceof GraphConnector) {
            skin = dictionary.retrieveConnector((GraphConnector) element);
        }

        if (skin != null) {
            skin.updateSelection();
        }
    }
    
    /**
     * Remove the specified {@link GraphElement} from selection.
     * 
     * @param element The element to remove from selection.
     */
    public void removeFromSelection(final GraphElement element) {
    	getSelectedElements().remove(element);
    }
    
    /**
     * Clear all {@link GraphElement} selected by the <code>SelectionManager</code>.
     */
    public void clearSelection() {
    	if(!getSelectedElements().isEmpty()) {
    		// Copy the selected elements to an array to prevent concurrent modifications.
    		final GraphElement[] selectedElements = getSelectedElements().toArray(new GraphElement[getSelectedElements().size()]);
	    	for(final GraphElement toRemove : selectedElements) {
	    		getSelectedElements().remove(toRemove);
	    	}
    	}
    }
    
    /**
     * Return the list of selected {@link GraphNode}. It will filter all the selected {@link GraphElement}
     * to return only the nodes.
     * 
     * @return The list of selected graph nodes.
     */
    public List<GraphNode> getSelectedNodes() {
        return selectedElements.stream().filter(e -> e instanceof GraphNode)
        		.map(e -> (GraphNode) e)
                .collect(Collectors.toList());
    }
    
    /**
     * Return the list of selected {@link GraphConnection}. It will filter all the selected {@link GraphElement}
     * to return only the connections.
     * 
     * @return The list of selected graph connections.
     */
    public List<GraphConnection> getSelectedConnections() {
        return selectedElements.stream().filter(e -> e instanceof GraphConnection)
        		.map(e -> (GraphConnection) e)
                .collect(Collectors.toList());
    }
    
    /**
     * Return the set of selected {@link GraphElement}.
     * 
     * @return The set of selected elements.
     */
    public ObservableSet<GraphElement> getSelectedElements() {
        return selectedElements;
    }
}
