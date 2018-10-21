package fr.alchemy.editor.api;

import fr.alchemy.editor.core.EditorManager;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * <code>AbstractListCell</code> is a abstraction layer to implement an {@link TextFieldListCell}
 * for the editor.
 * 
 * @param <T> The type of item.
 * 
 * @author GnosticOccultist
 */
public abstract class AbstractListCell<T> extends TextFieldListCell<T> {
	
	/**
	 * The content box.
	 */
	private final HBox content;
	/**
	 * The text label.
	 */
	private final Label text;
	/**
	 * The visibility icon.
	 */
	protected ImageView visibilityIcon;
	
	/**
	 * Instantiates a new <code>AbstractListCell</code> which
	 * needs to define {@link #getName(Object)} and {@link #getOpacity(Object)}.
	 * <p>
	 * If you want your list cell to have a visibility icon for each elements {@link #needVisibilityIcon()}
	 * needs to return true.
	 */
	public AbstractListCell() {
		
		this.content = new HBox();
		this.text = new Label();
		
		if(needVisibilityIcon()) {
			
			visibilityIcon = new ImageView();
			visibilityIcon.addEventFilter(MouseEvent.MOUSE_RELEASED, this::processEvent);
			visibilityIcon.setOnMouseReleased(this::processEvent);
			visibilityIcon.setPickOnBounds(true);
			
			content.getChildren().add(visibilityIcon);
		}
	
		content.getChildren().addAll(text);
		
		setEditable(false);
	}
	
	/**
	 * Process a {@link MouseEvent#MOUSE_RELEASED} on the visibility icon.
	 * Note that the function will only be called if {@link #needVisibilityIcon()} returns true.
	 * 
	 * @param event The mouse event to handle.
	 */
	protected void processEvent(MouseEvent event) {
		event.consume();
	
		hide(event);
	}
	
	/**
	 * Hides the icon of the cell if needed.
	 * Note that the function will only be called if {@link #needVisibilityIcon()} returns true.
	 * 
	 * @param event The mouse event to handle.
	 */
	protected abstract void hide(MouseEvent event);

	/**
	 * Update the item by updating its cell components.
	 */
	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null) {
			setText("");
			setGraphic(null);
			return;
		}
		
		if(needVisibilityIcon() && visibilityIcon != null) {
			
			visibilityIcon.setManaged(true);
			visibilityIcon.setVisible(true);
			visibilityIcon.setImage(EditorManager.editor().loadIcon("resources/icons/visible.png"));
			visibilityIcon.setOpacity(getOpacity(item));
		}
		
		text.setText(getName(item));
		
		setText("");
		setGraphic(content);
	}
	
	/**
	 * Return whether the cell needs a visibility icon.
	 * 
	 * @return Whether a visibility icon is needed.
	 */
	protected boolean needVisibilityIcon() {
		return true;
	}
	
	/**
	 * Return the name of the provided item.
	 * 
	 * @param item The item to create the name for.
	 * @return     The name of the item.
	 */
	protected abstract String getName(T item);
	
	/**
	 * Return the opacity for the cell of the provided item.
	 * 
	 * @param item The item to calculate the opacity for.
	 * @return     The opacity of the cell.
	 */
	protected abstract double getOpacity(T item);
}
