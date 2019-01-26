package fr.alchemy.editor.core.ui.editor.graph.selection;

import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import javafx.scene.control.MenuItem;

public class RemoveGraphNode extends MenuItem {
	
	public RemoveGraphNode(GraphNodeSkin skin) {
		setText("Remove node");
		setOnAction(e -> skin.getGraphEditor().remove(skin));
	}
}
