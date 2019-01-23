package fr.alchemy.editor.core.ui.editor.graph;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.editor.core.ui.editor.graph.connections.ConnectorDragManager;
import fr.alchemy.editor.core.ui.editor.graph.element.BaseGraphConnector;
import fr.alchemy.editor.core.ui.editor.graph.element.BaseGraphNode;
import fr.alchemy.editor.core.ui.editor.graph.skin.SelectionManager;
import fr.alchemy.editor.core.ui.editor.graph.skin.SkinManager;

/**
 * <code>SimpleGraphNodeEditor</code> is a basic implementation of {@link GraphNodeEditor}.
 * It manages a {@link GraphNodeEditorView} representing the entire view of the graph editor.
 * 
 * @author GnosticOccultist
 */
public class SimpleGraphNodeEditor implements GraphNodeEditor {
	
	/**
	 * The graph node editor view.
	 */
	private final GraphNodeEditorView view;
	/**
	 * The skin manager of the graph elements.
	 */
	private final SkinManager skinManager;
	/**
	 * The selection maanager.
	 */
	private final SelectionManager selectionManager;
	/**
	 * The selection maanager.
	 */
	private final ConnectorDragManager connectorDragManager;
	
	
	public SimpleGraphNodeEditor() {
		
		this.skinManager = new SkinManager(this);
		this.view = new GraphNodeEditorView();
		this.selectionManager = new SelectionManager(skinManager, view);
		this.connectorDragManager = new ConnectorDragManager(skinManager, view);
		
		view.getStylesheets().add("titled_skins.css");
	}
	
	public void addNode(GraphNode node) {
		if(node != null) {
			skinManager.addNode(node);
			GraphNodeSkin skin = skinManager.lookupNode(node);
			
			skin.initialize();
			view.add(skin);
		}
	}
	
	public void newNode() {
		GraphNode node = new BaseGraphNode();
		
		final GraphConnector input = new BaseGraphConnector();
		node.getConnectors().add(input);
		input.setParent(node);
		input.setType("input");
		
		final GraphConnector output = new BaseGraphConnector();
		node.getConnectors().add(output);
		output.setParent(node);
		output.setType("output");
		
		addNode(node);
	}
	
	public void reload() {
		selectionManager.initialize();
		connectorDragManager.initialize();
	}
	
	@Override
	public void select(GraphConnector element) {
		selectionManager.select(element);
	}
	
	/**
	 * Return the {@link GraphNodeEditorView} of this <code>SimpleGraphNodeEditor</code>.
	 * 
	 * @return The view containing the entire graph editor.
	 */
	public GraphNodeEditorView getView() {
		return view;
	}
	
	@Override
	public SkinManager getSkinManager() {
		return skinManager;
	}

	@Override
	public boolean isSelected(GraphElement element) {
		return selectionManager.getSelectedElements().contains(element);
	}
}
