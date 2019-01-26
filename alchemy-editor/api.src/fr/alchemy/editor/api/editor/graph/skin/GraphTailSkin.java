package fr.alchemy.editor.api.editor.graph.skin;

import java.util.List;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import javafx.geometry.Point2D;

/**
 * <code>GraphTailSkin</code> is an implementation of {@link GraphSkin} for {@link GraphConnector}, which
 * is reponsible for visualizing the tails that extend temporarily from the connectors during a drag-gesture
 * in the {@link GraphNodeEditor}.
 * <p>
 * Any custom tail skin must extend this class. It <b>must</b> also provides the super-implementation
 * constructor type in order to be applied for a specific <code>GraphConnector</code>.
 * <p>
 * The tail skins can have similar logic to {@link GraphConnectionSkin}, but they do not have to worry 
 * about positionable joints.
 * 
 * @author GnosticOccultist
 */
public abstract class GraphTailSkin extends GraphSkin<GraphConnector> {

	/**
	 * Instantiates a new <code>GraphTailSkin</code> for the specified {@link GraphConnector}.
	 * 
	 * @param node The graph connector that the tail will extend from.
	 */
	public GraphTailSkin(GraphConnector connector) {
		super(connector);
	}
	
	/**
	 * Updates the position of the <code>GraphTailSkin</code> according to the specified start points and end points.
	 * <p>
	 * This method will be called when a 'fresh' tail is created from an unoccupied {@link GraphConnector}.
	 * 
	 * @param start	The starting X and Y coordinates.
	 * @param end	The ending X and Y coordinates.
	 */
	public abstract void draw(Point2D start, Point2D end);
	
	/**
	 * Updates the position of the <code>GraphTailSkin</code> according to the specified start points and end points.
	 * <p>
	 * This method will be called when a tail is snapped to the target {@link GraphConnector} that the mouse is hovering over. 
	 * 
	 * @param start	 The starting X and Y coordinates.
	 * @param end	 The ending X and Y coordinates.
	 * @param target The target connector that the tail is snapping to.
	 * @param valid	 Whether the connection is valid.
	 */
	public abstract void draw(Point2D start, Point2D end, GraphConnector target, boolean valid);
	
	/**
	 * Updates the position of the <code>GraphTailSkin</code> according to the specified start points, end points, and joint positions.
	 * <p>
	 * This method will be called when an existing connection is repositioned. The tail skin may use the position of the
	 * old connection to decide how to position itself, or it may ignore this information.
	 * 
	 * @param start			 The starting X and Y coordinates.
	 * @param end			 The ending X and Y coordinates.
	 * @param jointPositions The list of point objects containing X and Y coordinates for a newly-created connection.
	 */
	public abstract void draw(Point2D start, Point2D end, List<Point2D> jointPositions);
	
	/**
	 * Updates the position of the <code>GraphTailSkin</code> according to the specified start points, end points and joint positions.
	 * <p>
	 * This method will be called when an existing connection is repositioned and the tail is snapped to a target {@link GraphConnector}.
	 * 
	 * @param start			 The starting X and Y coordinates.
	 * @param end			 The ending X and Y coordinates.
	 * @param jointPositions The list of point objects containing X and Y coordinates for a newly-created connection.
	 * @param target		 The target connector that the tail is snapping to.
	 * @param valid			 Whether the connection is valid.
	 */
	public abstract void draw(Point2D start, Point2D end, List<Point2D> jointPositions, GraphConnector target, boolean valid);
	
	/**
	 * Allocates a list of joint positions for a new connection.
	 * <p>
	 * When the <code>GraphTailSkin</code> is 'converted' into a connection during a successful drag-drop gesture, this method will be called
	 * so that the new connection's joint positions can be based on the final position of the tail.
	 * 
	 * @return The list of point objects containing X and Y coordinates for a newly-created connection.
	 */
	public abstract List<Point2D> allocateJointPositions();
}
