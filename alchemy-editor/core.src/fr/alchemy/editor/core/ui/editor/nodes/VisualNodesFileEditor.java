package fr.alchemy.editor.core.ui.editor.nodes;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.BaseFileEditor;
import fr.alchemy.editor.core.ui.component.nodes.VisualNodesContainer;

public class VisualNodesFileEditor extends BaseFileEditor<VisualNodesContainer> {

	public VisualNodesFileEditor() {
		construct(root);
	}
	
	@Override
	public void handleAddedProperty(Object property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRemovedProperty(Object property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected VisualNodesContainer createRoot() {
		return new VisualNodesContainer();
	}

	@Override
	public Array<String> getSupportedExtensions() {
		return null;
	}
}
