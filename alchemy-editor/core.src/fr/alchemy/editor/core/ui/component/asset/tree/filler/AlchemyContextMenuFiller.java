package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import java.util.List;

import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetFolderElement;
import javafx.scene.control.MenuItem;

/**
 * <code>AlchemyContextMenuFiller</code> is the default implementation of a {@link ContextMenuFiller}
 * to be used inside the Alchemy Editor.
 * 
 * @author GnosticOccultist
 */
public class AlchemyContextMenuFiller implements ContextMenuFiller {

	@Override
	public void fill(AssetElement element, List<MenuItem> items) {
		
		if(element instanceof AssetFolderElement) {
			items.add(new NewFileMenu(element));
		}
	}
}
