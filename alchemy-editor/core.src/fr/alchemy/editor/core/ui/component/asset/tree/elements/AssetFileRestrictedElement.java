package fr.alchemy.editor.core.ui.component.asset.tree.elements;

import java.nio.file.Path;

/**
 * <code>AssetFileRestrictedElement</code> is an implementation of {@link AssetElement} to represent a 
 * file on the disk, which access is restricted as read-only but can be used as an asset for the editor.
 * 
 * @author GnosticOccultist
 */
public class AssetFileRestrictedElement extends AssetElement {

	/**
	 * Instantiates a new <code>AssetFileRestrictedElement</code>.
	 * 
	 * @param file The path of the restricted asset file.
	 */
	public AssetFileRestrictedElement(Path file) {
		super(file);
	}

}
