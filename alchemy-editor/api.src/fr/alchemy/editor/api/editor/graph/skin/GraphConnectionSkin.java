package fr.alchemy.editor.api.editor.graph.skin;

import java.util.List;
import java.util.Map;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.GraphSkinDictionary;
import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.core.ui.FXUtils;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

/**
 * <code>GraphConnectionSkin</code> is an implementation of {@link GraphSkin} for {@link GraphConnection}, which
 * is reponsible for visualizing connections inside the {@link GraphNodeEditor}.
 * <p>
 * Any custom connection skin must extend this class. It <b>must</b> also provides the super-implementation
 * constructor type in order to be applied for a specific <code>GraphConnection</code>.
 * <p>
 * The root node must be created by the skin implementation and returned by the {@link #getRoot()} method.
 * For example, a very simple connection skin could use a {@link Line} whose start and end position are set
 * to those of the source and target {@link GraphConnector}.
 * 
 * @author GnosticOccultist
 */
public abstract class GraphConnectionSkin extends GraphSkin<GraphConnection> {
	
	/**
	 * The array of points for the connection.
	 */
	private Point2D[] points;
	/**
	 * The connection index inside the list of children of the connection layer.
	 */
	private int connectionIndex;
	
	/**
	 * Instantiates a new <code>GraphConnectionSkin</code> for the specified {@link GraphConnection}.
	 * 
	 * @param node The graph connection using the graph skin.
	 */
	public GraphConnectionSkin(final GraphConnection connection) {
		super(connection);
	}
	
	/**
	 * Sets the {@link GraphJointSkin} for all {@link GraphJoint} inside the <code>GraphConnectionSkin</code>.
	 * <p>
	 * This will be called as the connection skin is created. The connection skin can manipulate its joint skin
	 * if it chooses. For example a 'rectangular' connection skin may restrict the movement of the first and last 
	 * joints to the X direction only.
	 * 
	 * @param jointSkins The list of all joint's skins associated to the connection.
	 */
	public abstract void setJointSkins(final List<GraphJointSkin> jointSkins);
	
	/**
	 * Draws the <code>GraphConnectionSkin</code>. This is called every time the {@link GraphConnection}'s
	 * position could change, for example if one of its connectors is moved, after {@link #update()}.
	 * <p>
	 * A simple connection skin may ignore the given parameter. This parameter can for example be used to
	 * display a 'rerouting' effect when the connection passes over another connection.
	 * 
	 * @param allConnections The lists of points for all connections (can be ingored in a simple skin).
	 */
	public void draw(final Map<GraphConnectionSkin, Point2D[]> allConnections) {
		if(getRoot() != null && getRoot().getParent() != null) {
			connectionIndex = getRoot().getParent().getChildrenUnmodifiable().indexOf(getRoot());
		} else {
			connectionIndex = -1;
		}
	}
	
	/**
	 * Update and return the points of the <code>GraphConnectionSkin</code>. This is called every time
	 * the connections's position could change, for example if one of its {@link GraphConnector} is
	 * moved before {@link #draw(Map)}.
	 * <p>
	 * The order of the points is as follows:
	 * 
	 * <ol>
	 * <li>Source position.
	 * <li>Joint positions in same order the joints appear in their {@link GraphConnection}.
	 * <li>Target position.
	 * </ol>
	 * 
	 * <p>
	 * This method is called on <b>all</b> connection skins <b>before</b> the draw method is called
	 * on any connection skin. It can safely be ignored by simple skin implementations.
	 * <p>
	 * Overriding this method allows the connection skin to apply constraints to its set of points, 
	 * and these constraints will be taken into account during the draw methods of other connections, 
	 * even if they are drawn before this connection.
	 * 
	 * @return The array of points of the connection.
	 */
	public Point2D[] update() {
		final GraphConnection element = getElement();
        final GraphSkinDictionary skinManager = getGraphEditor() == null ? null : getGraphEditor().getSkinDictionary();
        if(element == null || skinManager == null) {
        	points = null;
        } else if(element.getJoints().isEmpty()) {
        	final Point2D[] points = new Point2D[2];
        	
        	// Create the start position aka the source connector position.
        	points[0] = FXUtils.getConnectorPosition(element.getSource(), skinManager);
        	
        	// Create the end position aka the target connector position.
        	points[1] = FXUtils.getConnectorPosition(element.getTarget(), skinManager);
        	
        	this.points = points;
        } else {
        	final int length = element.getJoints().size() + 2;
            final Point2D[] points = new Point2D[length];
            
            // Create the joint positions.
            for (int i = 0; i < element.getJoints().size(); i++) {
            	final GraphJoint joint = element.getJoints().get(i);
            	final GraphJointSkin jointSkin = skinManager.retrieveJoint(joint);
                final Region region = jointSkin.getRoot();

                final double x = region.getLayoutX() + jointSkin.getWidth() / 2;
                final double y = region.getLayoutY() + jointSkin.getHeight() / 2;
            	
            	points[i + 1] = new Point2D(x, y);
            }
            
            // Create the start position aka the source connector position.
        	points[0] = FXUtils.getConnectorPosition(element.getSource(), skinManager);
        	
        	// Create the end position aka the target connector position.
        	points[length - 1] = FXUtils.getConnectorPosition(element.getTarget(), skinManager);
        	
        	this.points = points;
        }
        
        return points;
	}
	
	/**
	 * Return the current points of the <code>GraphConnectionSkin</code>.
	 * 
	 * @return The current points of this connection.
	 */
	protected Point2D[] getPoints() {
		return points;
	}
	
	/**
	 * Return the cached position (index) of this <code>GraphConnectionSkin</code>
	 * inside the child-list of the parent {@link GraphConnection} layer.
	 * 
	 * @return The index of the connection.
	 */
	public int getParentIndex() {
		return connectionIndex;
	}
}
