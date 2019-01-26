package fr.alchemy.editor.api.editor.graph;

import fr.alchemy.editor.api.editor.graph.element.GraphConnector;

/**
 * <code>GraphConnectorStyle</code> is an enumeration of styles which can be applied
 * to a particular {@link GraphConnector}.
 * 
 * @author GnosticOccultist
 */
public enum GraphConnectorStyle {
	/**
	 * The default connector style.
	 */
	DEFAULT,
	/**
	 * The connector style for allowed connection.
	 */
	DRAG_OVER_ALLOWED,
	/**
	 * The connector style for forbidden connection.
	 */
	DRAG_OVER_FORBIDDEN;
}
