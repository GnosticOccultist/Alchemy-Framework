package fr.alchemy.editor.core.ui.component.asset.tree.elements;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.utilities.file.FileUtils;

/**
 * <code>AssetFolderElement</code> is an implementation of {@link AssetElement}
 * to represent a folder on the disk, which can be used as an asset repository for the editor.
 * 
 * @author GnosticOccultist
 */
public class AssetFolderElement extends AssetFileElement {
	
	/**
	 * Instantiates a new <code>AssetFolderElement</code>.
	 * 
	 * @param file The path of the asset folder.
	 */
	public AssetFolderElement(Path file) {
		super(file);
	}
	
	/**
	 * Return whether the <code>AssetFolderElement</code> has children.
	 * It will ensure that the path of the asset is a directory and check that the file's
	 * extension is accepted by the filter if defined.
	 */
	@Override
	public boolean hasChildren(Array<String> extensionFilter, boolean onlyFolders) {
		if(!Files.isDirectory(file)) {
			return false;
		}
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(file)) {
			for(Path path : stream) {
				
				String fileName = path.getFileName().toString();
				
				if(fileName.startsWith(".")) {
					continue;
				} else if (Files.isDirectory(path)) {
					return true;
				}
				
				if(onlyFolders) {
					continue;
				}
				
				String extension = FileUtils.getExtension(path);
				if(extensionFilter.isEmpty() || extensionFilter.contains(extension)) {
					return true;
				}
			}
		} catch (AccessDeniedException e) {
			return false;
		} catch (IOException e) {
			EditorManager.editor().logger().error(e.getMessage(), e);
		}
		
		return false;
	}
	
	/**
	 * Return the children of the <code>AssetFolderElement</code>.
	 * It will ensure that the path of the asset is a directory and check that the children's
	 * extension is accepted or that the children is a folder, which can then be drilled down.
	 */
	@Override
	public Array<AssetElement> getChildren(Array<String> extensionFilter, boolean onlyFolders) {
		if(!Files.isDirectory(file)) {
			return null;
		}
		
		Array<AssetElement> elements = Array.ofType(AssetElement.class);
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(file)) {
			for(Path child : stream) {
				
				String fileName = child.getFileName().toString();
				
				if(fileName.startsWith(".")) {
					continue;
				} else if (Files.isDirectory(child)) {
					elements.add(AssetElementFactory.createFor(child));
					continue;
				}
				
				if(onlyFolders) {
					continue;
				}
				
				String extension = FileUtils.getExtension(child);
				if(extensionFilter.isEmpty() || extensionFilter.contains(extension)) {
					elements.add(AssetElementFactory.createFor(child));
				}
			}
			
		} catch (IOException e) {
			EditorManager.editor().logger().error(e.getMessage(), e);
		}
		
		return elements;
	}
}
