package fr.alchemy.editor.api.undo;

import fr.alchemy.editor.api.editor.FileEditor;

/**
 * <code>UndoableFileEditor</code> is an implementation of {@link FileEditor} to support undo/redo of {@link UndoableOperation}.
 * Implementations of this interface can use an internal {@link UndoableOperationControl} to manage the undo and redo methods
 * 
 * @author GnosticOccultist
 */
public interface UndoableFileEditor extends FileEditor {

	/**
	 * Performs the provided {@link UndoableOperation} on this <code>UndoableFileEditor</code>.
	 * The method is intended to keep track of this operation so it can later be undone or redone.
	 * 
	 * @param operation The operation to perform on this editor.
	 */
	void perform(UndoableOperation operation);
	
	/**
	 * Undo the last {@link UndoableOperation} which was performed for this 
	 * <code>UndoableFileEditor</code>.
	 */
	void undo();
	
	/**
	 * Redo the last {@link UndoableOperation} which was undone for this 
	 * <code>UndoableFileEditor</code>.
	 */
	void redo();
	
	/**
	 * Increments the changes that occured in the <code>UndoableFileEditor</code>.
	 * The changes tracker is used to allow saving of the currently edited file.
	 */
	void incrementChange();
	
	/**
	 * Decrements the changes that occured in the <code>UndoableFileEditor</code>.
	 * The changes tracker is used to allow saving of the currently edited file.
	 */
	void decrementChange();
}
