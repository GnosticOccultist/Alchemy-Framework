package fr.alchemy.core.physic;

import fr.alchemy.core.scene.entity.Entity;
import javafx.geometry.Point2D;

/**
 * <code>BoundingCircle</code> is a circle shape box which represents the bounds of a 
 * scene-graph object such as {@link Entity}.
 * It is defined with a center point and a radius.
 * 
 * @author GnosticOccultist
 */
public final class BoundingCircle {
	
	/**
	 * The center point of the bounding circle.
	 */
	private double x, y;
	/**
	 * The radius of the bounding circle.
	 */
	private double radius;
	
	public BoundingCircle(final double x, final double y, final double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public BoundingCircle(final double radius) {
		this.radius = radius;
	}
	
	/**
	 * @return Whether the provided point is contained entirely inside the <code>BoundingCircle</code>.
	 */
	public boolean contains(final Point2D point) {
		return contains(point.getX(), point.getY());
	}
	
	/**
	 * @return Whether the provided point is contained entirely inside the <code>BoundingCircle</code>.
	 */
	public boolean contains(final double x, final double y) {
		final double dx = this.x - x;
		final double dy = this.y - y;
		
		return dx * dx + dy * dy <= radius * radius;
	}
	
	/**
	 * Checks if the provided <code>BoundingCircle</code> is entirely contained within this one.
	 * 
	 * @param circle The bounding circle to check with.
	 * @return		 Whether the circle is contained withing this one.
	 */
	public boolean contains(final BoundingCircle circle) {
		// Checks if circle to test is bigger.
		final double dRadius = radius - circle.radius;
		if (dRadius < 0f) {
			return false; 
		}
		
		final double dx = x - circle.x;
		final double dy = y - circle.y;
		
		final double totalRadius = radius + circle.radius;
		return (!(dRadius * dRadius < dx * dx + dy * dy) && (dx * dx + dy * dy < totalRadius * totalRadius));
	}
	
	/***
	 * Sets the values of the <code>BoundingCircle</code> to the
	 * provided one.
	 * 
	 * @param other The other bounding circle to get the values from.
	 * @return		The updated bounding circle.
	 */
	public BoundingCircle set(final BoundingCircle other) {
		this.x = other.x;
		this.y = other.y;
		this.radius = other.radius;
		
		return this;
	}
	
	/**
	 * @return The center X coordinate.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Sets the center of the <code>BoundingCircle</code> X coordinate.
	 * 
	 * @param x The center X coordinate.
	 * @return  The updated bounding circle.
	 */
	public BoundingCircle setX(final double x) {
		this.x = x;
		return this;
	}
	
	/**
	 * @return The center Y coordinate.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Sets the center of the <code>BoundingCircle</code> Y coordinate.
	 * 
	 * @param y The center Y coordinate.
	 * @return  The updated bounding circle.
	 */
	public BoundingCircle setY(final double y) {
		this.y = y;
		return this;
	}
	
	/**
	 * @return The radius of the <code>BoundingCircle</code>.
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Sets the radius of the <code>BoundingCircle</code>. 
	 * 
	 * @param  The radius of the bounding circle.
	 * @return The updated bounding circle.
	 */
	public BoundingCircle setRadius(final double radius) {
		this.radius = radius;
		return this;
	}
}
