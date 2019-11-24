package fr.alchemy.editor.core.ui.component.nodes;

import java.util.Arrays;
import java.util.Collection;

import fr.alchemy.editor.api.ui.component.nodes.VisualNodesContainer;
import fr.alchemy.editor.core.ui.component.nodes.DialogNodeElement.DialogNode;
import javafx.geometry.Point2D;
import javafx.scene.control.MenuItem;

public class DialogNodesContainer extends VisualNodesContainer<DialogNodeElement> {

	public DialogNodesContainer() {
		
		add(new DialogNodeElement(this, new DialogNode("Hello, this is a test")));
	}
	
	@Override
	protected Collection<MenuItem> createAddActions(Point2D location) {
		return Arrays.asList(new MenuItem("New Dialog Text"));
	}
}
