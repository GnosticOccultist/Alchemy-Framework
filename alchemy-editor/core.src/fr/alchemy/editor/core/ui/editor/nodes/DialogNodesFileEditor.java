package fr.alchemy.editor.core.ui.editor.nodes;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.VisualNodesFileEditor;
import fr.alchemy.editor.core.ui.component.nodes.DialogNodesContainer;

public class DialogNodesFileEditor extends VisualNodesFileEditor<DialogNodesContainer> {

	@Override
	protected DialogNodesContainer createRoot() {
		return new DialogNodesContainer();
	}
	
	@Override
	public Array<String> getSupportedExtensions() {
		return Array.of("nodes");
	}
}
