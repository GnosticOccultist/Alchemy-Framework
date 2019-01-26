package fr.alchemy.editor.core.ui.editor.graph.element;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;

/**
 * <code>BaseGraphConnection</code> is the most basic implementation of {@link GraphConnection}.
 * 
 * @author GnosticOccultist
 */
public class BaseGraphConnection implements GraphConnection {
	
	/**
	 * The source connector from which the connection is expanded.
	 */
	protected GraphConnector source;
	/**
	 * The target connector to which the connection is expanded.
	 */
	protected GraphConnector target;
	/**
	 * The type of the connection.
	 */
	protected String type;
	/**
	 * The array of joints composing the connection.
	 */
	protected final Array<GraphJoint> joints = Array.ofType(GraphJoint.class);

	@Override
	public GraphConnector getSource() {
		return source;
	}
	
	@Override
	public void setSource(GraphConnector source) {
		if(source == null && this.source != null) {
			this.source.getConnections().remove(this);
		}
		
		this.source = source;
		
		if(this.source != null) {
			this.source.getConnections().add(this);
		}
	}

	@Override
	public GraphConnector getTarget() {
		return target;
	}
	
	@Override
	public void setTarget(GraphConnector target) {
		if(target == null && this.target != null) {
			this.target.getConnections().remove(this);
		}
		
		this.target = target;
		
		if(this.target != null) {
			this.target.getConnections().add(this);
		}
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
	public Array<GraphJoint> getJoints() {
		return joints;
	}
}
