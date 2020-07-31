package fr.alchemy.editor.core.ui.component.asset.tree.filler.items;

import fr.alchemy.editor.api.editor.FileEditor;
import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.event.AlchemyEditorEvent;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.utilities.event.SingletonEventBus;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

/**
 * <code>OpenFileItem</code> is an implementation of {@link AbstractAssetItem} which allow to open an
 * {@link AssetElement} if a {@link FileEditor} implementation exists for its extension.
 * 
 * @author GnosticOccultist
 */
public class OpenFileItem extends AbstractAssetItem {
	
	/**
	 * Instantiates a new <code>OpenFileItem</code> with the specified {@link AssetElement}.
	 * 
	 * @param element The asset element for which the item was created.
	 */
	public OpenFileItem(AssetElement element) {
		super(element);
	}

	@Override
	protected void execute(ActionEvent event) {
		SingletonEventBus.publish(AlchemyEditorEvent.OPEN_FILE, 
				AlchemyEditorEvent.newOpenFileEvent(getElement().getFile()));
	}

	@Override
	protected Image icon() {
		return EditorManager.editor().loadIcon("/resources/icons/visible.png");
	}

	@Override
	protected String title() {
		return "Open file";
	}
}
