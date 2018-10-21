package fr.alchemy.editor.core.ui.component.asset.tree.elements;

import java.nio.file.Path;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.core.ui.component.asset.tree.AssetTree;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.ContextMenuFiller;
import javafx.scene.control.ContextMenu;

/**
 * <code>AssetElement</code> is an abstraction layer representing an asset inside 
 * the {@link AssetTree}, which is used to recursively cycle through the children of a root node.
 * To each asset element, can be attributed a {@link ContextMenu} builded using a {@link ContextMenuFiller}. 
 * <p>
 * It can either be an {@link AssetFileElement} or an {@link AssetFolderElement}.
 * 
 * @author GnosticOccultist
 */
public abstract class AssetElement implements Comparable<AssetElement> {
	
	/**
	 * The path corresponding to the asset.
	 */
	protected final Path file;
	
	/**
	 * Instantiates a new <code>AssetElement</code>
	 * 
	 * @param file The path of the asset.
	 */
	protected AssetElement(Path file) {
		this.file = file;
	}
	
	/**
	 * Return whether the <code>AssetElement</code> has children.
	 * <p>
	 * By default, it returns false.
	 * 
	 * @param extensionFilter The array containing the filtered extensions or none.
	 * @param onlyFolders 	  Whether to only show folder elements.
	 * @return				  Whether the asset element has children.
	 */
	public boolean hasChildren(Array<String> extensionFilter, boolean onlyFolders) {
		return false;
	}
	
	/**
	 * Return the array of children of the <code>AssetElement</code>.
	 * <p>
	 * By default, it returns an empty array.
	 * 
	 * @param extensionFilter The array containing the filtered extensions or none.
	 * @param onlyFolders	  Whether to only show folder elements.
	 * @return				  The array of children of the element.
	 */
	public Array<AssetElement> getChildren(Array<String> extensionFilter, boolean onlyFolders) {
		return Array.empty();
	}
	
	/**
	 * Return the path of the <code>AssetElement</code>.
	 * 
	 * @return The path to the asset.
	 */
	public Path getFile() {
		return file;
	}
	
    @Override
    public int compareTo(AssetElement other) {
        if (other == null) {
        	return -1;
        }
        Path file = getFile();
        Path otherFile = other.getFile();
        return file.getNameCount() - otherFile.getNameCount();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        if (o instanceof Path) {
        	return file.equals(o);
        }
        if(o instanceof AssetElement) {
        	AssetElement other = (AssetElement) o;
        	return file.equals(other.file);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }

    @Override
    public String toString() {
        return "AssetElement[" + file + ']';
    }
}
