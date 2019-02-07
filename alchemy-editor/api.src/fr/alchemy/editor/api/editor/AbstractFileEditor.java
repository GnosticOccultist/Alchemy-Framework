package fr.alchemy.editor.api.editor;

import java.nio.file.Path;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

/**
 * <code>AbstractFileEditor</code> is an abstract implementation of {@link FileEditor} which should be used by every file editor implementations.
 * It uses a specific type of region to display an editing view of a file, which must be created and expanded from the root.
 * 
 * @param <R> The type of region to use as the root of the editor.
 * 
 * @author GnosticOccultist
 */
public abstract class AbstractFileEditor<R extends Region> implements FileEditor {

	/**
	 * The root region used for the editor.
	 */
	protected R root;
	/**
	 * The file currently edited by the editor.
	 */
	protected Path file;
	/**
	 * The dirty property of the file being edited.
	 */
	private final BooleanProperty dirty;
	
	/**
	 * Instantiates a new <code>AbstractFileEditor</code>.
	 */
	protected AbstractFileEditor() {
		this.dirty = new SimpleBooleanProperty(this, "dirty", false);
		this.root = createRoot();
		
		construct(root);
	}
	
	/**
	 * Creates the root region to display the editing view of the currently loaded file.
	 * The method has to be overriden in order to create the root at instantiation.
	 * 
	 * @return The newly created root region.
	 */
	protected abstract R createRoot();
	
	/**
	 * Override this method to add elements to the root region of the <code>AbstractFileEditor</code>.
	 * 
	 * @param root The root region to construct the editing view from.
	 */
	protected void construct(R root) {
		root.setOnKeyPressed(this::processKeyPressed);
	}
	
	/**
	 * Process the specified {@link KeyEvent} from the <code>AbstractFileEditor</code>.
	 * By default it just add a saving shortcut in order to quickly save a dirty edited file using
	 * the CTRL+S combination.
	 * 
	 * @param event The key event to process.
	 */
	protected void processKeyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.S && event.isControlDown() && isDirty()) {
			save();
		}
	}
	
	@Override
	public boolean open(Path file) {
		this.file = file;
		
		return false;
	}
	
	@Override
	public boolean save() {
		if(!dirty.get()) {
			return false;
		}
		
		return false;
	}
	
	@Override
	public Path getFile() {
		return file;
	}
	
	/**
	 * Return the dirty property of the edited file by the <code>AbstractFileEditor</code>. 
	 * 
	 * @return The dirty property of the edited file.
	 */
	public BooleanProperty dirtyProperty() {
		return dirty;
	}
	
	/**
	 * Return whether the edited file by the <code>AbstractFileEditor</code> has 
	 * been changed and needs to be saved.
	 * 
	 * @return Whether the file has been changed.
	 */
	public boolean isDirty() {
		return dirty.get();
	}
	
	/**
	 * Sets whether the edited file by the <code>AbstractFileEditor</code> has 
	 * been changed and needs to be saved.
	 * 
	 * @param dirty Whether the file has been changed.
	 */
	protected void setDirty(boolean dirty) {
		this.dirty.set(dirty);
	}
	
	/**
	 * Return the root region of the <code>AbstractFileEditor</code>.
	 * 
	 * @return The root region of the file editor.
	 */
	public R getRoot() {
		return root;
	}
}
