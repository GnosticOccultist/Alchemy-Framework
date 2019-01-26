package fr.alchemy.editor.api.editor.graph.element;

import com.ss.rlib.common.util.array.Array;

/**
 * <code>GraphNode</code> is a {@link GraphElement} which represents a node that can have multiple {@link GraphConnector}
 * to be connected with other nodes.
 * 
 * @author GnosticOccultist
 */
public interface GraphNode extends GraphElement {
	
	/**
	 * Return the X-axis coordinate of this <code>GraphNode</code>.
	 * 
	 * @return The X coordinate of the node.
	 */
	double getX();
	
	/**
	 * Sets the Y-axis coordinate of this <code>GraphNode</code>.
	 * 
	 * @param x The Y coordinate of the node.
	 */
	void setX(double x);
	
	/**
	 * Return the Y-axis coordinate of this <code>GraphNode</code>.
	 * 
	 * @return The Y coordinate of the node.
	 */
	double getY();
	
	/**
	 * Sets the Y-axis coordinate of this <code>GraphNode</code>.
	 * 
	 * @param y The Y coordinate of the node.
	 */
	void setY(double y);
	
	/**
	 * Return the width of this <code>GraphNode</code>.
	 * 
	 * @return The width of the node.
	 */
	double getWidth();
	
	/**
	 * Sets the width of this <code>GraphNode</code>.
	 * 
	 * @param width The width of the node.
	 */
	void setWidth(double width);
	
	/**
	 * Return the height of this <code>GraphNode</code>.
	 * 
	 * @return The height of the node.
	 */
	double getHeight();
	
	/**
	 * Sets the height of this <code>GraphNode</code>.
	 * 
	 * @param height The height of the node.
	 */
	void setHeight(double height);
	
	/**
	 * Return the type of the <code>GraphNode</code>.
	 * 
	 * @return The type of the node.
	 */
	String getType();

	/**
	 * Sets the type of the <code>GraphNode</code>. It works with a set of keywords appended to a string, 
	 * i.e. 'titled' + 'node' means that this node is using the titled skin.
	 * 
	 * @param type The type of the node.
	 */
	void setType(String type);
	
	/**
	 * Return the array of {@link GraphConnector} contained in this <code>GraphNode</code>.
	 * 
	 * @return The array of connectors contained in the node.
	 */
	Array<GraphConnector> getConnectors();
}
