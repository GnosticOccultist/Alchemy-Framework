package fr.alchemy.editor.core.ui.component.asset.tree.filler.items;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class CopyFileItem extends MenuItem {
	
	private AssetElement element;
	
	public CopyFileItem(AssetElement element) {
		
		this.element = element;
		
		setText("Copy");
		setOnAction(this::execute);
		setGraphic(new ImageView(EditorManager.editor().loadIcon("/resources/icons/copy.png")));
	}
	
	private void execute(ActionEvent event) {
		
		Path path = element.getFile();
		File file = path.toFile();
		List<File> files = new ArrayList<>();
		files.add(file);
		
		// TODO: Handle multi-copying.
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent content = new ClipboardContent();
        content.putFiles(files);
        content.put(EditorManager.EDITOR_DATA, "copy");
		
        clipboard.setContent(content);
	}
}
