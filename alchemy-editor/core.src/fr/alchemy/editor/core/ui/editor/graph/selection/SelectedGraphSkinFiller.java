package fr.alchemy.editor.core.ui.editor.graph.selection;

import java.util.List;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphSkin;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.ContextMenuFiller;
import javafx.scene.control.MenuItem;

/**
 * <code>SelectedGraphSkinFiller</code> is an implementation of {@link ContextMenuFiller} to fill a selected {@link GraphSkin}
 * context menu with available items inside the {@link GraphNodeEditor}.
 * 
 * @author GnosticOccultist
 */
public class SelectedGraphSkinFiller implements ContextMenuFiller<GraphSkin> {

	@Override
	public void fill(GraphSkin skin, List<MenuItem> items) {
		
		if(skin instanceof GraphNodeSkin) {
			items.add(new RemoveGraphNode((GraphNodeSkin) skin));
		}
	}
}
