package fr.alchemy.editor.core.ui.editor.graph;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.BaseFileEditor;
import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectionSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphSkin;
import fr.alchemy.editor.core.export.XMLExporter;
import fr.alchemy.editor.core.ui.editor.graph.connections.ConnectorDragManager;
import fr.alchemy.editor.core.ui.editor.graph.element.BaseGraphConnector;
import fr.alchemy.editor.core.ui.editor.graph.element.BaseGraphNode;
import fr.alchemy.editor.core.ui.editor.graph.selection.SelectionManager;

/**
 * <code>SimpleGraphNodeEditor</code> is a basic implementation of {@link GraphNodeEditor}.
 * It manages a {@link GraphNodeEditorView} representing the entire view of the graph editor.
 * 
 * @author GnosticOccultist
 */
public class SimpleGraphNodeEditor extends BaseFileEditor<GraphNodeEditorView> implements GraphNodeEditor {
	
	private final AtomicInteger idGenerator = new AtomicInteger(0);
	/**
	 * The skin manager of the graph elements.
	 */
	private final GraphSkinManager skinManager;
	/**
	 * The selection maanager.
	 */
	private final SelectionManager selectionManager;
	/**
	 * The selection maanager.
	 */
	private final ConnectorDragManager connectorDragManager;
	
	public SimpleGraphNodeEditor() {
		
		this.skinManager = new GraphSkinManager(this);
		this.selectionManager = new SelectionManager(this, root);
		this.connectorDragManager = new ConnectorDragManager(skinManager, root);
		
		root.getStylesheets().clear();
		root.getStylesheets().add("titled_skins.css");

		construct(root);
	}
	
	public void addNode(GraphNode node) {
		if(node != null) {
			skinManager.addNode(node);
			GraphNodeSkin skin = skinManager.retrieveNode(node);
			
			skin.initialize();
			root.add(skin);
		}
	}
	
	public void newNode() {
		GraphNode node = new BaseGraphNode(acquireNextID());
		
		final GraphConnector input = new BaseGraphConnector();
		node.getConnectors().add(input);
		input.setParent(node);
		input.setType("input_left");
		
		final GraphConnector output = new BaseGraphConnector();
		node.getConnectors().add(output);
		output.setParent(node);
		output.setType("output_right");
		
		addNode(node);
	}
	
	public void reload() {
		selectionManager.initialize();
		connectorDragManager.initialize();
	}
	
	@Override
	public void select(GraphElement element) {
		selectionManager.select(element);
	}
	
	/**
	 * Return the {@link GraphNodeEditorView} of this <code>SimpleGraphNodeEditor</code>.
	 * 
	 * @return The view containing the entire graph editor.
	 */
	public GraphNodeEditorView getView() {
		return root;
	}
	
	@Override
	public GraphSkinManager getSkinDictionary() {
		return skinManager;
	}

	@Override
	public boolean isSelected(GraphElement element) {
		return selectionManager.getSelectedElements().contains(element);
	}

	@Override
	public void redraw(GraphSkin skin) {
		if(skin instanceof GraphConnectionSkin) {
			root.redrawConnection((GraphConnectionSkin) skin);
		}
	}

	@Override
	public void remove(GraphSkin skin) {
		if(skin instanceof GraphNodeSkin) {
			root.remove((GraphNodeSkin) skin);
		}	
	}
	
	@Override
	public boolean save() {
		System.out.println("saving");
		try(OutputStream stream = Files.newOutputStream(Paths.get(System.getProperty("user.dir") + "/graph-node-test.xml"))) {
			XMLExporter exporter = new XMLExporter(this, root);
			exporter.save(stream);
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public Array<String> getSupportedExtensions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GraphNodeEditorView createRoot() {
		return new GraphNodeEditorView(this);
	}

	@Override
	public int acquireNextID() {
		return idGenerator.getAndIncrement();
	}

	@Override
	public GraphNode getRootNode() {
		return root.getRootNode();
	}
}
