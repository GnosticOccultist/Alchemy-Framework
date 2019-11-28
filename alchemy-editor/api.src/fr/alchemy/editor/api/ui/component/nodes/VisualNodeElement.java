package fr.alchemy.editor.api.ui.component.nodes;

import java.util.UUID;

import fr.alchemy.utilities.Validator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * <code>VisualNodeElement</code> is a visual node representing a type of element inside a {@link VisualNodesContainer}. 
 *
 * @param <E> The type of element the node represent.
 * 
 * @author GnosticOccultist
 */
public class VisualNodeElement<E> extends VBox {

	/**
	 * The distance resize threshold where the node element can be resized from its bounds.
	 */
	private static final int RESIZE_THRESHOLD = 6;
	
	/**
	 * The selected state of the visual node.
	 */
	private final BooleanProperty selected = new BooleanPropertyBase(false) {
		
		@Override
		public  void invalidated() {
			pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), get());
		}
		
		@Override
		public Object getBean() {
			return VisualNodeElement.this;
		}

		@Override
		public String getName() {
			return "selected";
		}
	};
	
	/**
	 * The unique identifier of the node.
	 */
	protected final UUID id;
	/**
	 * The container of the visual node.
	 */
	protected final VisualNodesContainer container;
	/**
	 * The element wrapped in the node.
	 */
	protected final E element;
	/**
	 * The parameters container of the node.
	 */
	protected final VBox parametersContainer;
	/**
	 * The X and Y local coordinates of the node.
	 */
	protected double x, y;
	/**
	 * The last mouse X and Y local coordinates.
	 */
	protected double mouseX, mouseY;
	/**
	 * The last resized region of the node.
	 */
	private ResizeRegion lastResizeRegion = ResizeRegion.OUTSIDE;
	
	/**
	 * Instantiates a new <code>VisualNodeElement</code> to represent the provided element.
	 * 
	 * @param element The element to represent (not null).
	 */
	protected VisualNodeElement(VisualNodesContainer container, E element) {
		Validator.nonNull(element, "The element can't be null!");
		
		this.element = element;
		this.id = UUID.randomUUID();
		this.container = container;
		this.parametersContainer = new VBox();
		
		setOnMouseMoved(this::processMouseMoved);
		setOnMousePressed(this::processMousePressed);
		setOnMouseDragged(this::processMouseDragged);
		setOnMouseDragExited(this::processMouseExited);
		setOnContextMenuRequested(this::handleContextMenu);
		
		setPrefWidth(200D);
		getStyleClass().add("visual-node");
		
		createContent();
	}
	
	/**
	 * Creates the content of the <code>VisualNodeElement</code> by adding a header with the
	 * title and a container for parameters to add using {@link #constructParameters(VBox)}.
	 */
	protected void createContent() {
		StackPane header = new StackPane();
		Label titleLabel = new Label(getTitleText());
		
		header.getChildren().add(titleLabel);
		header.getStyleClass().add("header");
		
		getChildren().add(header);
		
		constructParameters(parametersContainer);
		getChildren().add(parametersContainer);
	}
	
	/**
	 * Constructs the parameters for the <code>VisualNodeElement</code>. Override this method
	 * to implement your own parameters into an implementation of this class.
	 * 
	 * @param container The container of the parameters (not null).
	 */
	protected void constructParameters(VBox container) {}
	
	/**
	 * Called by the {@link VisualNodesContainer} when it selects or deselects the <code>VisualNodeElement</code>.
	 * 
	 * @see VisualNodesContainer#requestSelection(VisualNodeElement)
	 * 
	 * @param value Whether the node needs to be selected or deselected.
	 */
	protected void onSelected(boolean value) {
		this.selected.setValue(value);
	}
	
	/**
	 * Handles the {@link ContextMenu} request of the <code>VisualNodeElement</code> by delegating its 
	 * creation to the associated {@link VisualNodesContainer}.
	 * 
	 * @param event The context menu event (not null).
	 */
	protected void handleContextMenu(ContextMenuEvent event) {
		Validator.nonNull(event, "The context menu event can't be null!");
		
		this.container.handleContextMenu(event);
	}
	
	/**
	 * Override this method to create your own actions to be registered when a {@link ContextMenu} is
	 * requested for the <code>VisualNodeElement</code>.
	 * 
	 * @param event   The context menu event (not null).
	 * @param actions The list of actions to add your actions to (not null).
	 */
	protected void fillAdditionalActions(ContextMenuEvent event, ObservableList<MenuItem> actions) {}
	
	/**
	 * Process the given moved {@link MouseEvent} by changing the {@link Cursor} according to the hovered region 
	 * of the <code>VisualNodeElement</code>.
	 * 
	 * @param event The mouse event that occured (not null).
	 */
	protected void processMouseMoved(MouseEvent event) {
		Validator.nonNull(event, "The mouse event can't be null!");
		
		if(event.isPrimaryButtonDown()) {
			return;
		}
		
		ResizeRegion region = getResizedRegion(event);
		setCursor(region.getFXCursor());
	}
	
	/**
	 * Process the given exited {@link MouseEvent} by changing the {@link Cursor} according to the one previously
	 * set by the parent node.
	 * 
	 * @param event The mouse event that occured (not null).
	 */
	protected void processMouseExited(MouseEvent event) {
		Validator.nonNull(event, "The mouse event can't be null!");
		
		if(!event.isPrimaryButtonDown()) {
			setCursor(null);
		}
	}
	
	/**
	 * Process the given dragged {@link MouseEvent} by moving the <code>VisualNodeElement</code> according to the mouse delta
	 * from previously invoked events.
	 * 
	 * @param event The mouse event that occured (not null).
	 */
	protected void processMouseDragged(MouseEvent event) {
		Validator.nonNull(event, "The mouse event can't be null!");
		
		if(canResize(lastResizeRegion)) {
			double mouseX = event.getX();
			setPrefWidth(getPrefWidth() + (mouseX - x));
			double mouseY = event.getY();
			setPrefHeight(getPrefHeight() + (mouseY - y));
			double layoutX = getLayoutX();
			double layoutY = getLayoutY();
	        setLayoutX(-1D);
	        setLayoutY(-1D);
	        setLayoutX(layoutX);
	        setLayoutY(layoutY);
			this.x = mouseX;
			this.y = mouseY;
		} else {
			// Get the local location of the mouse.
			Point2D posInParent = getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
			double offsetX = posInParent.getX() - mouseX;
			double offsetY = posInParent.getY() - mouseY;
				
			this.x = x + offsetX;
			this.y = y + offsetY;
				
			setLayoutX(x);
			setLayoutY(y);
				
			// Once the layout has been changed, update the mouse X and Y position for next call.
			this.mouseX = posInParent.getX();
			this.mouseY = posInParent.getY();
		}
		
		event.consume();
	}
	
	/**
	 * Process the given pressed {@link MouseEvent} by saving the mouse and the <code>VisualNodeElement</code> local coordinates,
	 * for later dragging events.
	 * 
	 * @param event The mouse event that occured (not null).
	 */
	protected void processMousePressed(MouseEvent event) {
		Validator.nonNull(event, "The mouse event can't be null!");
		
		if(event.getButton() != MouseButton.MIDDLE) {
			if(event.isControlDown() || (isSelected() && event.getButton() == MouseButton.SECONDARY)) {
				container.requestSelection(this, false);
			} else {
				container.requestSelection(this, true);
			}
		}
		
		if(event.getButton() != MouseButton.PRIMARY) {
			return;
		}
		
		this.lastResizeRegion = getResizedRegion(event);
		
		if(canResize(lastResizeRegion)) {
			this.x = event.getX();
			this.y = event.getY();
		} else {
			Point2D posInParent = getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
			
			// Record the current mouse X and Y position of the node
			this.mouseX = posInParent.getX();
			this.mouseY = posInParent.getY();
			
			this.x = getLayoutX();
			this.y = getLayoutY();
			
			toFront();
		}
		
		event.consume();
	}
	
	/**
	 * Return whether the <code>VisualNodeElement</code> can be resized by the provided {@link ResizeRegion}.
	 * 
	 * @param resizedRegion The region from which a resize has been requested (not null).
	 * @return				Whether the node element can be resized with the region.
	 */
	protected boolean canResize(ResizeRegion resizedRegion) {
		Validator.nonNull(resizedRegion, "The resized region can't be null!");
		return resizedRegion != ResizeRegion.OUTSIDE && resizedRegion != ResizeRegion.INSIDE;
	}
	
	/**
	 * Return the {@link ResizeRegion} from which the {@link MouseEvent} can resize the <code>VisualNodeElement</code>.
	 * 
	 * @param event The mouse event containing the mouse position (not null).
	 * @return		The region from which a resize can be performed.
	 */
	private ResizeRegion getResizedRegion(MouseEvent event) {
		Validator.nonNull(event, "The mouse event can't be null!");
		
		double x = event.getX();
		double y = event.getY();
		double width = getWidth();
		double height = getHeight();
		
		// Check first if we're totally outisde to avoid useless checks.
		if(x < 0 || y < 0 || x > width || y > height) {
			return ResizeRegion.OUTSIDE;
		}
		
		boolean north = y < RESIZE_THRESHOLD;
		boolean south = y > height - RESIZE_THRESHOLD;
		boolean east = x > width - RESIZE_THRESHOLD;
		boolean west = x < RESIZE_THRESHOLD;
		
		return ResizeRegion.get(north, south, east, west, true);
	}
	
	/**
	 * Return whether the provided {@link VisualNodeParameter} can be attached together.
	 * 
	 * @param in  The in visual node parameter (not null).
	 * @param out The out visual node parameter (not null).
	 * @return	  Whether the two parameters can be attached.
	 */
	public boolean canAttach(VisualNodeParameter in, VisualNodeParameter out) {
		if(in.getElement().getID().equals(out.getElement().getID())) {
			return false;
		}
		
		if(!in.isInput() || out.isInput()) {
			return false;
		}
		
		return true;
	}

	/**
	 * Return the title text of the <code>VisualNodeElement</code>, which is displayed
	 * in the header of the node representation.
	 * 
	 * @return The title text of the node.
	 */
	protected String getTitleText() {
		return "Title";
	}
	
	/**
	 * Return the unique identifier of the <code>VisualNodeElement</code>.
	 * 
	 * @return The unique identifier of the node (not null).
	 */
	public UUID getID() {
		return id;
	}
	
	/**
	 * Return whether the <code>VisualNodeElement</code> is selected.
	 * 
	 * @return Whether the visual node is selected.
	 */
	public boolean isSelected() {
		return selected.get();
	}
	
	/**
	 * <code>ResizeRegion</code> is an enumeration of possible resizable regions around the border
	 * of a rectangular shape.
	 * <p>
	 * It is used during mouse hover and drag events on the {@link VisualNodeElement} to determine
	 * resize direction and what cursor is to display during resizing.
	 * 
	 * @see VisualNodeElement#changeCursor(MouseEvent)
	 * 
	 * @author GnosticOccultist
	 */
	public enum ResizeRegion {
		/**
		 * The north side of the rectangular shape.
		 */
		NORTH,
		/**
		 * The south side of the rectangular shape.
		 */
		SOUTH,
		/**
		 * The east side of the rectangular shape.
		 */
		EAST,
		/**
		 * The west side of the rectangular shape.
		 */
		WEST,
		/**
		 * The north-east side of the rectangular shape.
		 */
		NORTH_EAST,
		/**
		 * The north-west side of the rectangular shape.
		 */
		NORTH_WEST,
		/**
		 * The south-east side of the rectangular shape.
		 */
		SOUTH_EAST,
		/**
		 * The south-west side of the rectangular shape.
		 */
		SOUTH_WEST,
		/**
		 * The inside of the rectangular shape.
		 */
		INSIDE,
		/**
		 * The outside of the rectangular shape.
		 */
		OUTSIDE;
		
		/**
		 * Return the type of {@link Cursor} corresponding to the <code>ResizeRegion</code>.
		 * 
		 * @return The type of cursor corresponding to the resize region, or null if the resize region 
		 * 		   is inside or outside the resize margin.
		 */
		public Cursor getFXCursor() {
			switch (this) {
	            case NORTH_EAST:
	                return Cursor.NE_RESIZE;
	            case NORTH_WEST:
	            	return Cursor.NW_RESIZE;
	            case SOUTH_EAST:
	            	return Cursor.SE_RESIZE;
	            case SOUTH_WEST:
	            	return Cursor.SW_RESIZE;
	            case NORTH:
	                return Cursor.N_RESIZE;
	            case SOUTH:
	                return Cursor.S_RESIZE;
	            case EAST:
	                return Cursor.E_RESIZE;
	            case WEST:
	            	return Cursor.W_RESIZE;
	
	            case INSIDE:
	            case OUTSIDE:
	                // Set to null instead of Cursor.DEFAULT so it doesn't overwrite cursor settings of parent.
	            	return null;
	        }
			return null;
		}
		
		/**
		 * Return a <code>ResizeRegion</code> matching the provided boolean values.
		 * 
		 * @param north  Whether the north region is focused.
		 * @param south  Whether the south region is focused.
		 * @param east   Whether the east region is focused.
		 * @param west   Whether the west region is focused.
		 * @param inside Whether the inside region is focused.
		 * @return		 The resize region matching the boolean (not null).
		 */
		public static ResizeRegion get(boolean north, boolean south, boolean east, boolean west, boolean inside) {
			if(north && east) {
	        	return ResizeRegion.NORTH_EAST;
	        } else if (north && west) {
	            return ResizeRegion.NORTH_WEST;
	        } else if (south && east) {
	            return ResizeRegion.SOUTH_EAST;
	        } else if (south && west) {
	            return ResizeRegion.SOUTH_WEST;
	        } else if (north) {
	            return ResizeRegion.NORTH;
	        } else if (south) {
	            return ResizeRegion.SOUTH;
	        } else if (east) {
	            return ResizeRegion.EAST;
	        } else if (west) {
	            return ResizeRegion.WEST;
	        } else if (inside) {
	        	return ResizeRegion.INSIDE;
	        }
			
			return ResizeRegion.OUTSIDE;
		}
	}
}
