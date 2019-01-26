package fr.alchemy.editor.api.editor.graph.element;

/**
 * <code>GraphJoint</code> is a {@link GraphElement} which represents joint inside a {@link GraphConnection}.
 * The joint mark a corner inside the connection and can be dragged to modify the connection's path.
 * 
 * @author GnosticOccultist
 */
public interface GraphJoint extends GraphElement {
	
	/**
	 * Return the X-axis coordinate of this <code>GraphJoint</code>.
	 * 
	 * @return The X coordinate of the joint.
	 */
	double getX();

	/**
	 * Sets the X-axis coordinate of this <code>GraphJoint</code>.
	 * 
	 * @param x The X coordinate of the joint.
	 */
	void setX(double x);
	
	/**
	 * Return the Y-axis coordinate of this <code>GraphJoint</code>.
	 * 
	 * @return The Y coordinate of the joint.
	 */
	double getY();

	/**
	 * Sets the Y-axis coordinate of this <code>GraphJoint</code>.
	 * 
	 * @param y The Y coordinate of the joint.
	 */
	void setY(double y);
	
	/**
	 * Return the {@link GraphConnection} which owns this <code>GraphJoint</code>.
	 * 
	 * @return The connection which owns this joint.
	 */
	GraphConnection getConnection();
	
	/**
	 * Sets the {@link GraphConnection} which owns this <code>GraphJoint</code>.
	 * 
	 * @param connection The connection which owns this joint.
	 */
	void setConnection(GraphConnection connection);

	/**
	 * Return the type of the <code>GraphJoint</code>.
	 * 
	 * @return The type of the joint.
	 */
	String getType();

	/**
	 * Sets the type of the <code>GraphJoint</code>. It works with a set of keywords appended to a string, 
	 * i.e. 'titled' + 'joint' means that this joint is using the titled skin.
	 * 
	 * @param type The type of the joint.
	 */
	void setType(String type);
}
