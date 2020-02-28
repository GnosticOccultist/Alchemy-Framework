package fr.alchemy.editor.api.undo;

/**
 * <code>AbstractUndoableOperation</code> is an abstract implementation of {@link UndoableOperation}.
 * 
 * @param <O> The type of operation consumer which will handle the operation changes.
 * 
 * @author GnosticOccultist
 */
public abstract class AbstractUndoableOperation<O extends OperationConsumer> implements UndoableOperation {

	/**
	 * Whether the operation has been recently done and can therefore be undone
	 * when asked.
	 */
	private boolean done;
	
	protected AbstractUndoableOperation() {
		this.done = true;
	}
	
	@Override
	public final void undo(UndoableFileEditor editor) {
		this.done = false;
		
		doUndo(cast(editor));
	}

	protected abstract void doUndo(O editor);

	@Override
	public final void redo(UndoableFileEditor editor) {
		this.done = true;
		
		doRedo(cast(editor));
	}
	
	protected abstract void doRedo(O editor);

	@Override
	public boolean canUndo() {
		return done;
	}

	@Override
	public boolean canRedo() {
		return !done;
	}
	
	@SuppressWarnings("unchecked")
	private O cast(UndoableFileEditor editor) {
		if(!(editor instanceof OperationConsumer)) {
			throw new IllegalArgumentException("The provided editor instance '" + editor + "' doesn't handle changes!");
		}
		return (O) editor;
	}
}
