package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import java.util.List;

import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * <code>ContextMenuFiller</code> is an interface to implement a filler designed to add {@link MenuItem}
 * to a {@link ContextMenu}.
 *
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface ContextMenuFiller {
	
	/**
	 * Fill the specified {@link AssetElement} with a list of available {@link MenuItem} for
	 * this element.
	 * 
	 * @param element The element to fill with items.
	 * @param items   The list to add the items to.
	 */
	void fill(AssetElement element, List<MenuItem> items);
}
