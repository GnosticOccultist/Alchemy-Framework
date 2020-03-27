package fr.alchemy.editor.api.model.undo;

import fr.alchemy.editor.api.control.TreeViewControl;

public interface OperationConsumer {

	/**
	 * Performs the provided {@link UndoableOperation} on this <code>OperationConsumer</code>.
	 * The method is intended to keep track of this operation so it can later be undone or redone.
	 * 
	 * @param operation The operation to perform on this editor (not null).
	 */
	void perform(UndoableOperation operation);
	
	/**
	 * Handles the provided new property object using this <code>OperationConsumer</code>.
	 * 
	 * @param property The property that have been added (not null).
	 */
	default void handleAddedProperty(Object property) {}
	
	/**
	 * Handles a child object being added to a parent in the given {@link TreeViewControl} using this 
	 * <code>OperationConsumer</code>.
	 * 
	 * @param treeView The tree view control to notify about the operation changes (not null).
	 * @param parent   The parent object to which a child was added (not null).
	 * @param child    The child object which was added to the parent (not null).
	 */
	default void handleAddedChild(TreeViewControl<?, ?> treeView, Object parent, Object child) {}
	
	/**
	 * Handles a child object being removed from its parent in the given {@link TreeViewControl} using this 
	 * <code>OperationConsumer</code>.
	 * 
	 * @param treeView The tree view control to notify about the operation changes (not null).
	 * @param parent   The parent object to which a child was removed (not null).
	 * @param child    The child object which was removed from the parent (not null).
	 */
	default void handleRemovedChild(TreeViewControl<?, ?> treeView, Object parent, Object child) {}
	
	/**
	 * Handles the provided removed property object using this <code>OperationConsumer</code>.
	 * 
	 * @param property The property that have been removed (not null).
	 */
	default void handleRemovedProperty(Object property) {}
}
