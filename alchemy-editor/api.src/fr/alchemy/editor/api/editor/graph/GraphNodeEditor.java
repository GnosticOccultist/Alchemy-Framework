package fr.alchemy.editor.api.editor.graph;

import fr.alchemy.editor.api.editor.graph.element.GraphElement;

public interface GraphNodeEditor extends GraphNodeEditorSkins {
	
	boolean isSelected(GraphElement element);
}
