package fr.alchemy.editor.core.ui.editor.nodes;

import java.io.IOException;

import fr.alchemy.editor.api.editor.VisualNodesFileEditor;
import fr.alchemy.editor.core.ui.component.nodes.DialogNodesContainer;
import fr.alchemy.utilities.collections.array.Array;

public class DialogNodesFileEditor extends VisualNodesFileEditor<DialogNodesContainer> {

	@Override
	protected DialogNodesContainer createRoot() {
		return new DialogNodesContainer();
	}
	
	@Override
	protected void doOpen() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Array<String> getSupportedExtensions() {
		return Array.of("nodes");
	}
}
