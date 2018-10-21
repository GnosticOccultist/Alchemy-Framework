package fr.alchemy.editor.core.ui.component.asset.tree.elements;

import java.nio.file.Path;

/**
 * <code>AssetFileElement</code> is an implementation of {@link AssetElement}
 * to represent a file on the disk, which can be used as an asset for the editor.
 * 
 * @author GnosticOccultist
 */
public class AssetFileElement extends AssetElement {

	/**
	 * Instantiates a new <code>AssetFileElement</code>.
	 * 
	 * @param file The path of the asset file.
	 */
	public AssetFileElement(Path file) {
		super(file);
	}
}
