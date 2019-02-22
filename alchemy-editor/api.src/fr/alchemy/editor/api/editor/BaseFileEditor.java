package fr.alchemy.editor.api.editor;

import java.util.concurrent.atomic.AtomicInteger;

import fr.alchemy.editor.api.undo.UndoableFileEditor;
import fr.alchemy.editor.api.undo.UndoableOperation;
import fr.alchemy.editor.api.undo.UndoableOperationControl;
import fr.alchemy.utilities.Validator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

public abstract class BaseFileEditor<R extends Region> extends AbstractFileEditor<R> implements UndoableFileEditor {

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
	
	@Override
	public void perform(UndoableOperation operation) {
		Validator.nonNull(operation, "The operation to perform can't be null!");
		operations.execute(operation);
	}
	
	@Override
	protected void processKeyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.Z && event.isControlDown()) {
			undo();
		} else if(event.getCode() == KeyCode.Y && event.isControlDown()) {
			redo();
		}
		
		super.processKeyPressed(event);
	}
	
	@Override
	public void undo() {
		operations.undo(this);
	}
	
	@Override
	public void redo() {
		operations.redo(this);
	}
	
	@Override
	public void incrementChange() {
		changeCounter.incrementAndGet();
		setDirty(true);
	}

	@Override
	public void decrementChange() {
		changeCounter.decrementAndGet();
		setDirty(changeCounter.get() > 0);
	}
}
