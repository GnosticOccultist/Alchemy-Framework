package fr.alchemy.editor.core.ui.editor.nodes;

import fr.alchemy.editor.api.editor.VisualNodesFileEditor;
import fr.alchemy.editor.core.ui.component.nodes.DialogNodesContainer;

public class DialogNodesFileEditor extends VisualNodesFileEditor<DialogNodesContainer> {

	@Override
	protected DialogNodesContainer createRoot() {
		return new DialogNodesContainer();
	}
}
