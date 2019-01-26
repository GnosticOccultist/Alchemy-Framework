package fr.alchemy.editor.api.editor.graph.element;

import com.ss.rlib.common.util.array.Array;

/**
 * <code>GraphConnector</code> is a {@link GraphElement} which represents a connector of a {@link GraphNode}.
 * Each connector can be attached to one or multiple {@link GraphConnection}.
 * 
 * @author GnosticOccultist
 */
public interface GraphConnector extends GraphElement {

	/**
	 * Return the parent {@link GraphNode} of this <code>GraphConnector</code>.
	 * 
	 * @return The parent node of the connector.
	 */
	GraphNode getParent();

	/**
	 * Sets the parent {@link GraphNode} of this <code>GraphConnector</code>.
	 * 
	 * @param node The parent node of the connector.
	 */
	void setParent(GraphNode node);
	
	/**
	 * Return the type of the <code>GraphConnector</code>.
	 * 
	 * @return The type of the connector.
	 */
	String getType();

	/**
	 * Sets the type of the <code>GraphConnectors</code>. It works with a set of keywords appended to a string, 
	 * i.e. 'input' + 'titled' + 'connector' means that this connector is using the titled skin but it's also an input.
	 * 
	 * @param type The type of the connector.
	 */
	void setType(String type);
	
	/**
	 * Return the array of {@link GraphConnection} attached to this <code>GraphConnector</code>.
	 * 
	 * @return The array of connections attached to this connector.
	 */
	Array<GraphConnection> getConnections();
}
