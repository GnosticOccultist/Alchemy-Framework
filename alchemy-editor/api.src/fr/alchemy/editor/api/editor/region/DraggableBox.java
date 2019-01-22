package fr.alchemy.editor.api.editor.region;

import fr.alchemy.editor.core.ui.FXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * <code>DraggableBox</code> is an implementation of {@link StackPane}, which is used to display
 * a draggable box with children layed out according to its subclass. The size of the box should be 
 * set via {@link #resize(double, double)}, and will not be affected by parent layout.
 * 
 * @author GnosticOccultist
 */
public class DraggableBox extends StackPane {
	
	/**
	 * Used for internal storing of the layout X-coordinate.
	 */
	double lastLayoutX;
	/**
	 * Used for internal storing of the layout Y-coordinate.
	 */
	double lastLayoutY;
	/**
	 * Used for internal storing of the mouse X-coordinate.
	 */
	double lastMouseX;
	/**
	 * Used for internal storing of the mouse Y-coordinate.
	 */
	double lastMouseY;
	
	/**
	 * Instantiates a new empty <code>DraggableBox</code>.
	 */
	public DraggableBox() {
		
		setPickOnBounds(false);
		
		addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
		addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
		addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);
	}
	
	/**
	 * Handles the mouse-pressed events of the <code>DraggableBox</code>.
	 * 
	 * @param event The mouse-pressed event.
	 */
	protected void handleMousePressed(final MouseEvent event) {
		if(event.getButton() != MouseButton.PRIMARY || !isEditable()) {
			return;
		}
		
		final Point2D cursorPosition = FXUtils.cursorPosition(event, getContainer(this));
        storeClick(cursorPosition.getX(), cursorPosition.getY());
        event.consume();
	}
	
	/**
	 * Stores the relevant layout values at the time of the last mouse click,
	 * using mouse-pressed event.
	 * <p>
	 * It is used during drag event to compute the correct layout coordinates based
	 * on where the <code>DraggableBox</code> was picked by the mouse.
	 * 
	 * @param x The container X-coordinate of the click event.
	 * @param y The container Y-coordinate of the click event.
	 */
	protected void storeClick(final double x, final double y) {
		lastLayoutX = getLayoutX();
		lastLayoutY = getLayoutY();
		
		lastMouseX = x;
		lastMouseY = y;
	}
	
	/**
	 * Handles the mouse-dragged events of the <code>DraggableBox</code>.
	 * 
	 * @param event The mouse-dragged event.
	 */
	protected void handleMouseDragged(final MouseEvent event) {
		if(event.getButton() != MouseButton.PRIMARY || !isEditable()) {
			return;
		}
		
        final Point2D cursorPosition = FXUtils.cursorPosition(event, getContainer(this));
        handleDrag(cursorPosition.getX(), cursorPosition.getY());
        event.consume();
	}
	
	/**
	 * Handles a drag event to the given cursor position.
	 * 
	 * @param x The cursor X-coordinate relative to the container.
	 * @param y The cursor Y-coordinate relative to the container.
	 */
	private void handleDrag(final double x, final double y) {
		handleDragX(x);
		handleDragY(y);
		
		// Notify that the box has moved.
		positionMoved();
	}

	private void handleDragX(double x) {
		final double maxParentWidth = getParent().getLayoutBounds().getWidth();
		
        final double maxLayoutX = maxParentWidth - getWidth() - 15;

        final double scaleFactor = getLocalToSceneTransform().getMxx();

        double newLayoutX = lastLayoutX + (x - lastMouseX) / scaleFactor;
        
        // Even if snap-to-grid is off, we use Math.round to ensure drawing 'on-pixel' when zoomed in past 100%.
        newLayoutX = Math.round(newLayoutX);
        
        if(newLayoutX > maxLayoutX) {
        	newLayoutX = maxLayoutX;
        }
        
        setLayoutX(newLayoutX);
	}

	private void handleDragY(double y) {
        final double maxParentHeight = getParent().getLayoutBounds().getHeight();
        
        final double maxLayoutY = maxParentHeight - getHeight() - 15;

        final double scaleFactor = getLocalToSceneTransform().getMxx();

        double newLayoutY = lastLayoutY + (y - lastMouseY) / scaleFactor;
        
        // Even if snap-to-grid is off, we use Math.round to ensure drawing 'on-pixel' when zoomed in past 100%.
        newLayoutY = Math.round(newLayoutY);
        
        if(newLayoutY > maxLayoutY) {
        	newLayoutY = maxLayoutY;
        }
        
        setLayoutY(newLayoutY);
	}
	
	protected void handleMouseReleased(final MouseEvent event) {
		//event.consume();
	}
	
	/**
	 * Return the closest ancestor (e.g. parent, grandparent) to a {@link Node} that is subclass
	 * of {@link Region}.
	 * 
	 * @param node The node to find container.
	 * @return	   The node's closest ancestor that is a subclass of region, or null if none.
	 */
	Region getContainer(final Node node) {
		final Parent parent = node.getParent();
		
		if(parent == null) {
			return null;
		} else if(parent instanceof Region) {
			return (Region) parent;
		} else {
			return getContainer(parent);
		}
	}
	
	/**
	 * Called when the position of the <code>DraggableBox</code> has been changed by
	 * the user.
	 */
	protected void positionMoved() {}
	
	/**
	 * Return <code>false</code>, the <code>DraggableBox</code> isn't resizable.
	 * Use {@link ResizableBox} if you want it to be.
	 */
	@Override
	public boolean isResizable() {
		return false;
	}
	
	/**
	 * Return whether the current mouse position would lead to a resize operation
	 * on this <code>DraggableBox</code>.
	 * <p>
	 * Note that the draggable box isn't resizable so it returns <code>false</code>, 
	 * this method is expected to be overriden by subclasses.
	 * 
	 * @return Whether the mouse is near the edge of the rectangle so 
	 * 		   that a resize would occur.
	 */
	public boolean isMouseInPositionForResize() {
		return false;
	}
	
	/**
	 * Return whether the <code>DraggableBox</code> is editable.
	 * 
	 * @return Whether the draggable box is editable.
	 */
	protected boolean isEditable() {
		return true;
	}
}
