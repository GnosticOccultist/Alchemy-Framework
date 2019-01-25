package fr.alchemy.editor.api.editor.graph;

import fr.alchemy.editor.api.editor.graph.element.GraphConnection;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;
import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphSkin;
import javafx.scene.layout.Region;

/**
 * <code>GraphNodeEditor</code> is an interface for displaying and editing graph-like diagrams
 * in JavaFX. Each {@link GraphElement} is composed of a {@link GraphSkin} which can be added 
 * to a {@link Region} in order to construct and display a well thought out node graph. 
 * Such element can either be:
 * <ul>
 * <li>{@link GraphNode}: The most basic element to construct new graph from. Each node can be
 * linked together using connectors and connections.
 * <li>{@link GraphConnector}: These elements can be added to a node for representing either an 
 * input or output of data through out this node.
 * A non yet linked connector can be dragged over another one to create a connection if it's allowed,
 * note that during this moment the connector is described as a tail and doesn't use the same skin.
 * <li>{@link GraphConnection}: A link between two connectors, often an input and an output, each
 * connection can be composed of multiple joints allowing to modify its path.
 * <li>{@link GraphJoint}: A corner inside a connection used to modify the generated path of said
 * connection.
 * 
 * @author GnosticOccultist
 */
public interface GraphNodeEditor {
	
	/**
	 * Return whether the specified {@link GraphElement} is selected in 
	 * the <code>GraphNodeEditor</code>.
	 * 
	 * @param element The graph element to check selection.
	 * @return		  Whether the element is currently selected.
	 */
	boolean isSelected(GraphElement element);

	/**
	 * Selects the specified {@link GraphElement} in the <code>GraphNodeEditor</code>.
	 * 
	 * @param element The element to select.
	 */
	void select(GraphElement element);

	/**
	 * Return the {@link GraphSkinDictionary} to retrieve a {@link GraphElement}'s skin.
	 * 
	 * @return The skin dictionary.
	 */
	GraphSkinDictionary getSkinDictionary();
	
	/**
	 * Return the {@link Region} used to display the {@link GraphElement} in.1
	 * 
	 * @return The editor's view region.
	 */
	Region getView();

	/**
	 * Redraws the specified {@link GraphSkin} on the <code>GraphNodeEditor</code>'s view.
	 * 
	 * @param skin The graph skin implementation to redraw.
	 */
	void redraw(GraphSkin skin);
}
