package fr.alchemy.editor.api.editor.region;

import fr.alchemy.editor.core.ui.FXUtils;
import fr.alchemy.utilities.Validator;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * <code>ResizableBox</code> is an implementation of {@link DraggableBox}, which can be resized
 * with its own implementation based on the region of the mouse.
 * 
 * @author GnosticOccultist
 */
public class ResizableBox extends DraggableBox {
	
	/**
	 * The default distance from the box border in which to consider drag events as
	 * resize events (default &rarr; 8).
	 */
	private static final int DEFAULT_RESIZE_BORDER_TOLERANCE = 8;
	
	/**
	 * Used for internal storing of the layout last width.
	 */
	double lastWidth;
	/**
	 * Used for internal storing of the layout last height.
	 */
	double lastHeight;
	/*
	 * The distance from the box border in which to consider drag events as
	 * resize events (default &rarr; 8).
	 */
	private int resizeBorderTolerance = DEFAULT_RESIZE_BORDER_TOLERANCE;
	/**
	 * Whether the box can be resized with the current mouse position.
	 */
	private boolean mouseInPositionForResize;
	/**
	 * The last mouse region for the resizable box.
	 */
	private RectangleMouseRegion lastMouseRegion;
	
	/**
	 * Instantiates a new empty <code>ResizableBox</code>.
	 */
	public ResizableBox() {
		
		addEventHandler(MouseEvent.MOUSE_ENTERED, this::processMousePosition);
		addEventHandler(MouseEvent.MOUSE_MOVED, this::processMousePosition);
		addEventHandler(MouseEvent.MOUSE_EXITED, this::handleMouseExited);
	}
	
	@Override
	protected void handleMousePressed(final MouseEvent event) {
		super.handleMousePressed(event);
		
		if(!(getParent() instanceof Region)) {
			return;
		} else if(!event.isPrimaryButtonDown() || !isEditable()) {
			setCursor(null);
			return;
		}
		
		storeClickForResize(event.getX(), event.getY());
	}
	
	@Override
	protected void handleMouseDragged(MouseEvent event) {
		if(lastMouseRegion == null || !(getParent() instanceof Region) || !event.isPrimaryButtonDown() || !isEditable()) {
			setCursor(null);
			return;
		}
		
		final Point2D cursorPosition = FXUtils.cursorPosition(event, getContainer(this));
		if(lastMouseRegion == RectangleMouseRegion.INSIDE) {
			super.handleMouseDragged(event);
		} else if(lastMouseRegion != RectangleMouseRegion.OUTSIDE) {
			handleResize(cursorPosition.getX(), cursorPosition.getY());
			event.consume();
		}
	}
	
	@Override
	protected void handleMouseReleased(final MouseEvent event) {
		super.handleMouseReleased(event);
		processMousePosition(event);
	}
	
	private void handleMouseExited(final MouseEvent event) {
		if(!event.isPrimaryButtonDown()) {
			setCursor(null);
		}
	}
	
	private void processMousePosition(final MouseEvent event) {
		if(event.isPrimaryButtonDown() || !isEditable()) {
			return;
		}
		
		final RectangleMouseRegion mouseRegion = getMouseRegion(event.getX(), event.getY());
		mouseInPositionForResize = mouseRegion != RectangleMouseRegion.INSIDE;
		updateCursor(mouseRegion);
	}

	private void handleResize(final double x, final double y) {
		switch (lastMouseRegion) {
			case EAST:
				handleResizeEast(x);
				break;
			case WEST:
				handleResizeWest(x);
				break;
			case NORTH:
				handleResizeNorth(y);
				break;
			case SOUTH:
				handleResizeSouth(y);
				break;
			case NORTH_EAST:
				handleResizeNorth(y);
                handleResizeEast(x);
				break;
			case NORTH_WEST:
				handleResizeNorth(y);
				handleResizeWest(x);
				break;
			case SOUTH_EAST:
				handleResizeSouth(y);
				handleResizeEast(x);
				break;
			case SOUTH_WEST:
				handleResizeSouth(y);
                handleResizeWest(x);
				break;
			
			case INSIDE:
			case OUTSIDE:
				break;
		}
	}

