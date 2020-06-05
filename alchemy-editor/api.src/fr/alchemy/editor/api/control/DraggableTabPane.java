package fr.alchemy.editor.api.control;

import java.util.function.Predicate;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * <code>DraggableTabPane</code> is an implementation of {@link TabPane} to handle {@link DraggableTab}.
 * 
 * @author GnosticOccultist
 * 
 * @see DraggableTab
 */
public class DraggableTabPane extends TabPane {

	/**
	 * The predicate to check if a tab can be attached.
	 */
	private Predicate<DraggableTab> tester;
	
	/**
	 * Instantiates a new <code>DraggableTabPane</code> using the provided {@link Predicate}.
	 * 
	 * @param tester The predicate to check if a tab can be attached, or null.
	 */
	public DraggableTabPane(Predicate<DraggableTab> tester) {
		this.tester = tester == null ? t -> true : tester;
		
		construct();
	}
	
	/**
	 * Instantiates a new <code>DraggableTabPane</code> with the given {@link Tab} to display 
	 * and using the provided {@link Predicate}.
	 * 
	 * @param tester The predicate to check if a tab can be attached, or null.
	 * @param tabs	 The tabs to display inside the pane.
	 */
	public DraggableTabPane(Predicate<DraggableTab> tester, Tab... tabs) {
		super(tabs);
		
		this.tester = tester == null ? t -> true : tester;
		
		construct();
	}

	/**
	 * Construct the <code>DraggableTabPane</code> content.
	 */
	protected void construct() {
		setRotateGraphic(true);
		
		setOnDragOver(this::dragOver);
		setOnDragDropped(this::dragDropped);
	}
	
	/**
	 * Handle the dropped {@link DraggableTab} on the <code>DraggableTabPane</code> using the 
	 * provided {@link DragEvent} and by adding the tab to the pane if possible.
	 * 
	 * @param event The drag event to handle (not null).
	 * 
	 * @see #canAccept(Dragboard)
	 */
	private void dragDropped(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if(!canAccept(dragboard)) {
			return;
		}
		
		Tab draggedTab = DraggableTab.DRAGGED_TAB.get();
		
		TabPane oldTabPane = draggedTab.getTabPane();
		oldTabPane.getTabs().remove(draggedTab);

		getTabs().add(draggedTab);
		getSelectionModel().select(draggedTab);

		DraggableTab.DRAGGED_TAB.set(null);
		
		event.setDropCompleted(true);
		event.consume();
	}
	
	/**
	 * Handle the {@link DraggableTab} dragged over the <code>DraggableTabPane</code> using the 
	 * provided {@link DragEvent} and by checking if it can be accepted by the pane.
	 * 
	 * @param event The drag event to handle (not null).
	 * 
	 * @see #canAccept(Dragboard)
	 */
	private void dragOver(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if(!canAccept(dragboard)) {
			return;
		}
		
		event.acceptTransferModes(TransferMode.MOVE);
		event.consume();
	}
	
	/**
	 * Return whether the <code>DraggableTabPane</code> can accept the content of the provided {@link Dragboard}.
	 * 
	 * @param dragboard The dragboard with the moved content (not null).
	 * @return			Whether the content can be accepted by the pane.
	 * 
	 * @see #containsDraggedTab(Dragboard)
	 */
	protected boolean canAccept(Dragboard dragboard) {
		boolean result = isDraggingTab(dragboard) && 
				DraggableTab.DRAGGED_TAB.get().getTabPane() == DraggableTabPane.this;
				
		DraggableTab draggedTab = DraggableTab.DRAGGED_TAB.get();
		return result && tester.test(draggedTab);
	}
	
	/**
	 * Return whether the provided {@link Dragboard} contains the {@link #DATA_FORMAT} of the 
	 * {@link DraggableTab}.
	 * 
	 * @param dragboard The dragboard with the moved content (not null).
	 * @return			Whether the content contains a dragged tab.
	 */
	private boolean isDraggingTab(Dragboard dragboard) {
		return dragboard.hasContent(DraggableTab.DATA_FORMAT) && DraggableTab.DRAGGED_TAB != null;
	}
	
	/**
	 * Return the {@link Predicate} to check if a {@link DraggableTab} can be attached to 
	 * the <code>DraggableTabPane</code>.
	 * 
	 * @return The predicate to check if a tab can be attached.
	 */
	protected Predicate<DraggableTab> getTester() {
		return tester;
	}
}
