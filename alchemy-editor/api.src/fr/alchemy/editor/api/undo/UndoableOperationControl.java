package fr.alchemy.editor.api.undo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class UndoableOperationControl implements UndoableOperation {

	/**
	 * The default history size.
	 */
	private static final int DEFAULT_HISTORY = 20;
	
	/**
	 * The undoable file editor owning the control.
	 */
	private final UndoableFileEditor editor;
	/**
	 * The deque of undoable operations.
	 */
	private final Deque<UndoableOperation> operations;
	/**
	 * The limit of the deque.
	 */
	private final int limit;
	
	/**
	 * Instantiates a new <code>UndoableOperationControl</code> for the specified {@link UndoableFileEditor}
	 * with a maximum history size of 20.
	 * 
	 * @param editor The editor for which the control is created.
	 */
	public UndoableOperationControl(UndoableFileEditor editor) {
		this(editor, DEFAULT_HISTORY);
	}
	
	/**
	 * Instantiates a new <code>UndoableOperationControl</code> for the specified {@link UndoableFileEditor}
	 * and with the given maximum history size. 
	 * 
	 * @param editor  The editor for which the control is created.
	 * @param history The maximum history size.
	 */
	public UndoableOperationControl(UndoableFileEditor editor, int history) {
		this.editor = editor;
		this.operations = new ArrayDeque<UndoableOperation>(history);
		this.limit = history;
	}
	
	/**
	 * Executes the specified {@link UndoableOperation} and stores it as the first undoable
	 * action, meaning it will be the first operation to be undone when calling {@link #undo(UndoableFileEditor)}.
	 * 
	 * @param operation The operation to execute.
	 */
	public void execute(UndoableOperation operation) {
		
		operation.redo(editor);
		editor.incrementChange();
		
		if(operations.size() >= limit) {
			Iterator<UndoableOperation> it = operations.descendingIterator();
			while(it.hasNext()) {
				UndoableOperation oldestUndo = it.next();
				if(!oldestUndo.canUndo()) {
					continue;
				}
				
				operations.remove(oldestUndo);
				break;
			}
		}
		
		operation.reinject();
		operations.addFirst(operation);
	}
	
	@Override
	public void undo(UndoableFileEditor editor) {
		if(!canUndo()) {
			return;
		}
		
		UndoableOperation operation = operations.pollFirst();
		operation.undo(editor);
		editor.decrementChange();
		
		// Re-inject for possible redone operation.
		operation.reinject();
		operations.addLast(operation);
	}

	@Override
	public void redo(UndoableFileEditor editor) {
		if(!canRedo()) {
			return;
		}
		
		UndoableOperation operation = operations.pollLast();
		operation.redo(editor);
		editor.incrementChange();
		
		// Re-inject for possible undone operation.
		operation.reinject();
		operations.addFirst(operation);
	}

	@Override
	public boolean canUndo() {
		UndoableOperation operation = operations.peekFirst();
		return operation != null && operation.canUndo();
	}

	@Override
	public boolean canRedo() {
		UndoableOperation operation = operations.peekLast();
		return operation != null && operation.canRedo();
	}

	@Override
	public void reinject() {
		throw new IllegalStateException("Can't re-inject an UndoableOperationControl!");
	}
}
