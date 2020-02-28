package fr.alchemy.editor.api.control;

import fr.alchemy.utilities.Validator;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * <code>TreeViewControlCell</code> is an implementation of {@link TextFieldTreeCell} to draw a {@link TextField}
 * in a cell of a {@link TreeViewControl}.
 * 
 * @param <E> The element type contained in the tree view to display in the cell.
 * 
 * @author GnosticOccultist
 */
public class TreeViewControlCell<E> extends TextFieldTreeCell<E> {

	/**
	 * The tree view control.
	 */
	private final TreeViewControl<E> treeViewControl;
	
	/**
	 * Instantiates a new <code>TreeViewControlCell</code> for the provided {@link TreeViewControl}.
	 * 
	 * @param treeViewControl The tree view control (not null).
	 */
	protected TreeViewControlCell(TreeViewControl<E> treeViewControl) {
		Validator.nonNull(treeViewControl, "The tree view control can't be null!");
		this.treeViewControl = treeViewControl;
		
		setOnMouseClicked(this::handleMouseClick);
	}
	
	/**
	 * Handles the provided click {@link MouseEvent} by requesting a {@link ContextMenu} to
	 * the {@link TreeViewControl}.
	 * 
	 * @param event The mouse event to handle (not null).
	 */
	private void handleMouseClick(MouseEvent event) {
		MouseButton button = event.getButton();
		
		if(button == MouseButton.SECONDARY) {
			E element = getItem();
			if(element == null) {
				return;
			}
			
			treeViewControl.requestContextMenu(getItem()).
				ifPresent(menu -> menu.show(this, Side.BOTTOM, 0, 0));
		}
	}
}
