package fr.alchemy.editor.core.ui.component.asset.tree.elements;

import java.nio.file.Files;
import java.nio.file.Path;

public class AssetElementFactory {
	
    public static AssetElement createFor(Path file) {
        if (Files.isDirectory(file)) {
            return new AssetFolderElement(file);
        } else if (!Files.isWritable(file) || !Files.isReadable(file)){
            return new AssetFileRestrictedElement(file);
        } else {
        	return new AssetFileElement(file);
        }
    }
}
