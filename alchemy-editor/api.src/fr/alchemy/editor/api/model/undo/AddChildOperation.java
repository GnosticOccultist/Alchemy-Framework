package fr.alchemy.editor.api.model.undo;

import fr.alchemy.editor.api.control.TreeViewControl;
import fr.alchemy.utilities.Validator;

/**
 * <code>AddChildOperation</code> is an abstract implementation of {@link AbstractUndoableOperation} which can be used when
 * a child is added to a parent usually with a {@link TreeViewControl} to notify of this operation.
 *
 * @param <P> The type of the parent object.
 * @param <C> The type of the child object.
 * @param <O> The type of operation consumer which will handle the operation changes.
 * 
 * @author GnosticOccultist
 */
public abstract class AddChildOperation<P, C, O extends OperationConsumer> extends AbstractUndoableOperation<O> {

	/**
	 * The tree view control to notify about the operation changes.
	 */
	private final TreeViewControl<O, ?> treeView;
	/**
	 * The parent object to which a child is added.
	 */
	protected final P parent;
	/**
	 * The child object being added to the parent.
	 */
	protected final C child;
	
	/**
	 * Instantiates a new <code>AddChildOperation</code> for the provided parent and child instances
	 * and the {@link TreeViewControl} to be notified.
	 * 
	 * @param treeView The tree view control to notify about the operation changes (not null).
	 * @param parent   The parent object to which a child is added (not null).
	 * @param child	   The child object being added to the parent (not null).
	 */
	public AddChildOperation(TreeViewControl<O, ?> treeView, P parent, C child) {
		Validator.nonNull(treeView, "The tree view control can't be null!");
		Validator.nonNull(parent, "The parent instance can't be null!");
		Validator.nonNull(child, "The child instance can't be null!");
		this.treeView = treeView;
		this.parent = parent;
		this.child = child;
	}
	
	@Override
	protected final void doUndo(O consumer) {
		removeChild();
		consumer.handleRemovedChild(treeView, parent, child);
	}

	/**
	 * Removes the child object from its parent. The method is being called by {@link #doUndo(OperationConsumer)}.
	 */
	protected abstract void removeChild();
	
	@Override
	protected final void doRedo(O consumer) {
		addChild();
		consumer.handleAddedChild(treeView, parent, child);
	}
	
	/**
	 * Adds the child object to its parent. The method is being called by {@link #doRedo(OperationConsumer)}.
	 */
	protected abstract void addChild();
}
