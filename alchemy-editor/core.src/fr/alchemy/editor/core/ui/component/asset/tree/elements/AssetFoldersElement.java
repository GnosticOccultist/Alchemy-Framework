package fr.alchemy.editor.core.ui.component.asset.tree.elements;

import java.nio.file.Path;
import java.util.Objects;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayCollectors;

/**
 * <code>AssetFoldersElement</code> is an implementation of {@link AssetElement}, very
 * similar to the {@link AssetFolderElement}, except it handles filling from multiple root folder.
 * 
 * @author GnosticOccultist
 */
public class AssetFoldersElement extends AssetElement {
	
	/**
	 * The array of folder's path.
	 */
    private final Array<Path> folders;

    /**
	 * Instantiates a new <code>AssetFoldersElement</code>.
	 * 
	 * @param file The array of folder's path.
	 */
    public AssetFoldersElement(Array<Path> folders) {
        super(folders.get(0));
        this.folders = folders;
    }
    
    /**
     * Return the children of the <code>AssetFoldersElement</code>.
	 * It will create for each folder path an {@link AssetFolderElement}, which can then be drilled
	 * down for filling.
     */
    @Override
    public Array<AssetElement> getChildren(Array<String> extensionFilter, boolean onlyFolders) {
        return folders.stream().map(AssetElementFactory::createFor)
                .collect(ArrayCollectors.toArray(AssetElement.class));
    }

    /**
     * Return whether the <code>AssetFoldersElement</code> has children.
     */
    @Override
    public boolean hasChildren(Array<String> extensionFilter, boolean onlyFolders) {
        return !folders.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AssetFoldersElement that = (AssetFoldersElement) o;
        return Objects.equals(folders, that.folders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), folders);
    }

    @Override
    public String toString() {
        return "FoldersResourceElement";
    }
}
