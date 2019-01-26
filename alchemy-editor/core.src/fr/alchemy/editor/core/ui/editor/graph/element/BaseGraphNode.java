package fr.alchemy.editor.core.ui.editor.graph.element;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;

/**
 * <code>BaseGraphNode</code> is the most basic implementation of {@link GraphNode}.
 * 
 * @author GnosticOccultist
 */
public class BaseGraphNode implements GraphNode {
	
	/**
	 * The X-axis coordinate of the node.
	 */
	protected double x;
	/**
	 * The Y-axis coordinate of the node.
	 */
	protected double y;
	/**
	 * The width of the node.
	 */
	protected double width = 151.0;
	/**
	 * The height of the node.
	 */
	protected double height = 101.0;
	/**
	 * The type of the node.
	 */
	protected String type;
	/**
	 * The array of connectors contained in the graph node.
	 */
	protected final Array<GraphConnector> connectors = Array.ofType(GraphConnector.class);

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public Array<GraphConnector> getConnectors() {
		return connectors;
	}
}
