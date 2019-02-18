package fr.alchemy.editor.api.editor;

import java.nio.file.Path;

import com.ss.rlib.common.util.array.Array;

import javafx.beans.binding.BooleanExpression;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * <code>FileEditor</code> is an implementation of an {@link EditorComponent}, specially
 * designed to visually edit a physical file present on the disk.
 * <p>
 * Each file editor implementation can be used for one or multiple type of file, depending
 * on the extension of the edited file.
 * 
 * @author GnosticOccultist
 */
public interface FileEditor extends EditorComponent {
	
	/**
	 * Open the provided file and set it as the currently edited
	 * one for this <code>FileEditor</code>.
	 * 
	 * @param file The path of the file to open.
	 * @return     Whether the opening process has been successful.
	 */
	boolean open(Path file);
	
	/**
	 * Saves the currently edited file, if it exists, into the disk's file.
	 * 
	 * @return Whether the saving process has been successful.
	 */
	boolean save();
	
	/**
	 * Return the currently edited file in the <code>FileEditor</code>.
	 * 
	 * @return The edited file.
	 */
	Path getFile();
	
	/**
	 * Return the array of supported extensions for the <code>FileEditor</code>.
	 * 
	 * @return The supported extensions of files.
	 */
	Array<String> getSupportedExtensions();
	
	/**
	 * Return the dirty property of the edited file by the <code>FileEditor</code>. 
	 * 
	 * @return The dirty property of the edited file.
	 */
	BooleanExpression dirtyProperty();
	
	/**
	 * Return the root pane of the <code>FileEditor</code>. 
	 * 
	 * @return The root pane of the editor.
	 */
	Region getRoot();
	
	Pane getUIPage();
	
	/**
	 * Return the currently edited file name.
	 */
	@Override
	default String getName() {
		return getFile() != null ? getFile().getFileName().toString() : "";
	}
}