package fr.alchemy.editor.core.ui.component.nodes;

import fr.alchemy.editor.api.ui.component.nodes.VisualNodeElement;
import fr.alchemy.editor.core.ui.component.nodes.DialogNodeElement.DialogNode;

public class DialogNodeElement extends VisualNodeElement<DialogNode> {

	public DialogNodeElement(DialogNode element) {
		super(element);
	}

	public static class DialogNode {
		
		private final String dialog;
		
		public DialogNode(String dialog) {
			this.dialog = dialog;
		}
		
		public String getDialog() {
			return dialog;
		}
	}
}
