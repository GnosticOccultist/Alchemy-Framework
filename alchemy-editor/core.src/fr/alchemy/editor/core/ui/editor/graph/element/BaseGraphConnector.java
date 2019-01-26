package fr.alchemy.editor.core.ui.editor.graph.element;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;

/**
 * <code>BaseGraphConnector</code> is the most basic implementation of {@link GraphConnector}.
 * 
 * @author GnosticOccultist
 */
public class BaseGraphConnector implements GraphConnector {
	
	/**
	 * The parent node of the connector.
	 */
	protected GraphNode parent;
	/**
	 * The type of the connector.
	 */
	protected String type;
	/**
	 * The array of connections connected to the connector.
	 */
	protected final Array<GraphConnection> connections = Array.ofType(GraphConnection.class);
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String value) {
		this.type = value;
	}

	@Override
	public GraphNode getParent() {
		return parent;
	}
	
	@Override
	public void setParent(GraphNode parent) {
		this.parent = parent;
	}

	@Override
	public Array<GraphConnection> getConnections() {
		return connections;
	}
}
