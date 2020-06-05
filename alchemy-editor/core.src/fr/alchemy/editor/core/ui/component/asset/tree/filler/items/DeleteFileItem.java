package fr.alchemy.editor.core.ui.component.asset.tree.filler.items;

import com.ss.rlib.common.util.FileUtils;

import fr.alchemy.editor.core.ui.component.asset.tree.AssetTree;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

/**
 * <code>DeleteFileItem</code> is an implementation of {@link AbstractAssetItem} which allow to delete an
 * {@link AssetElement} from its {@link AssetTree} and the corresponding file from the disk.
 * 
 * @author GnosticOccultist
 */
public class DeleteFileItem extends AbstractAssetItem {

	/**
	 * Instantiates a new <code>DeleteFileItem</code> with the specified {@link AssetElement}.
	 * 
	 * @param element The asset element for which the item was created.
	 */
	public DeleteFileItem(AssetElement element) {
		super(element);
	}

	@Override
	protected void execute(ActionEvent event) {
		FileUtils.delete(getElement().getFile());
	}

	@Override
	protected Image icon() {
		return null;
	}

	@Override
	protected String title() {
		return "Delete";
	}
}
