package fr.alchemy.editor.core.ui.editor.graph;

import fr.alchemy.editor.api.editor.graph.GraphConnectorValidator;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;

/**
 * <code>BaseGraphConnectorValidator</code> reprensents a basic implementation of {@link GraphConnectorValidator} which
 * simply checks that the {@link GraphConnector} aren't already connected, that they are not part of the same {@link GraphNode}
 * because it doesn't allow for loop connection, and finally that one is an 'input' and the other one an 'output' by checking
 * their type.
 * 
 * @author GnosticOccutlist
 */
public class BaseGraphConnectorValidator implements GraphConnectorValidator {

	@Override
	public boolean prevalidate(GraphConnector source, GraphConnector target) {
		if(source == null || target == null) {
			return false;
		} else if(source.equals(target)) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean validate(GraphConnector source, GraphConnector target) {
		if(source.getType() == null || target.getType() == null) {
			return false;
		} else if(!source.getConnections().isEmpty() || !target.getConnections().isEmpty()) {
			return false;
		} else if(source.getParent().equals(target.getParent())) {
			return false;
		}
		
		final boolean sourceIsInput = source.getType().contains("input");
        final boolean targetIsInput = target.getType().contains("input");

        return sourceIsInput != targetIsInput;
	}

	@Override
	public String createConnectionType(GraphConnector source, GraphConnector target) {
		return null;
	}

	@Override
	public String createJointType(GraphConnector source, GraphConnector target) {
		return null;
	}
}
