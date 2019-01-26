package fr.alchemy.editor.api.editor.graph.event;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;

/**
 * <code>GraphInputGesture</code> is an enumeration describing all the available gestures when using
 * a {@link GraphNodeEditor}.
 * It prevents certain gesture to perform actions at the same time, mainly moving {@link GraphElement} and
 * connecting them.
 * 
 * @author GnosticOccultist
 */
public enum GraphInputGesture {
	/**
	 * Resizing the graph editor elements.
	 */
	RESIZE,
	/**
	 * Moving the graph editor elements.
	 */
	MOVE,
	/**
	 * Connecting graph editor elements.
	 */
	CONNECT,
	/**
	 * Selecting graph editor elements.
	 */
	SELECT;
}
