package fr.alchemy.editor.api.editor.graph;

import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;

/**
 * <code>GraphConnectorValidator</code> is an interface for customizing {@link GraphConnector} validation.
 * Every custom connector validator must implement this interface.
 * 
 * @author GnosticOccultist
 */
public interface GraphConnectorValidator {
	
	/**
	 * Pre-validate check made during drag-over events. If the pre-validate checks fails, 
	 * the dragged connector will not interact with the dragged-over connector at all.
	 * <p>
	 * If the pre-validate check passes it will go through the {@link #validate(GraphConnector, GraphConnector)}
	 * check.
	 * 
	 * @param source The graph connector that was dragged.
	 * @param target The graph connector that was dragged-over.
	 * @return		 Whether the pre-validation check passes.
	 */
	boolean prevalidate(GraphConnector source, GraphConnector target);
	
	/**
	 * Validate check made during drag-over events. Only made if the {@link #prevalidate(GraphConnector, GraphConnector)}
	 * check passes.
	 * <p>
	 * If the validation check passes it means that the connection is allowed and will be created based
	 * on the type returned by the {@link #createConnectionType(GraphConnector, GraphConnector)} method.
	 * 
	 * @param source The graph connector that was dragged.
	 * @param target The graph connector that was dragged-over.
	 * @return		 Whether the validation check passes and the connection is allowed.
	 */
	boolean validate(GraphConnector source, GraphConnector target);
	
	/**
	 * Creates the 'type' string to be used in a new {@link GraphConnection}.
	 * <p>
	 * If both pre-validation and validation checks pass, a new connection will be created of the
	 * type returned by this method.  
	 * 
	 * @param source The graph connector that was dragged.
	 * @param target The graph connector that was dragged-over.
	 * @return		 A string specifying the type for the new connection.
	 */
	String createConnectionType(GraphConnector source, GraphConnector target);
	
	/**
	 * Creates the 'type' string to be used in the joints inside a new {@link GraphConnection}. 
	 * 
	 * @param source The graph connector that was dragged.
	 * @param target The graph connector that was dragged-over.
	 * @return		 A string specifying the type for the new joints.
	 */
	String createJointType(GraphConnector source, GraphConnector target);
}
