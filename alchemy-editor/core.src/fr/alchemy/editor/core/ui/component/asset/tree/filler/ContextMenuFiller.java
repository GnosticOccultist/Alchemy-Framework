package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import java.util.List;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * <code>ContextMenuFiller</code> is an interface to implement a filler designed to add {@link MenuItem}
 * to a {@link ContextMenu}.
 * 
 * @param <E> The element to fill the context menu for.
 * 
 * @author GnosticOccultist
 */
@FunctionalInterface
public interface ContextMenuFiller<E> {
	
	/**
	 * Fill the specified element with a list of available {@link MenuItem} for it.
	 * 
	 * @param element The element to fill with items.
	 * @param items   The list to add the items to.
	 */
	void fill(E element, List<MenuItem> items);
}
