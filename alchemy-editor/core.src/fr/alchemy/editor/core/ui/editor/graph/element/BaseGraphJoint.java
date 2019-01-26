package fr.alchemy.editor.core.ui.editor.graph.element;

import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;

/**
 * <code>BaseGraphJoint</code> is the most basic implementation of {@link GraphJoint}.
 * 
 * @author GnosticOccultist
 */
public class BaseGraphJoint implements GraphJoint {

	/**
	 * The X-axis coordinate of the joint.
	 */
	protected double x;
	/**
	 * The Y-axis coordinate of the joint.
	 */
	protected double y;
	/**
	 * The connection owning the joint.
	 */
	protected GraphConnection connection;
	/**
	 * The type of the joint.
	 */
	protected String type;
	
	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public GraphConnection getConnection() {
		return connection;
	}

	@Override
	public void setConnection(GraphConnection value) {
		this.connection = value;
	}

	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public void setType(String type) {
		this.type = type;
	}
}
