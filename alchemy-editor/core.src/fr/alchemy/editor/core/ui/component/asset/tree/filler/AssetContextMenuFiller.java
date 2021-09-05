package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import java.util.List;

import fr.alchemy.editor.core.ui.FXUtils;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetFileElement;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetFileRestrictedElement;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.CopyFileItem;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.DeleteFileItem;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.OpenFileItem;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.PasteFileItem;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.ReadFileItem;
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
		if(element.getClass() == AssetFileElement.class) {
			items.add(new OpenFileItem(element));
		}
		
		if(element instanceof AssetFileElement) {
			items.add(new DeleteFileItem(element));
		}
		
		if(element instanceof AssetFileRestrictedElement) {
			items.add(new ReadFileItem(element));
		}
		
		items.add(new CopyFileItem(element));
		
		if(FXUtils.hasFileInClipboard()) {
			items.add(new PasteFileItem(element));
		}
	}
}
