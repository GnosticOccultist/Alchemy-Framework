package fr.alchemy.editor.api.editor.graph.element;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;

/**
 * <code>GraphConnection</code> is a {@link GraphElement} which represents a connection
 * between two {@link GraphConnector} inside the {@link GraphNodeEditor}.
 * It contains a set of {@link GraphJoint} representing the path corner of the connection.
 * 
 * @author GnosticOccultist
 */
public interface GraphConnection extends GraphElement {

	/**
	 * Return the source {@link GraphConnector} from which the <code>GraphConnection</code>
	 * expands from.
	 * 
	 * @return The source connector of the connection.
	 */
	GraphConnector getSource();

	/**
	 * Sets the source {@link GraphConnector} from which the <code>GraphConnection</code>
	 * expands from.
	 * 
	 * @param source The source connector of the connection.
	 */
	void setSource(GraphConnector source);
	
	/**
	 * Return the target {@link GraphConnector} to which the <code>GraphConnection</code>
	 * expands to.
	 * 
	 * @return The target connector of the connection.
	 */
	GraphConnector getTarget();

	/**
	 * Sets the target {@link GraphConnector} to which the <code>GraphConnection</code>
	 * expands to.
	 * 
	 * @param target The target connector of the connection.
	 */
	void setTarget(GraphConnector target);

	/**
	 * Return the type of the <code>GraphConnection</code>.
	 * 
	 * @return The type of the connection.
	 */
	String getType();

	/**
	 * Sets the type of the <code>GraphConnection</code>. It works with a set of keywords
	 * appended to a string, i.e. 'titled' + 'connection' means that this connection is using the titled skin.
	 * 
	 * @param type The type of the connection.
	 */
	void setType(String type);
	
	/**
	 * Return the array of {@link GraphJoint} contained by the <code>GraphConnection</code>.
	 * 
	 * @return The array of joints composing the connection.
	 */
	Array<GraphJoint> getJoints();
}
