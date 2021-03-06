package fr.alchemy.editor.api.editor;

import java.io.IOException;
import java.nio.file.Path;

import fr.alchemy.editor.api.element.ToolbarEditorElement;
import fr.alchemy.utilities.collections.array.Array;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
	 * The UI elements composing the file editor.
	 */
	protected final Array<EditorElement> elements = Array.ofType(EditorElement.class);
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
	 * The read-only property of the file being edited.
	 */
	private final BooleanProperty readOnly;
	
	/**
	 * Instantiates a new <code>AbstractFileEditor</code>.
	 */
	protected AbstractFileEditor() {
		this.dirty = new SimpleBooleanProperty(this, "dirty", false);
		this.readOnly = new SimpleBooleanProperty(this, "readOnly", false);
		this.root = createRoot();
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

        VBox container = new VBox();
        StackPane page = new StackPane(container);
        page.setPickOnBounds(true);
        
        elements.forEach(element -> element.constructUI(container));
        
        ToolbarEditorElement toolbar = getElement(ToolbarEditorElement.class);
        // If there is no toolbar we can take the whole height of the container.
        if(toolbar == null) {
        	root.prefHeightProperty().bind(container.heightProperty());
        }
        
        root.prefWidthProperty().bind(container.widthProperty());
        container.getChildren().add(root);
        
		root.setOnKeyPressed(this::processKeyPressed);
	}
	
	/**
	 * Process the specified {@link KeyEvent} which occured on the <code>AbstractFileEditor</code>.
	 * <p>
	 * This implementation just add a saving shortcut, in order to quickly save a dirty edited file using
	 * the CTRL+S combination, before consuming the event.
	 * 
	 * @param event The key event to process.
	 */
	protected void processKeyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.S && event.isControlDown() && isDirty()) {
			save();
		}
		
		event.consume();
	}
	
	@Override
	public boolean open(Path file) {
		this.file = file;
		
		try {
			doOpen();
			loadState();
			return true;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Performs the main operation to open a file in the <code>AbstractFileEditor</code>. The path of
	 * the opened file is already set with the {@link #open(Path)} method.
	 * 
	 * @throws IOException Thrown if the file failed to be read.
	 */
	protected abstract void doOpen() throws IOException;
	
	/**
	 * Loads the state of the <code>AbstractFileEditor</code> after opening a file to ensure that the 
	 * editor options and file are matching.
	 */
	protected void loadState() {
		
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
	@Override
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
	 * Return the read-only property of the edited file by the <code>AbstractFileEditor</code>. 
	 * 
	 * @return The read-only property of the edited file.
	 */
	@Override
	public BooleanExpression readOnlyProperty() {
		return readOnly;
	}
	
	/**
	 * Return whether the edited file by the <code>AbstractFileEditor</code> is readable-only, meaning
	 * it cannot be modified.
	 * 
	 * @return Whether the file is readable-only.
	 */
	public boolean isReadOnly() {
		return readOnly.get();
	}
	
	/**
	 * Sets whether the edited file by the <code>AbstractFileEditor</code> is readable-only, meaning
	 * it cannot be modified.
	 * 
	 * @param readOnly Whether the file is readable-only.
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly.set(readOnly);
	}
	
	/**
	 * Return the UI-page of the <code>AbstractFileEditor</code>.
	 * 
	 * @return The UI-page of the file editor.
	 */
	public Pane getUIPage() {
		return (Pane) root.getParent().getParent();
	}
	
	@Override
	public R getRoot() {
		return root;
	}
	
	/**
	 * Return an {@link EditorElement} matching the given element type if any
	 * is present in the <code>AbstractFileEditor</code>.
	 * 
	 * @param type The type of editor element to get.
	 * @return	   The editor element or null if none.
	 */
	public <E extends EditorElement> E getElement(Class<E> type) {
		for(EditorElement element : elements) {
			if(type.isAssignableFrom(element.getClass())) {
				return type.cast(element);
			}
		}
		return null;
	}
}