    /**
     * Handles a resize event in the north (top) direction to the given cursor y
     * position.
     *
     * @param y the cursor scene-y position
     */
    private void handleResizeNorth(final double y) {

        final double scaleFactor = getLocalToSceneTransform().getMyy();

        final double yDragDistance = (y - lastMouseY) / scaleFactor;
        final double minResizeHeight = Math.max(getMinHeight(), 0);

        double newLayoutY = lastLayoutY + yDragDistance;
        double newHeight = lastHeight - yDragDistance;

        
        // Even if snap-to-grid is off, we use Math.round to ensure drawing 'on-pixel' when zoomed in past 100%.
        final double roundedLayoutY = Math.round(newLayoutY);
        newHeight = Math.round(newHeight - roundedLayoutY + newLayoutY);
        newLayoutY = roundedLayoutY;

        // Min & max resize logic here.
        if (newLayoutY < 15)
        {
            newLayoutY = 15;
            newHeight = lastLayoutY + lastHeight - 15;
        }
        else if (newHeight < minResizeHeight)
        {
            newLayoutY = lastLayoutY + lastHeight - minResizeHeight;
            newHeight = minResizeHeight;
        }

        setLayoutY(newLayoutY);
        setHeight(newHeight);
    }

    /**
     * Handles a resize event in the south (bottom) direction to the given
     * cursor y position.
     *
     * @param y the cursor scene-y position
     */
    private void handleResizeSouth(final double y) {

        final double scaleFactor = getLocalToSceneTransform().getMyy();

        final double yDragDistance = (y - lastMouseY) / scaleFactor;
        final double maxParentHeight = getParent().getLayoutBounds().getHeight();

        final double minResizeHeight = Math.max(getMinHeight(), 0);
        final double maxAvailableHeight = maxParentHeight - getLayoutY() - 15;

        double newHeight = lastHeight + yDragDistance;

        // Even if snap-to-grid is off, we use Math.round to ensure drawing 'on-pixel' when zoomed in past 100%.
        newHeight = Math.round(newHeight);

        // Min & max resize logic here.
        if (newHeight > maxAvailableHeight) {
            newHeight = maxAvailableHeight;
        } else if (newHeight < minResizeHeight) {
            newHeight = minResizeHeight;
        }

        setHeight(newHeight);
    }

    /**
     * Handles a resize event in the east (right) direction to the given cursor
     * x position.
     *
     * @param x the cursor scene-x position
     */
    private void handleResizeEast(final double x) {

        final double scaleFactor = getLocalToSceneTransform().getMxx();

        final double xDragDistance = (x - lastMouseX) / scaleFactor;
        final double maxParentWidth = getParent().getLayoutBounds().getWidth();

        final double minResizeWidth = Math.max(getMinWidth(), 0);
        final double maxAvailableWidth = maxParentWidth - getLayoutX() - 15;

        double newWidth = lastWidth + xDragDistance;

        // Even if snap-to-grid is off, we use Math.round to ensure drawing 'on-pixel' when zoomed in past 100%.
        newWidth = Math.round(newWidth);
        
        // Min & max resize logic here.
        if (newWidth > maxAvailableWidth) {
            newWidth = maxAvailableWidth;
        } else if (newWidth < minResizeWidth) {
            newWidth = minResizeWidth;
        }

        setWidth(newWidth);
    }

    /**
     * Handles a resize event in the west (left) direction to the given cursor x
     * position.
     *
     * @param x the cursor scene-x position
     */
    private void handleResizeWest(final double x) {

        final double scaleFactor = getLocalToSceneTransform().getMxx();

        final double xDragDistance = (x - lastMouseX) / scaleFactor;
        final double minResizeWidth = Math.max(getMinWidth(), 0);

        double newLayoutX = lastLayoutX + xDragDistance;
        double newWidth = lastWidth - xDragDistance;
       
        // Even if snap-to-grid is off, we use Math.round to ensure drawing 'on-pixel' when zoomed in past 100%.
        final double roundedLayoutX = Math.round(newLayoutX);
        newWidth = Math.round(newWidth - roundedLayoutX + newLayoutX);
        newLayoutX = roundedLayoutX;

        // Min & max resize logic here.
        if (newLayoutX < 15)
        {
            newLayoutX = 15;
            newWidth = lastLayoutX + lastWidth - 15;
        }
        else if (newWidth < minResizeWidth)
        {
            newLayoutX = lastLayoutX + lastWidth - minResizeWidth;
            newWidth = minResizeWidth;
        }

        setLayoutX(newLayoutX);
        setWidth(newWidth);
    }
	
	/**
	 * Stores the relevant layout values at the time of the last mouse click
	 * (mouse-pressed event).
	 * 
	 * @param x The X-coordinate of the click event.
	 * @param y The Y-coordinate of the click event.
	 */ 
	private void storeClickForResize(final double x, final double y) {
		this.lastWidth = getWidth();
		this.lastHeight = getHeight();
		
		this.lastMouseRegion = getMouseRegion(x, y);
	}
	
