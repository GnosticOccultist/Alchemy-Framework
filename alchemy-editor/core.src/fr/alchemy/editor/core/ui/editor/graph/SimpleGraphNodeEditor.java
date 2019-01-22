package fr.alchemy.editor.core.ui.editor.graph;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphElement;

/**
 * <code>SimpleGraphNodeEditor</code> is a basic implementation of {@link GraphNodeEditor}.
 * It manages a {@link GraphNodeEditorView} representing the entire view of the graph editor.
 * 
 * @author GnosticOccultist
 */
public class SimpleGraphNodeEditor implements GraphNodeEditor {
	
	/**
	 * The graph node editor view.
	 */
	private final GraphNodeEditorView view;
	
	public SimpleGraphNodeEditor() {
		
		this.view = new GraphNodeEditorView();
		view.getStylesheets().add("titled_skins.css");
	}
	
	/**
	 * Return the {@link GraphNodeEditorView} of this <code>SimpleGraphNodeEditor</code>.
	 * 
	 * @return The view containing the entire graph editor.
	 */
	public GraphNodeEditorView getView() {
		return view;
	}

	@Override
	public boolean isSelected(GraphElement element) {
		return false;
	}
}
