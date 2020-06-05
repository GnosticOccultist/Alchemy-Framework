package fr.alchemy.editor.api.control;

import fr.alchemy.utilities.Validator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

/**
 * <code>DraggableTab</code> is an implementation of {@link Tab} which can be dragged around using the {@link DragEvent} gesture.
 * The draggable behavior of the tab rely on both {@link DraggableTabPane} and other tabs, meaning if one of those two objects are hovered
 * the dragged tab can be potentially moved.
 * 
 * @author GnosticOccultist
 * 
 * @see DraggableTabPane
 */
public class DraggableTab extends Tab {
	
	/**
	 * The data format used in a dragboard for dragged tabs. 
	 */
	protected static final DataFormat DATA_FORMAT = new DataFormat("DraggableTab");
	/**
	 * The tab which is currently dragged or empty for none.
	 */
	public static final ObjectProperty<DraggableTab> DRAGGED_TAB = new SimpleObjectProperty<>();
	
	/**
	 * The title label of the tab.
	 */
	protected final Label titleLabel;
	/**
	 * The current tab pane to which the tab is added.
	 */
	protected TabPane tabPane;
	
	/**
	 * Instantiates a new <code>DraggableTab</code> with the given title.
	 * 
	 * @param title The title to use for the tab (not null, not empty).
	 */
	public DraggableTab(String title) {
		Validator.nonEmpty(title, "The title of the tab can't be null or empty!");
		
		this.titleLabel = new Label(title);
		
		this.titleLabel.setOnDragDetected(this::startDrag);
		this.titleLabel.setOnDragOver(this::dragOver);
		this.titleLabel.setOnDragDone(this::stopDrag);
		this.titleLabel.setOnDragDropped(this::dragDropped);
		
		construct();
	}
	
	/**
	 * Construct the <code>DraggableTab</code> by adding its title and an icon if {@link #getIcon()}
	 * doesn't return null.
	 */
	protected void construct() {
		HBox container = new HBox();
		
		Image icon = getIcon();
		if(icon != null) {
			container.getChildren().add(new ImageView(icon));
		}
		
		container.getChildren().add(titleLabel);
		
		tabPaneProperty().addListener((obs, oldVal, newVal) -> {
			this.tabPane = newVal;
		});
		
		setGraphic(container);
	}
	
	/**
	 * Start dragging the <code>DraggableTab</code> using the provided {@link MouseEvent} and
	 * by adding its {@link #DATA_FORMAT} to a {@link ClipboardContent}.
	 * 
	 * @param event The mouse event to handle (not null).
	 */
	private void startDrag(MouseEvent event) {
		WritableImage snapshot = titleLabel.snapshot(new SnapshotParameters(), null);
		
		Dragboard dragboard = titleLabel.startDragAndDrop(TransferMode.MOVE);
		ClipboardContent content = new ClipboardContent();
		content.put(DATA_FORMAT, 0);
		
		dragboard.setContent(content);
		dragboard.setDragView(snapshot);
		titleLabel.setCursor(Cursor.MOVE);
		
		DRAGGED_TAB.set(this);
		
		event.consume();
	}
	
	/**
	 * Handle the dropped <code>DraggableTab</code> using the provided {@link DragEvent} and
	 * by adding it to the corresponding {@link TabPane}.
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
		
		DraggableTab draggedTab = DRAGGED_TAB.get();
		
		TabPane oldTabPane = draggedTab.getTabPane();
		oldTabPane.getTabs().remove(draggedTab);
		
		int thisTabIndex = getTabPane().getTabs().indexOf(DraggableTab.this);
		getTabPane().getTabs().add(thisTabIndex, draggedTab);
		getTabPane().getSelectionModel().select(draggedTab);
		
		event.setDropCompleted(true);
		DRAGGED_TAB.set(null);
		event.consume();
	}
	
	/**
	 * Stop dragging the <code>DraggableTab</code> using the provided {@link DragEvent}.
	 * 
	 * @param event The drag event to handle (not null).
	 */
	private void stopDrag(DragEvent event) {
		titleLabel.setCursor(Cursor.DEFAULT);
		DRAGGED_TAB.set(null);
		event.consume();
	}
	
	/**
	 * Handle the dragged over <code>DraggableTab</code> using the provided {@link DragEvent} and
	 * by checking if it can be accepted by the tab.
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
	 * Return whether the <code>DraggableTab</code> can accept the content of the provided {@link Dragboard}.
	 * 
	 * @param dragboard The dragboard with the moved content (not null).
	 * @return			Whether the content can be accepted by the tab.
	 * 
	 * @see #containsDraggedTab(Dragboard)
	 */
	protected boolean canAccept(Dragboard dragboard) {
		boolean result = containsDraggedTab(dragboard) && DRAGGED_TAB.get() != DraggableTab.this;
				
		DraggableTab draggedTab = DRAGGED_TAB.get();
		if(tabPane instanceof DraggableTabPane) {
			DraggableTabPane pane = (DraggableTabPane) tabPane;
			result = result && pane.getTester().test(draggedTab);
		}
		
		return result;
	}
	
	/**
	 * Return whether the provided {@link Dragboard} contains the {@link #DATA_FORMAT} of the 
	 * <code>DraggableTab</code>.
	 * 
	 * @param dragboard The dragboard with the moved content (not null).
	 * @return			Whether the content contains a dragged tab.
	 */
	private boolean containsDraggedTab(Dragboard dragboard) {
		return dragboard.hasContent(DATA_FORMAT) && DRAGGED_TAB != null;
	}
	
	/**
	 * Return the {@link Image} to use as an icon for the <code>DraggableTab</code>.
	 * 
	 * @return The icon image or null to display no icon.
	 */
	protected Image getIcon() {
		// TODO: Link this to an EditorComponent method 
		return null;
	}
}
