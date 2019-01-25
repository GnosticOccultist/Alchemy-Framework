package fr.alchemy.editor.api.editor.graph;

import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectionSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectorSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphJointSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphTailSkin;

/**
 * <code>GraphSkinDictionary</code> caches the created {@link GraphSkin} for each {@link GraphElement}
 * in order to later be accessed, cleanup or reloaded.
 * 
 * @author GnosticOccultist
 */
public interface GraphSkinDictionary {
	
	/**
	 * Retrieves the {@link GraphNodeSkin} of the provided {@link GraphNode}
	 * stored in the <code>GraphSkinDictionary</code>.
	 * 
	 * @param node The node to retrieve the skin of.
	 * @return	   The skin associated with the graph node.
	 */
	GraphNodeSkin retrieveNode(GraphNode node);
	
	/**
	 * Retrieves the {@link GraphConnectorSkin} of the provided {@link GraphConnector}
	 * stored in the <code>GraphSkinDictionary</code>.
	 * 
	 * @param node The connector to retrieve the skin of.
	 * @return	   The skin associated with the graph connector.
	 */
	GraphConnectorSkin retrieveConnector(GraphConnector connector);
	
	/**
	 * Retrieves the {@link GraphConnectionSkin} of the provided {@link GraphConnection}
	 * stored in the <code>GraphSkinDictionary</code>.
	 * 
	 * @param node The connection to retrieve the skin of.
	 * @return	   The skin associated with the graph connection.
	 */
	GraphConnectionSkin retrieveConnection(GraphConnection connection);
	
	/**
	 * Retrieves the {@link GraphJointSkin} of the provided {@link GraphJoint}
	 * stored in the <code>GraphSkinDictionary</code>.
	 * 
	 * @param node The joint to retrieve the skin of.
	 * @return	   The skin associated with the graph joint.
	 */
	GraphJointSkin retrieveJoint(GraphJoint joint);
	
	/**
	 * Retrieves the {@link GraphTailSkin} of the provided {@link GraphTail}
	 * stored in the <code>GraphSkinDictionary</code>.
	 * 
	 * @param node The connector to retrieve the skin of.
	 * @return	   The skin associated with the graph connector.
	 */
	GraphTailSkin retrieveTail(GraphConnector connector);
}
