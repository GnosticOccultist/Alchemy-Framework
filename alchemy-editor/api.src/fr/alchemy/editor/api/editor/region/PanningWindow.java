package fr.alchemy.editor.api.editor.region;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

/**
 * <code>PanningWindow</code> is an implementation of {@link Region}, which is used to create a window
 * over a large region of content, in the way that the window can be panned around relative to its content.
 * <p>
 * Note that only the parts of the content which are inside the window will be rendered. Everything outside
 * of it will be clipped.
 * 
 * @author GnosticOccultist
 */
public class PanningWindow extends Region {
	
	/**
	 * The clipping rectangle shape.
	 */
	private final Rectangle clip = new Rectangle();
	/**
	 * The region where the content is situated.
	 */
	private Region content;
	/**
	 * The content X-axis property.
	 */
	private final DoubleProperty contentX = new SimpleDoubleProperty() {
		
		@Override
		protected void invalidated() {
			requestLayout();
		}
	};
	/**
	 * The content Y-axis property.
	 */
	private final DoubleProperty contentY = new SimpleDoubleProperty() {
		
		@Override
		protected void invalidated() {
			requestLayout();
		}
	};
	/**
	 * The scroll bar for the X-axis.
	 */
	private final ScrollBar scrollX = new ScrollBar();
	/**
	 * The scroll bar for the Y-axis.
	 */
	private final ScrollBar scrollY = new ScrollBar();
	/**
	 * The zooming property.
	 */
	private final DoubleProperty zoom = new SimpleDoubleProperty(1);
	/**
	 * The scaling of the window.
	 */
	private final Scale scale = new Scale();
	
	/**
	 * Instantiates a new <code>PanningWindow</code>.
	 */
	public PanningWindow() {

		// Setup the clipping area to fit the width and height of the window.
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
		setClip(clip);
		
		scale.xProperty().bind(zoom);
		scale.yProperty().bind(zoom);
		
		getChildren().addAll(scrollX, scrollY);
		
        scrollX.setOrientation(Orientation.HORIZONTAL);
        scrollX.valueProperty().bindBidirectional(contentX);
        
        scrollY.setOrientation(Orientation.VERTICAL);
        scrollY.valueProperty().bindBidirectional(contentY);
	}
	
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		
		final double width = getWidth();
		final double height = getHeight();
		
		content.relocate(-contentX.get(), -contentY.get());
		
		final double w = scrollY.getWidth();
		final double h = scrollX.getHeight();
		
		scrollX.resizeRelocate(0, snapPosition(height - h), snapSize(width - w), h);
		scrollY.resizeRelocate(snapPosition(width - w), 0, w, snapSize(height - h));
		
		scrollX.setMin(0);
		scrollX.setMax(getMaxX());
		scrollX.setVisibleAmount(width);
		scrollY.setMin(0);
		scrollY.setMax(getMaxY());
		scrollY.setVisibleAmount(height);
	}
	
	/**
	 * Checks that the <code>PanningWindow</code> bounds are completely inside the content bounds,
	 * and repositions if necessary.
	 * <p>
	 * Also checks that the window position values are integers to ensure strokes are drawn cleanly.
	 * </p>
	 */
	protected void checkWindowBounds() {
		panTo(getContentX(), getContentY());
	}
	
	/**
	 * Pans the <code>PanningWindow</code> to the specified X and Y coordinates.
	 * The window can't be panned outside the content. When the window 'hits the edge' of the
	 * content it will stop.
	 * 
	 * @param x The X coordinate of the content top-left corner.
	 * @param y The Y coordinate of the content top-left corner.
	 */
	public void panTo(final double x, final double y) {
		final double newX = checkContentX(x);
		final double newY = checkContentY(y);
		
		if(newX != getContentX() || newY != getContentY()) {
			contentX.set(newX);
			contentY.set(newY);
		}
	}
	
	/**
	 * Sets the content of the <code>PanningWindow</code>.
	 * Note that the content's managed attribute will be set to false. Its size must therefore be
	 * set manually using the {@link Region#resize(double, double)} method of the {@link Region} class.
	 * 
	 * @param content The region to be displayed inside the panning window.
	 */
	public void setContent(final Region content) {
		// Remove children and release bindings from old content, if any exists.
		final Region prevContent = content;
		if(prevContent != null) {
			getChildren().remove(prevContent);
			prevContent.getTransforms().remove(scale);
		}
		
		this.content = content;
		
		if(content != null) {
			content.setManaged(false);
			getChildren().add(content);
			content.getTransforms().add(scale);
			
			scrollX.setVisible(true);
			scrollY.setVisible(true);
		} else {
			scrollX.setVisible(false);
			scrollY.setVisible(false);
		}
	}
	
	/**
	 * Return the X coordinate of the top-left corner of the content for the 
	 * <code>PanningWindow</code>.
	 * 
	 * @return The X coordinate of the content top-left corner.
	 */
	public double getContentX() {
		return contentX.get();
	}
	
	/**
	 * Return the Y coordinate of the top-left corner of the content for the 
	 * <code>PanningWindow</code>.
	 * 
	 * @return The Y coordinate of the content top-left corner.
	 */
	public double getContentY() {
		return contentY.get();
	}
	
	/**
	 * Return the maximum scrollable distance for the <code>PanningWindow</code> on
	 * the X-axis. It compares the actual content width to the window width.
	 * 
	 * @return The max scrollable distance for the window on X-axis.
	 */
	private double getMaxX() {
		return content != null ? content.getBoundsInParent().getWidth() - getWidth() : 0;
	}
	
	/**
	 * Return the maximum scrollable distance for the <code>PanningWindow</code> on
	 * the Y-axis. It compares the actual content height to the window height.
	 * 
	 * @return The max scrollable distance for the window on Y-axis.
	 */
	private double getMaxY() {
		return content != null ? content.getBoundsInParent().getHeight() - getHeight() : 0;
	}
	
	private double checkContentX(final double xToCheck) {
		return snapPosition(Math.min(getMaxX(), Math.max(xToCheck, 0)));
	}
	
	private double checkContentY(final double yToCheck) {
		return snapPosition(Math.min(getMaxY(), Math.max(yToCheck, 0)));
	}
}
