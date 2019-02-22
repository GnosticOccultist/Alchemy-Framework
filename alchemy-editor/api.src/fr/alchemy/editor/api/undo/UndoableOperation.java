package fr.alchemy.editor.api.undo;

/**
 * <code>UndoableOperation</code> represents an interface to implement an undoable operation which is executed inside
 * an {@link UndoableFileEditor}. Such operation can be undone and redone at any time using an {@link UndoableOperationControl}.
 * 
 * @author GnosticOccultist
 */
public interface UndoableOperation {
	
	/**
	 * Undo the <code>UndoableOperation</code> which was executed inside the specified {@link UndoableFileEditor}.
	 * 
	 * @param editor The editor on which this operation was executed.
	 */
	void undo(UndoableFileEditor editor);
	
	/**
	 * Redo the <code>UndoableOperation</code> which was undone inside the specified {@link UndoableFileEditor}.
	 * 
	 * @param editor The editor on which this operation was undone.
	 */
	void redo(UndoableFileEditor editor);
	
	/**
	 * Return whether the <code>UndoableOperation</code> can be undone in its current state.
	 * 
	 * @return Whether the operation can be currently undone.
	 */
	boolean canUndo();
	
	/**
	 * Return whether the <code>UndoableOperation</code> can be redone in its current state.
	 * 
	 * @return Whether the operation can be currently redone.
	 */
	boolean canRedo();
}
