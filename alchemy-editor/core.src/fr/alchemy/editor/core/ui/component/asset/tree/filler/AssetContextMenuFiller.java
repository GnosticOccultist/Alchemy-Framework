package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import java.util.List;

import fr.alchemy.editor.core.ui.FXUtils;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetFileElement;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.CopyFileItem;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.OpenFileItem;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.PasteFileItem;
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
		if(element instanceof AssetFileElement) {
			items.add(new OpenFileItem(element));
		}
		
		items.add(new CopyFileItem(element));
		
		if(FXUtils.hasFileInClipboard()) {
			items.add(new PasteFileItem(element));
		}
	}
}
