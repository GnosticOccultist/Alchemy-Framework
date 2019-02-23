package fr.alchemy.editor.core.ui.component.asset.tree.filler.items;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.utilities.array.Array;
import fr.alchemy.utilities.file.FileUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

public class PasteFileItem extends MenuItem {
	
	private AssetElement element;
	
	public PasteFileItem(AssetElement element) {
		
		this.element = element;
		
		setText("Paste");
		setOnAction(this::execute);
		setGraphic(new ImageView(EditorManager.editor().loadIcon("/resources/icons/clipboard.png")));
	}
	
	private void execute(ActionEvent event) {
		
		Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard == null) {
            return;
        }
        
        Path currentFile = element.getFile();

        @SuppressWarnings("unchecked")
		List<File> files = (List<File>) clipboard.getContent(DataFormat.FILES);
        if (files == null || files.isEmpty()) {
            return;
        }
        
        files.forEach(file -> copyFile(currentFile, file.toPath()));
        clipboard.clear();
	}

	private void copyFile(Path target, Path file) {
		
		// Make sure we are copying in a directory.
		if(!Files.isDirectory(target)) {
			target = target.getParent();
		}
		
		Array<Path> files = null;
		if(Files.isDirectory(file)) {
			 files = FileUtils.getFiles(file, false, true);
		}
		
		Path newFile = target.resolve(file.getFileName().toString());
		
		try {
			safeCopy(file, files, newFile);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
	}
	
	private void safeCopy(Path file, Array<Path> files, Path newFile) throws IOException {
		Files.copy(file, newFile);
		
		files.forEach(path -> {
			
			Path targetFile = newFile.resolve(file.relativize(path));
			
			try {
				Files.copy(path, targetFile);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}
}
