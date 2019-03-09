package fr.alchemy.editor.core.ui.component.asset.tree.filler.items;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * <code>CopyFileItem</code> is an implementation of {@link AbstractAssetItem} which allow to copy an
 * {@link AssetElement} and its children if the element points to a directory.
 * 
 * @author GnosticOccultist
 */
public class CopyFileItem extends AbstractAssetItem {
	
	/**
	 * Instantiates a new <code>CopyFileItem</code> with the specified {@link AssetElement}.
	 * 
	 * @param element The asset element for which the item was created.
	 */
	public CopyFileItem(AssetElement element) {
		super(element);
	}

	@Override
	protected void execute(ActionEvent event) {
		
		File file = getElement().getFile().toFile();
		List<File> files = new ArrayList<>();
		files.add(file);
		
		// TODO: Handle multi-copying.
		Clipboard clipboard = Clipboard.getSystemClipboard();
		if(clipboard == null) {
			return;
		}
		
		ClipboardContent content = new ClipboardContent();
		content.putFiles(files);
		content.put(EditorManager.EDITOR_DATA, "copy");
		
		clipboard.setContent(content);
	}

	@Override
	protected Image icon() {
		return EditorManager.editor().loadIcon("/resources/icons/copy.png");
	}

	@Override
	protected String title() {
		return "Copy";
	}
}
