package fr.alchemy.editor.api.undo;

public interface OperationConsumer {

	/**
	 * Performs the provided {@link UndoableOperation} on this <code>OperationConsumer</code>.
	 * The method is intended to keep track of this operation so it can later be undone or redone.
	 * 
	 * @param operation The operation to perform on this editor.
	 */
	void perform(UndoableOperation operation);
	
	/**
	 * Handles the provided new property object using this <code>OperationConsumer</code>.
	 * 
	 * @param property The property that have been added.
	 */
	default void handleAddedProperty(Object property) {}
	
	/**
	 * Handles the provided removed property object using this <code>OperationConsumer</code>.
	 * 
	 * @param property The property that have been removed.
	 */
	default void handleRemovedProperty(Object property) {}
}
