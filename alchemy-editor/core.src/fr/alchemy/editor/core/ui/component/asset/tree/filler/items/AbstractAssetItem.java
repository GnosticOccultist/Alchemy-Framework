package fr.alchemy.editor.core.ui.component.asset.tree.filler.items;

import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.AssetContextMenuFiller;
import fr.alchemy.utilities.Validator;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * <code>AbstractAssetItem</code> is an abstract implementation of {@link MenuItem} to integrate
 * a context menu item generated when an {@link AssetElement} is being right-clicked.
 * <p>
 * Each implementation of this class must be registered inside an {@link AssetContextMenuFiller}
 * in order to be added when constructing the {@link ContextMenu}.
 * 
 * @author GnosticOccultist
 */
public abstract class AbstractAssetItem extends MenuItem {
	
	/**
	 * The asset element.
	 */
	private AssetElement element;

	/**
	 * Instantiates a new <code>AbstractAssetItem</code> with the specified {@link AssetElement}.
	 * 
	 * @param element The asset element for which the item was created.
	 */
	protected AbstractAssetItem(AssetElement element) {
		Validator.nonNull(element, "The asset element can't be null!");
		this.element = element;
		
		String title = (title().isEmpty() || title() == null) ? "Unnamed Item" : title();
		setText(title);
		setOnAction(this::execute);
	
		if(icon() != null) {
			setGraphic(new ImageView(icon()));
		}
	}
	
	/**
	 * Implement an operation to be executed when the <code>AbstractAssetItem</code>
	 * is fired.
	 * 
	 * @param event The event that has been firing the operation.
	 */
	protected abstract void execute(ActionEvent event);
	
	/**
	 * Return the {@link Image} used as an icon of the <code>AbstractAssetItem</code>.
	 * 
	 * @return The icon to use for the item.
	 */
	protected abstract Image icon();
	
	/**
	 * Return the title as a {@link String} of the <code>AbstractAssetItem</code>.
	 * 
	 * @return The title to use for the item.
	 */
	protected abstract String title();
	
	/**
	 * Return the {@link AssetElement} for which the <code>AbstractAssetItem</code>
	 * was created.
	 * 
	 * @return The asset element owning the menu item.
	 */
	protected AssetElement getElement() {
		return element;
	}
}
