package fr.alchemy.editor.api.undo;

/**
 * <code>AbstractUndoableOperation</code> is an abstract implementation of {@link UndoableOperation}.
 * 
 * @author GnosticOccultist
 */
public class AbstractUndoableOperation implements UndoableOperation {

	/**
	 * Whether the operation has been recently done and can therefore be undone
	 * when asked.
	 */
	private boolean done;
	
	protected AbstractUndoableOperation() {
		this.done = true;
	}
	
	@Override
	public void undo(UndoableFileEditor editor) {
		this.done = false;
	}

	@Override
	public void redo(UndoableFileEditor editor) {
		this.done = true;
	}

	@Override
	public boolean canUndo() {
		return done;
	}

	@Override
	public boolean canRedo() {
		return !done;
	}
}
