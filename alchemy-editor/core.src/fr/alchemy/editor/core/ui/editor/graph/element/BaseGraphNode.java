package fr.alchemy.editor.core.ui.editor.graph.element;

import java.util.ArrayList;
import java.util.List;

import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;

public class BaseGraphNode implements GraphNode {
	
	/**
	 * The connectors of the graph node.
	 */
	protected List<GraphConnector> connectors;
	
	/**
	 * Return the list of {@link GraphConnector} for this <code>BaseGraphNode</code> or
	 * instantiates a new one if none.
	 * 
	 * @return The list of connectors for the graph node.
	 */
	public List<GraphConnector> getConnectors() {
		if(connectors == null) {
			connectors = new ArrayList<GraphConnector>();
		}
		return connectors;
	}
}
