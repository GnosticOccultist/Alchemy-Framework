package fr.alchemy.editor.api.editor.graph.skin;

import fr.alchemy.editor.api.editor.graph.GraphConnectorStyle;
import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;

/**
 * <code>GraphConnectorSkin</code> is an implementation of {@link GraphSkin} for {@link GraphConnector}, which
 * is reponsible for visualizing connectors inside the {@link GraphNodeEditor}.
 * <p>
 * Any custom connector skin must extend this class. It <b>must</b> also provides the super-implementation
 * constructor type in order to be applied for a specific <code>GraphConnector</code>.
 * <p>
 * The root node must be created by the skin implementation and returned by the {@link #getRoot()} method.
 * 
 * @author GnosticOccultist
 */
public abstract class GraphConnectorSkin extends GraphSkin<GraphConnector> {

	/**
	 * Instantiates a new <code>GraphConnectorSkin</code> for the specified {@link GraphConnector}.
	 * 
	 * @param node The graph connector using the graph skin.
	 */
	public GraphConnectorSkin(final GraphConnector connector) {
		super(connector);
	}
	
	/**
	 * Return the width of the <code>GraphConnectorSkin</code>.
	 * 
	 * @return The width of the connector skin.
	 */
	public abstract double getWidth();
	
	/**
	 * Return the height of the <code>GraphConnectorSkin</code>.
	 * 
	 * @return The height of the connector skin.
	 */
	public abstract double getHeight();
	
	/**
	 * Applies the specified {@link GraphConnectorStyle} to the <code>GraphConnectorSkin</code>.
	 * <p>
	 * This is called by the library during various mouse events. For example when a connector
	 * is dragged over another connector in an attempt to create a new connection.
	 */
	public abstract void applyStyle(GraphConnectorStyle style);
}