	/**
	 * Return the particular sub-region exprimed as {@link RectangleMouseRegion} enumerations,
	 * of the <code>ResizableBox</code> that the given cursor position is in.
	 * 
	 * @param x The X-coordinate of the cursor.
	 * @param y The Y-coordinate of the cursor.
	 * @return  The corresponding sub-region that the cursor is located in.
	 */
	private RectangleMouseRegion getMouseRegion(final double x, final double y) {
		
		final double width = getWidth();
		final double height = getHeight();
		
		if(x < 0 || y < 0 || x > width || y > height) {
			return RectangleMouseRegion.OUTSIDE;
		}
		
		final boolean isNorth = y < resizeBorderTolerance;
        final boolean isSouth = y > height - resizeBorderTolerance;
        final boolean isEast = x > width - resizeBorderTolerance;
        final boolean isWest = x < resizeBorderTolerance;
        
        if(isNorth && isEast) {
        	return RectangleMouseRegion.NORTH_EAST;
        } else if (isNorth && isWest) {
            return RectangleMouseRegion.NORTH_WEST;
        } else if (isSouth && isEast) {
            return RectangleMouseRegion.SOUTH_EAST;
        } else if (isSouth && isWest) {
            return RectangleMouseRegion.SOUTH_WEST;
        } else if (isNorth) {
            return RectangleMouseRegion.NORTH;
        } else if (isSouth) {
            return RectangleMouseRegion.SOUTH;
        } else if (isEast) {
            return RectangleMouseRegion.EAST;
        } else if (isWest) {
            return RectangleMouseRegion.WEST;
        } else {
            return RectangleMouseRegion.INSIDE;
        }
	}
	
	/**
	 * Updates the JavaFX cursor style based on the specified {@link RectangleMouseRegion}.
	 * <p>
	 * It should occur for example when the cursor is near the border of the rectangle, to
	 * indicate that resizing is allowed.
	 * </p>
	 * 
	 * @param mouseRegion The sub-region where the cursor mouse is located.
	 */
    private void updateCursor(final RectangleMouseRegion mouseRegion) {
        switch (mouseRegion) {
            case NORTH_EAST:
                setCursor(Cursor.NE_RESIZE);
                break;
            case NORTH_WEST:
                setCursor(Cursor.NW_RESIZE);
                break;
            case SOUTH_EAST:
                setCursor(Cursor.SE_RESIZE);
                break;
            case SOUTH_WEST:
                setCursor(Cursor.SW_RESIZE);
                break;
            case NORTH:
                setCursor(Cursor.N_RESIZE);
                break;
            case SOUTH:
                setCursor(Cursor.S_RESIZE);
                break;
            case EAST:
                setCursor(Cursor.E_RESIZE);
                break;
            case WEST:
                setCursor(Cursor.W_RESIZE);
                break;

            case INSIDE:
            case OUTSIDE:
                // Set to null instead of Cursor.DEFAULT so it doesn't overwrite cursor settings of parent.
                setCursor(null);
                break;
        }
    }
	
	@Override
	public boolean isMouseInPositionForResize() {
		return mouseInPositionForResize;
	}
	
	/**
	 * Return the border tolerance for the purposes of resizing the <code>ResizableBox</code>.
	 * By default it is set to {@link #DEFAULT_RESIZE_BORDER_TOLERANCE}.
	 * <p>
	 * Drag events which take place within this distance of the rectangle border will be 
	 * interpreted as resize events. Further inside the rectangle, they will be treated
	 * as regular drag events.
	 * 
	 * @return The value specifying the resize border tolerance.
	 */
	public int getResizeBorderTolerance() {
		return resizeBorderTolerance;
	}
	
	/**
	 * Sets the border tolerance for the purposes of resizing the <code>ResizableBox</code>.
	 * The specified tolerance must be greater than 0, by default it is set to 
	 * {@link #DEFAULT_RESIZE_BORDER_TOLERANCE}.
	 * <p>
	 * Drag events which take place within this distance of the rectangle border will be 
	 * interpreted as resize events. Further inside the rectangle, they will be treated
	 * as regular drag events.
	 * 
	 * @param tolerance The value specifying the resize border tolerance.
	 */
	public void setResizeBorderTolerance(int tolerance) {
		Validator.positive(tolerance);
		this.resizeBorderTolerance = tolerance;
	}
	
	/**
	 * <code>RectangleMouseRegion</code> is an enumeration of possible regions around the border
	 * of a rectangle shape.
	 * <p>
	 * It is used during mouse hover and drag events on the {@link ResizableBox} to determine
	 * resize direction and what cursor is to display during resizing.
	 * 
	 * @author GnosticOccultist
	 */
	public enum RectangleMouseRegion {
		
		NORTH,
		NORTH_EAST,
		EAST,
		SOUTH_EAST,
		SOUTH,
		SOUTH_WEST,
		WEST,
		NORTH_WEST,
		INSIDE,
		OUTSIDE;
	}
}
