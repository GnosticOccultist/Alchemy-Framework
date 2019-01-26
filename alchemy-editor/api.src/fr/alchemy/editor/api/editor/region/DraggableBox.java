package fr.alchemy.editor.api.editor.region;

import fr.alchemy.editor.api.editor.graph.event.GraphEventManager;
import fr.alchemy.editor.api.editor.graph.event.GraphInputGesture;
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
	
	private static final double DEFAULT_ALIGNMENT_THRESHOLD = 5;
	
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
     * The alignment targets for the X-axis.
     */
    private double[] alignmentTargetsX;
    /**
     * The alignment targets for the Y-axis.
     */
    private double[] alignmentTargetsY;
    /**
     * The threshold value for the alignment.
     */
    private double alignmentThreshold = DEFAULT_ALIGNMENT_THRESHOLD;
	/**
     * The box dependency for the X-axis.
     */
    private DraggableBox dependencyX;
    /**
     * The box dependency for the Y-axis.
     */
    private DraggableBox dependencyY;
	
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
	 * Sets a dependent <code>DraggableBox</code> that will be moved on the X-axis when
	 * this draggable box is moved on the X-axis.
	 * 
	 * @param dependencyX The dependent box for the X-axis.
	 */
	public void bindLayoutX(DraggableBox dependencyX) {
		this.dependencyX = dependencyX;
	}
	
	/**
	 * Sets a dependent <code>DraggableBox</code> that will be moved on the Y-axis when
	 * this draggable box is moved on the Y-axis.
	 * 
	 * @param dependencyY The dependent box for the Y-axis.
	 */
	public void bindLayoutY(DraggableBox dependencyY) {
		this.dependencyY = dependencyY;
	}
	
	/**
	 * Sets the set of X-axis values that the <code>DraggableBox</code> will align to when dragged close enough.
	 * <p>
	 * This mechanism will be active if the list isn't null and not empty. If both this mechanism and
     * snap-to-grid are active, snap-to-grid will take priority.
	 * 
	 * @param pAlignmentTargetsX A list of X-axis values to align the box to, or null to ignore.
	 */
	public void setAlignmentTargetsX(final double[] pAlignmentTargetsX) {
       this.alignmentTargetsX = pAlignmentTargetsX;
    }
	
	/**
	 * Sets the set of Y-axis values that the <code>DraggableBox</code> will align to when dragged close enough.
	 * <p>
	 * This mechanism will be active if the list isn't null and not empty. If both this mechanism and
     * snap-to-grid are active, snap-to-grid will take priority.
	 * 
	 * @param pAlignmentTargetsX A list of Y-axis values to align the box to, or null to ignore.
	 */
	public void setAlignmentTargetsY(final double[] pAlignmentTargetsY) {
       this.alignmentTargetsY = pAlignmentTargetsY;
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
		
		if(GraphEventManager.instance().canActivate(GraphInputGesture.MOVE, event)) {
			final Point2D cursorPosition = FXUtils.cursorPosition(event, getContainer(this));
	        storeClick(cursorPosition.getX(), cursorPosition.getY());
	        event.consume();
		}
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
		
		GraphEventManager manager = GraphEventManager.instance();
		if(manager.canActivate(GraphInputGesture.MOVE, event)) {
			final Point2D cursorPosition = FXUtils.cursorPosition(event, getContainer(this));
			handleDrag(cursorPosition.getX(), cursorPosition.getY());
			manager.activateInputGesture(GraphInputGesture.MOVE);
			event.consume();
		}   
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
        
        if(alignmentTargetsX != null) {
        	newLayoutX = align(newLayoutX, alignmentTargetsX);
        }
        
        if(newLayoutX > maxLayoutX) {
        	newLayoutX = maxLayoutX;
        }
        
        setLayoutX(newLayoutX);
        
        if(dependencyX != null) {
        	dependencyX.setLayoutX(newLayoutX);
        }
	}

	private void handleDragY(double y) {
        final double maxParentHeight = getParent().getLayoutBounds().getHeight();
        
        final double maxLayoutY = maxParentHeight - getHeight() - 15;

        final double scaleFactor = getLocalToSceneTransform().getMxx();

        double newLayoutY = lastLayoutY + (y - lastMouseY) / scaleFactor;
        
        // Even if snap-to-grid is off, we use Math.round to ensure drawing 'on-pixel' when zoomed in past 100%.
        newLayoutY = Math.round(newLayoutY);
        
        if(alignmentTargetsY != null) {
        	newLayoutY = align(newLayoutY, alignmentTargetsY);
        }
        
        if(newLayoutY > maxLayoutY) {
        	newLayoutY = maxLayoutY;
        }
        
        setLayoutY(newLayoutY);
        
        if(dependencyY != null) {
        	dependencyY.setLayoutX(newLayoutY);
        }
	}
	
	/**
	 * Aligns the given position to the first alignment value that is closer than the alignment threshold
	 * for the <code>DraggableBox</code>.
     * <p>
     * Returns the original position if no alignment values are nearby.
	 * 
	 * @param position	      The position to be aligned.
	 * @param alignmentValues The list of alignment values.
	 * @return				  The new position after alignment.
	 */
	private double align(final double position, final double[] alignmentValues) {
		for(final double alignmentValue : alignmentValues) {
			if(Math.abs(alignmentValue - position) <= alignmentThreshold) {
				return alignmentValue;
			}
		}
		return position;
	}

	protected void handleMouseReleased(final MouseEvent event) {
		if(GraphEventManager.instance().finishInputGesture(GraphInputGesture.MOVE)) {
			event.consume();
		}
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
