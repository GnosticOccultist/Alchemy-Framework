package fr.alchemy.editor.core.ui.component.asset.tree.filler.items;

import fr.alchemy.editor.api.editor.FileEditor;
import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.event.AlchemyEditorEvent;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.utilities.event.EventBus;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

/**
 * <code>ReadFileItem</code> is an implementation of {@link AbstractAssetItem} which allow to open an
 * {@link AssetElement} in read-only mode if a {@link FileEditor} implementation exists for its extension.
 * 
 * @author GnosticOccultist
 */
public class ReadFileItem extends AbstractAssetItem {
	
	/**
	 * Instantiates a new <code>ReadFileItem</code> with the specified {@link AssetElement}.
	 * 
	 * @param element The asset element for which the item was created.
	 */
	public ReadFileItem(AssetElement element) {
		super(element);
	}

	@Override
	protected void execute(ActionEvent event) {
		EventBus.publish(AlchemyEditorEvent.OPEN_FILE, 
				AlchemyEditorEvent.newOpenFileEvent(getElement().getFile(), true));
	}

	@Override
	protected Image icon() {
		return EditorManager.editor().loadIcon("/resources/icons/visible.png");
	}

	@Override
	protected String title() {
		return "Open file (read-only)";
	}
}
