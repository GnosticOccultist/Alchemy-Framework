package fr.alchemy.editor.api.control;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import fr.alchemy.editor.api.undo.OperationConsumer;
import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.array.Array;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

/**
 * <code>TreeViewControl</code> is an implementation of {@link VBox} which display an internal {@link TreeView} filled with
 * object matching the type <code>E</code>.
 * <p>
 * A selection {@link Consumer} can be defined which will be called every time the selected items in the tree change.
 * 
 * @param <E> The element type contained in the tree view.
 * 
 * @author GnosticOccultist
 */
public abstract class TreeViewControl<O extends OperationConsumer, E> extends VBox {

	/**
	 * The tree to visualize a hierarchy of elements.
	 */
	private TreeView<E> treeView;
	/**
	 * The consumer to invoke when selecting elements.
	 */
	private final Consumer<Array<?>> selectionConsumer;
	/**
	 * The operation consumer.
	 */
	private final O operationConsumer;
	
	/**
	 * Instantiates a new <code>TreeViewControl</code> with no elements in the tree.
	 * 
	 * @param selectionMode     The selection mode for the tree (not null).
	 * @param selectionConsumer The consumer to invoke when selecting elements (not null).
	 */
	public TreeViewControl(SelectionMode selectionMode, Consumer<Array<?>> selectionConsumer, O operationConsumer) {
		Validator.nonNull(selectionMode, "The selection mode can't be null!");
		Validator.nonNull(selectionConsumer, "The selection consumer can't be null!");
		
		this.selectionConsumer = selectionConsumer;
		this.operationConsumer = operationConsumer;
		createContent(selectionMode);
	}
	
	/**
	 * Create the {@link TreeView} of the <code>TreeViewControl</code> and adding it as a child.
	 * This method is automatically called in the constructor.
	 * 
	 * @param selectionMode The selection mode for the tree (not null).
	 */
	protected void createContent(SelectionMode selectionMode) {
		this.treeView = new TreeView<>();
		this.treeView.setCellFactory(o -> createControlCell());
		this.treeView.setShowRoot(true);
		this.treeView.setEditable(true);
		this.treeView.setFocusTraversable(true);
		this.treeView.prefHeightProperty().bind(heightProperty());
		this.treeView.prefWidthProperty().bind(widthProperty());
		
		MultipleSelectionModel<TreeItem<E>> selectionModel = getTreeView().getSelectionModel();
		selectionModel.setSelectionMode(selectionMode);
		selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			notifySelection();
		});
		
		getChildren().add(treeView);
	}
	
	/**
	 * Create and return an new instance of {@link TreeViewControlCell} to display the
	 * elements in the <code>TreeViewControl</code>.
	 * 
	 * @return A new control cell instance (not null).
	 */
	protected TreeViewControlCell<O, E> createControlCell() {
		return new TreeViewControlCell<>(this);
	}
	
	/**
	 * Notify that a new selection as occured in the <code>TreeViewControl</code> by invoking 
	 * the specified consumer with an array containing the selected elements.
	 */
	private void notifySelection() {
		List<Object> objects = getTreeView()
				.getSelectionModel()
				.getSelectedItems()
				.stream()
				.map(TreeItem::getValue)
				.collect(Collectors.toList());
		
		selectionConsumer.accept(Array.ofAll(objects));
	}
	
	/**
	 * Request a new {@link ContextMenu} for the given element which can be filled with {@link MenuItem}.
	 * 
	 * @param element The element for which the context menu is requested (not null).
	 * @return		  An optional value containing a menu or empty if no menu needs to be shown.
	 */
	protected Optional<ContextMenu> requestContextMenu(E element) {
		return Optional.empty();
	}

	/**
	 * Return the internal {@link TreeView} of the <code>TreeViewControl</code>.
	 * 
	 * @return The tree view of the control.
	 */
	protected TreeView<E> getTreeView() {
		return treeView;
	}
	
	/**
	 * Return the {@link OperationConsumer} in charge of handling the operation changes
	 * for the <code>TreeViewControl</code>.
	 * 
	 * @return The operation consumer of the control.
	 */
	public O getOperationConsumer() {
		return operationConsumer;
	}
}
