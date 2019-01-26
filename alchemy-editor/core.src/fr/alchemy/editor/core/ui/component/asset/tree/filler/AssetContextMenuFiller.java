package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import java.util.List;

import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetFolderElement;
import javafx.scene.control.MenuItem;

/**
 * <code>AssetContextMenuFiller</code> is an implementation of {@link ContextMenuFiller} to fill an {@link AssetElement}
 * context menu with available items.
 * 
 * @author GnosticOccultist
 */
public class AssetContextMenuFiller implements ContextMenuFiller<AssetElement> {

	@Override
	public void fill(AssetElement element, List<MenuItem> items) {
		if(element instanceof AssetFolderElement) {
			items.add(new NewFileMenu(element));
		}
	}
}
