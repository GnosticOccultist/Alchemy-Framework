package fr.alchemy.editor.api.editor;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.ui.component.nodes.VisualNodesContainer;

public abstract class VisualNodesFileEditor<C extends VisualNodesContainer> extends BaseFileEditor<VisualNodesContainer> {
	
	public VisualNodesFileEditor() {
		super();
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
	public Array<String> getSupportedExtensions() {
		return null;
	}
}
