package fr.alchemy.core.physic;

import fr.alchemy.core.scene.entity.Entity;
import javafx.geometry.Point2D;

/**
 * <code>BoundingBox</code> is a rectangular box which represents the bounds of a 
 * scene-graph object such as {@link Entity}.
 * It is defined with an upper-left corner point and a width and height for each axis.
 * 
 * @author GnosticOccultist
 */
public final class BoundingBox {
	/**
	 * The upper-left corner of the bounding box.
	 */
	private double x, y;
	/**
	 * The width and height of the bounding box.
	 */
	private double width, height;
	
	public BoundingBox(final double width, final double height) {
		this(0, 0, width, height);
	}
	
	public BoundingBox(final Point2D corner, final double width, final double height) {
		this(corner.getX(), corner.getY(), width, height);
	}
	
	public BoundingBox(final double x, final double y, final double width, final double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return Whether the provided point is contained entirely inside the <code>BoundingBox</code>.
	 */
	public boolean contains(final Point2D point) {
		return contains(point.getX(), point.getY());
	}
	
	/**
	 * @return Whether the provided point is contained entirely inside the <code>BoundingBox</code>.
	 */
	public boolean contains(final double x, final double y) {
		return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
	}
	
	/**
	 * Checks if the provided <code>BoundingBox</code> is entirely contained within this one.
	 * 
	 * @param box The bounding box to check with.
	 * @return	  Whether the box is contained within this one.
	 */
	public boolean contains(final BoundingBox box) {
		final double minX = box.x;
		final double maxX = minX + box.width;

		final double minY = box.y;
		final double maxY = minY + box.height;

		return ((minX > x && minX < x + width) && (maxX > x && maxX < x + width))
			&& ((minY > y && minY < y + height) && (maxY > y && maxY < y + height));
	}
	
	/**
	 * @return The perimeter of the <code>BoundingBox</code>.
	 */
	public double perimeter() {
		return 2 * (width + height);
	}
	
	/**
	 * @return The area of the <code>BoundingBox</code>.
	 */
	public double area() {
		return width * height;
	}
	
	/**
	 * @return The center coordinates of the <code>BoundingBox</code>.
	 */
	public Point2D getCenter() {
		return new Point2D(x + width / 2, y + height / 2);
	}
	
	/**
	 * Centers the <code>BoundingBox</code> on the provided coordinates.
	 * 
	 * @return The updated bounding box.
	 */
	public BoundingBox center(final double x, final double y) {
		setX(x - width / 2);
		setY(y - height / 2);
		return this;
	}
	
	/***
	 * Sets the values of the <code>BoundingBox</code> to the
	 * provided one.
	 * 
	 * @param other The other bounding box to get the values from.
	 * @return		The updated bounding box.
	 */
	public BoundingBox set(final BoundingBox other) {
		this.x = other.x;
		this.y = other.y;
		this.width = other.width;
		this.height = other.height;
		
		return this;
	}
	
	/**
	 * @return The upper-left X coordinate.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Sets the upper-left X coordinate.
	 * 
	 * @param x The upper-left X coordinate.
	 * @return  The updated bounding box.
	 */
	public BoundingBox setX(final double x) {
		this.x = x;
		return this;
	}
	
	/**
	 * @return The upper-left Y coordinate.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Sets the upper-left Y coordinate.
	 * 
	 * @param y The upper-left Y coordinate.
	 * @return  The updated bounding box.
	 */
	public BoundingBox setY(final double y) {
		this.y = y;
		return this;
	}
	
	/**
	 * @return The width of the <code>BoundingBox</code>.
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * Sets the width of the <code>BoundingBox</code>.
	 * 
	 * @param width The width.
	 * @return The updated bounding box.
	 */
	public BoundingBox setWidth(final double width) {
		this.width = width;
		return this;
	}
	
	/**
	 * @return The height of the <code>BoundingBox</code>.
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Sets the width of the <code>BoundingBox</code>.
	 * 
	 * @param height The height.
	 * @return The updated bounding box.
	 */
	public BoundingBox setHeight(final double height) {
		this.height = height;
		return this;
	}
	
	@Override
	public boolean equals(final Object o) {
		if(this == o) {
			return true;
		}
		if(o == null || !(o instanceof BoundingBox)) {
			return false;
		}
		BoundingBox box = (BoundingBox) o;
		if(width != box.width || height != box.height || x != box.x || y != box.y) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": Corner[" + x + ";" + y + "]" 
					+ " Width: " + width + " Height: " + height;
	}
}
