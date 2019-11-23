package fr.alchemy.editor.core.ui.component.nodes;

import fr.alchemy.editor.api.ui.component.nodes.VisualNodesContainer;
import fr.alchemy.editor.core.ui.component.nodes.DialogNodeElement.DialogNode;

public class DialogNodesContainer extends VisualNodesContainer<DialogNodeElement> {

	public DialogNodesContainer() {
		
		add(new DialogNodeElement(new DialogNode("Hello, this is a test")));
	}
}
