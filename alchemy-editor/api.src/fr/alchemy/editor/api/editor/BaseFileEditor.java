package fr.alchemy.editor.api.editor;

import java.util.concurrent.atomic.AtomicInteger;

import fr.alchemy.editor.api.model.undo.OperationConsumer;
import fr.alchemy.editor.api.model.undo.UndoableFileEditor;
import fr.alchemy.editor.api.model.undo.UndoableOperation;
import fr.alchemy.editor.api.model.undo.UndoableOperationControl;
import fr.alchemy.utilities.Validator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

/**
 * <code>BaseFileEditor</code> is an abstract extension of {@link AbstractFileEditor} which implements {@link UndoableFileEditor},
 * in order to keep track of performed {@link UndoableOperation} and undo/redo them at any time.
 * <p>
 * This implementation provides undo-redo methods and keyboard shortcuts to invoke them (CTRL+Z & CTRL+Y), as well as a change counter 
 * to automatically change the dirty flag on this editor. In order for these feature to work, you must create implementations of 
 * {@link UndoableOperation} for each action that causes a modification in the edited file and invoke them using {@link #perform(UndoableOperation)}.
 * 
 * @param <R> The type of region to use as the root of the editor.
 * 
 * @author GnosticOccultist
 */
public abstract class BaseFileEditor<R extends Region> extends AbstractFileEditor<R> implements UndoableFileEditor, OperationConsumer {

	/**
	 * The counter keeping track of the changes.
	 */
	private final AtomicInteger changeCounter;
	/**
	 * The operation control of this editor.
	 */
	private final UndoableOperationControl operations;

	/**
	 * Instantiates a new <code>BaseFileEditor</code>. The {@link UndoableOperationControl} is
	 * created with a maximum history size of 20.
	 */
	protected BaseFileEditor() {
		this.changeCounter = new AtomicInteger();
		this.operations = new UndoableOperationControl(this);
	}
	
	/**
	 * Instantiates a new <code>BaseFileEditor</code> with the provided maximum history size
	 * for the {@link UndoableOperationControl}.
	 */
	protected BaseFileEditor(int history) {
		this.changeCounter = new AtomicInteger();
		this.operations = new UndoableOperationControl(this, history);
	}
	
	/**
	 * Performs the provided {@link UndoableOperation} on this <code>BaseFileEditor</code>, using 
	 * the {@link UndoableOperationControl}.
	 * The method is intended to keep track of this operation so it can later be undone or redone.
	 * 
	 * @param operation The operation to perform on this editor (not null).
	 */
	@Override
	public void perform(UndoableOperation operation) {
		Validator.nonNull(operation, "The operation to perform can't be null!");
		operations.execute(operation);
	}
	
	/**
	 * Process the specified {@link KeyEvent} which occured on the <code>BaseFileEditor</code>.
	 * <p>
	 * This implementation just add some generic shortcuts, such as a saving shortcut (CTRL+S), a undo 
	 * operation shortcut (CTRL+Z) and a redo operation shortcut (CTRL+Y).
	 * <p>
	 * At the end of the method the event is being consumed automatically.
	 * 
	 * @param event The key event to process.
	 */
	@Override
	protected void processKeyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.Z && event.isControlDown()) {
			undo();
		} else if(event.getCode() == KeyCode.Y && event.isControlDown()) {
			redo();
		}
		
		super.processKeyPressed(event);
	}
	
	/**
	 * Undo the last performed {@link UndoableOperation}, by delegating it to the
	 * {@link UndoableOperationControl}.
	 * <p>
	 * The function is being called when the CTRL+Z shortcut has been pressed.
	 */
	@Override
	public void undo() {
		operations.undo(this);
	}
	
	/**
	 * Redo the last undone {@link UndoableOperation}, by delegating it to the
	 * {@link UndoableOperationControl}.
	 * <p>
	 * The function is being called when the CTRL+Y shortcut has been pressed.
	 */
	@Override
	public void redo() {
		operations.redo(this);
	}
	
	/**
	 * Increments the changes that occured in the <code>BaseFileEditor</code>, and
	 * update the dirty flag of the editor.
	 */
	@Override
	public void incrementChange() {
		changeCounter.incrementAndGet();
		setDirty(true);
	}

	/**
	 * Decrements the changes that occured in the <code>BaseFileEditor</code>, and
	 * update the dirty flag of the editor.
	 */
	@Override
	public void decrementChange() {
		changeCounter.decrementAndGet();
		setDirty(changeCounter.get() > 0);
	}
}
